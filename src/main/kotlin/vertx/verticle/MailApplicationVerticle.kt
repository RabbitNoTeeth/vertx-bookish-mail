package vertx.verticle

import `fun`.bookish.vertx.activemq.client.core.ActiveMQClient
import config.MailApplicationConfig
import config.MailConfigConstants
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.MailConfig
import io.vertx.ext.mail.StartTLSOptions
import io.vertx.ext.mail.MailMessage
import org.apache.log4j.Logger
import util.TemplateFileLoader


class MailApplicationVerticle : AbstractVerticle() {

    private val logger: Logger = Logger.getLogger(this.javaClass)

    override fun start() {

        val mailConfig = getMailConfig()

        val mailClient = MailClient.createShared(this.vertx,mailConfig)

        val activeMQClient = ActiveMQClient.create(this.vertx,MailApplicationConfig.activeMQConfig)

        val registerSubscriber = activeMQClient.createSubscriber("mailApp-registerSubscriber",
                MailApplicationConfig.activeMQConfig.getString(MailConfigConstants.ACTIVEMQ_TOPIC_MAIL_REGISTER))

        // 监听用户注册消息
        registerSubscriber.listen {
            if(it.succeeded()){
                sendRegisterMail(mailClient,it.result())
            }else{
                throw RuntimeException(it.cause())
            }
        }


        val subscribeSubscriber = activeMQClient.createSubscriber("mailApp-subscribeSubscriber",
                MailApplicationConfig.activeMQConfig.getString(MailConfigConstants.ACTIVEMQ_TOPIC_MAIL_SUBSCRIBE))

        // 监听用户订阅消息
        subscribeSubscriber.listen {
            if(it.succeeded()){
                sendSubscribeMail(mailClient,it.result())
            }else{
                throw RuntimeException(it.cause())
            }
        }


    }

    /**
     * 发送注册邮件
     */
    private fun sendRegisterMail(mailClient: MailClient,message: JsonObject) {

        logger.info("收到新的注册用户消息,准备发送邮件...")

        val mail = MailMessage().apply {
            from = MailApplicationConfig.mailConfig.getString(MailConfigConstants.MAIL_DEFAULT_FROM)
            to = listOf(message.getString("email"))
            subject = "亲爱的\"" + message.getString("username") + "\", 欢迎您加入Bookish !"
            text = TemplateFileLoader.loadRegisterMailTemplate()
                    .replace("USERNAME", message.getString("username"))
        }

        mailClient.sendMail(mail){
            if(it.succeeded()){
                logger.info("用户注册邮件发送成功!")
            }else{
                throw RuntimeException("用户注册邮件发送失败!",it.cause())
            }
        }

    }


    /**
     * 发送订阅邮件
     */
    private fun sendSubscribeMail(mailClient: MailClient,message: JsonObject) {

        logger.info("收到新的文章发布消息,准备发送订阅邮件...")

        val mail = MailMessage().apply {
            from = MailApplicationConfig.mailConfig.getString(MailConfigConstants.MAIL_DEFAULT_FROM)
            to = message.getString("emails").split(",")
            subject = "Bookish博客网:您订阅的作者[${message.getString("author")}]发布了新文章!"
            text = TemplateFileLoader.loadSubscribeMailTemplate()
                    .replace("AUTHOR", message.getString("author"))
                    .replace("ARTICLE_TITLE", message.getString("title"))
                    .replace("ARTICLE_CODE", message.getString("articleId"))
        }

        mailClient.sendMail(mail){
            if(it.succeeded()){
                logger.info("用户订阅邮件发送成功!")
            }else{
                throw RuntimeException("用户订阅邮件发送失败!",it.cause())
            }
        }

    }


    /**
     * 加载mail配置
     */
    private fun getMailConfig(): MailConfig{
        return MailConfig().apply {
            hostname = MailApplicationConfig.mailConfig.getString(MailConfigConstants.MAIL_HOST)
            port = MailApplicationConfig.mailConfig.getInteger(MailConfigConstants.MAIL_PORT)
            username = MailApplicationConfig.mailConfig.getString(MailConfigConstants.MAIL_USERNAME)
            password = MailApplicationConfig.mailConfig.getString(MailConfigConstants.MAIL_PASSWORD)
            isSsl = MailApplicationConfig.mailConfig.getBoolean(MailConfigConstants.MAIL_SSL)
            if(MailApplicationConfig.mailConfig.getBoolean(MailConfigConstants.MAIL_STARTTLS)){
                starttls = StartTLSOptions.REQUIRED
            }
        }
    }



}


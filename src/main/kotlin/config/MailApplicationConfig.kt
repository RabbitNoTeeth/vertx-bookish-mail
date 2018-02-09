package config

import io.vertx.core.json.JsonObject
import java.util.*

object MailApplicationConfig {

    val activeMQConfig = JsonObject()

    val mailConfig = JsonObject()

    fun loadConfig(){
        try {
            val classLoader = this.javaClass.classLoader
            classLoader.getResourceAsStream(MailConfigConstants.PROFILES_FILE_NAME).use {
                val profilesProperties = Properties().apply { load(it) }
                val activeProfiles =
                        profilesProperties.getProperty(MailConfigConstants.PROFILES_ACTIVE)?:throw IllegalStateException("未找到配置项:${MailConfigConstants.PROFILES_ACTIVE}")
                classLoader.getResourceAsStream(activeProfiles + ".properties").use {
                    val profilesSettings = Properties().apply { load(it) }
                    //加载activeMQ配置
                    this.loadActiveMQConfig(profilesSettings)
                    //加载mail配置
                    loadMailConfig(profilesSettings)
                }
            }
        }catch (e:Exception){
            throw RuntimeException(e)
        }
    }


    /**
     * 加载activeMQ配置
     */
    private fun loadActiveMQConfig(properties: Properties){
        this.activeMQConfig.put("clientID",
                properties.getProperty(MailConfigConstants.ACTIVEMQ_CLIENT_ID)?:null)
        this.activeMQConfig.put("username",
                properties.getProperty(MailConfigConstants.ACTIVEMQ_USERNAME)?: throw IllegalStateException("未找到配置项:${MailConfigConstants.ACTIVEMQ_USERNAME}"))
        this.activeMQConfig.put("password",
                properties.getProperty(MailConfigConstants.ACTIVEMQ_PASSWORD)?: throw IllegalStateException("未找到配置项:${MailConfigConstants.ACTIVEMQ_PASSWORD}"))
        this.activeMQConfig.put("brokerURL",
                properties.getProperty(MailConfigConstants.ACTIVEMQ_BROKER_URL)?: throw IllegalStateException("未找到配置项:${MailConfigConstants.ACTIVEMQ_BROKER_URL}"))


        this.activeMQConfig.put(MailConfigConstants.ACTIVEMQ_PREFIX_QUEUE_USER,
                properties.getProperty(MailConfigConstants.ACTIVEMQ_PREFIX_QUEUE_USER)?: throw IllegalStateException("未找到配置项:${MailConfigConstants.ACTIVEMQ_PREFIX_QUEUE_USER}"))
        this.activeMQConfig.put(MailConfigConstants.ACTIVEMQ_TOPIC_MSG_SYSTEM,
                properties.getProperty(MailConfigConstants.ACTIVEMQ_TOPIC_MSG_SYSTEM)?: throw IllegalStateException("未找到配置项:${MailConfigConstants.ACTIVEMQ_TOPIC_MSG_SYSTEM}"))
        this.activeMQConfig.put(MailConfigConstants.ACTIVEMQ_TOPIC_MAIL_REGISTER,
                properties.getProperty(MailConfigConstants.ACTIVEMQ_TOPIC_MAIL_REGISTER)?: throw IllegalStateException("未找到配置项:${MailConfigConstants.ACTIVEMQ_TOPIC_MAIL_REGISTER}"))
        this.activeMQConfig.put(MailConfigConstants.ACTIVEMQ_TOPIC_MAIL_SUBSCRIBE,
                properties.getProperty(MailConfigConstants.ACTIVEMQ_TOPIC_MAIL_SUBSCRIBE)?: throw IllegalStateException("未找到配置项:${MailConfigConstants.ACTIVEMQ_TOPIC_MAIL_SUBSCRIBE}"))
    }

    /**
     * 加载mail配置
     */
    private fun loadMailConfig(properties: Properties){
        this.mailConfig.put(MailConfigConstants.MAIL_HOST,
                properties.getProperty(MailConfigConstants.MAIL_HOST)?: throw IllegalStateException("未找到配置项:${MailConfigConstants.MAIL_HOST}"))
        this.mailConfig.put(MailConfigConstants.MAIL_PORT,
                properties.getProperty(MailConfigConstants.MAIL_PORT)?.toInt()?: throw IllegalStateException("未找到配置项:${MailConfigConstants.MAIL_PORT}"))
        this.mailConfig.put(MailConfigConstants.MAIL_USERNAME,
                properties.getProperty(MailConfigConstants.MAIL_USERNAME)?: throw IllegalStateException("未找到配置项:${MailConfigConstants.MAIL_USERNAME}"))
        this.mailConfig.put(MailConfigConstants.MAIL_PASSWORD,
                properties.getProperty(MailConfigConstants.MAIL_PASSWORD)?: throw IllegalStateException("未找到配置项:${MailConfigConstants.MAIL_PASSWORD}"))
        this.mailConfig.put(MailConfigConstants.MAIL_STARTTLS,
                properties.getProperty(MailConfigConstants.MAIL_STARTTLS)?.toBoolean()?: throw IllegalStateException("未找到配置项:${MailConfigConstants.MAIL_STARTTLS}"))
        this.mailConfig.put(MailConfigConstants.MAIL_SSL,
                properties.getProperty(MailConfigConstants.MAIL_SSL)?.toBoolean()?: throw IllegalStateException("未找到配置项:${MailConfigConstants.MAIL_SSL}"))
        this.mailConfig.put(MailConfigConstants.MAIL_DEFAULT_FROM,
                properties.getProperty(MailConfigConstants.MAIL_DEFAULT_FROM)?: throw IllegalStateException("未找到配置项:${MailConfigConstants.MAIL_DEFAULT_FROM}"))
    }


}

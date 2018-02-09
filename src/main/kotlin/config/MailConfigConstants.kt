package config


object MailConfigConstants {


    //------------------------环境配置--------------------------

    val PROFILES_FILE_NAME = "profiles.properties"
    val PROFILES_ACTIVE = "profiles.active"


    //------------------------activeMq配置--------------------------

    val ACTIVEMQ_CLIENT_ID = "activeMQ.base.clientID"
    val ACTIVEMQ_USERNAME = "activeMQ.base.username"
    val ACTIVEMQ_PASSWORD = "activeMQ.base.password"
    val ACTIVEMQ_BROKER_URL = "activeMQ.base.brokerURL"

    val ACTIVEMQ_PREFIX_QUEUE_USER = "activeMQ.ext.prefix.queue.user"
    val ACTIVEMQ_TOPIC_MSG_SYSTEM = "activeMQ.ext.topic.msg.system"
    val ACTIVEMQ_TOPIC_MAIL_REGISTER = "activeMQ.ext.topic.mail.register"
    val ACTIVEMQ_TOPIC_MAIL_SUBSCRIBE = "activeMQ.ext.topic.mail.subscribe"

    //------------------------mail配置--------------------------

    val MAIL_HOST = "mail.host"
    val MAIL_PORT = "mail.port"
    val MAIL_USERNAME = "mail.username"
    val MAIL_PASSWORD = "mail.password"
    val MAIL_STARTTLS = "mail.startTLS"
    val MAIL_SSL = "mail.ssl"
    val MAIL_DEFAULT_FROM = "mail.default.from"


}
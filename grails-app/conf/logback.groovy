import ch.qos.logback.classic.encoder.PatternLayoutEncoder

appender("Console", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d %-5level %logger %msg%n"
    }
}

appender("R", RollingFileAppender) {
    file = "hfyz.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%d %-5level %msg%n"
    }

    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "hfyz.log.%d{yyyy-MM-dd}"
        maxHistory = 5
    }
}

logger("asset", WARN)
logger("net", WARN)
logger("org", ERROR)
logger("hfyz", ERROR)
logger("kafka", ERROR)
logger("grails.plugins", ERROR)
logger("grails.artefact", ERROR)
logger("grails.plugin", ERROR)
logger("grails.web", ERROR)
logger("grails.core", ERROR)
logger("grails.boot", ERROR)
logger("grails.util", ERROR)
logger("grails.app.services.org.geeks.browserdetection.UserAgentIdentService", ERROR)
logger("ugrails.Application", WARN)
logger("net.sf.ehcache.config.ConfigurationFactory", ERROR)
logger("reactor", WARN)

final String LOG_LEVEL = System.getProperty("LOG_LEVEL") ?:
        System.getenv("LOG_LEVEL")

root(valueOf(LOG_LEVEL), ["Console", "R"])

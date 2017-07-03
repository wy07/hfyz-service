package com.commons.utils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogUtils {
    private static String HOST_NAME = InetAddress.localHost.hostName
    private static Logger log = LoggerFactory.getLogger(this)

    static String debug(String type, Map params, String platform, String exception) {
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def message =  "${HOST_NAME} - ${params.action} ${type} ${params} ${params?.agent ? params?.agent : '-'} ${platform} ${exception ? exception : '-'}"
        log.debug message
    }

    static String info(String type, Map params, String username, String platform, String describe) {
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def message =  "${HOST_NAME} - ${params.action} ${type} ${params} ${username ? username : '-'} ${params?.agent ? params?.agent : '-'} ${platform} ${describe ? describe : '-'}"
        log.info message
    }

    static String error(String type, Map params, String username, String platform, String describe, String exception) {
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def message =  "${HOST_NAME} - ${params.action} ${type} ${params} ${username ? username : '-'} ${params?.agent ? params?.agent : '-'} ${platform} ${describe ? describe : '-'} ${exception ? exception : '-'}"
        log.error message
    }
}

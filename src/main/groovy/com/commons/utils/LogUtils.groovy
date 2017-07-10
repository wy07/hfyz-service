package com.commons.utils

import org.grails.web.util.WebUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

class LogUtils {
    private static String HOST_NAME = InetAddress.localHost.hostName
    private static Logger log = LoggerFactory.getLogger(this)

    static String debug(Class clazz, String type, Map params, String platform) {
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def session = WebUtils.retrieveGrailsWebRequest().getSession()
        def request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
        def IP = request.getRequest().getHeader("X-Real-IP")?:request.getRequest().getRemoteAddr()
        def action = params.action
        def agent = '"' + session.getAttribute('agent') + '"'
        def message =  "${HOST_NAME} ${IP} ${clazz.name} ${action} ${type} ${agent} ${platform} ${params}"
        log.debug message
    }

    static warn(Logger logTest, Object ... args) {
        StringBuilder sb = new StringBuilder()
        for(Object o : args) {
            sb.append(o).append(" ")
        }
        logTest.warn sb.toString()
    }

    static String info(Class clazz, String type, Map params, String username, String platform, String describe) {
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def session = WebUtils.retrieveGrailsWebRequest().getSession()
        def request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
        def IP = request.getRequest().getHeader("X-Real-IP")?:request.getRequest().getRemoteAddr()
        def action = params.action
        def agent = '"' + session.getAttribute('agent') + '"'
        def message =  "${HOST_NAME} ${IP} ${clazz.name} ${action} ${type} ${username ?: '-'} ${agent} ${platform} ${describe ?: '-'} ${params} "
        log.info message
    }

    static String error(Class clazz, String type, Map params, String username, String platform, String describe, String exception) {
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def session = WebUtils.retrieveGrailsWebRequest().getSession()
        def request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
        def IP = request.getRequest().getHeader("X-Real-IP")?:request.getRequest().getRemoteAddr()
        def action = params.action
        def agent = '"' + session.getAttribute('agent') + '"'
        def exceptionStr = '"' + exception + '"'
        def message =  "${HOST_NAME} ${IP} ${clazz.name} ${action} ${type} ${username ?: '-'} ${agent} ${platform} ${exceptionStr ?: '-'} ${params} "
        log.error message
    }
}

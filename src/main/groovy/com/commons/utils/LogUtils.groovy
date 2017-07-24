package com.commons.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

class LogUtils {
    private static String HOST_NAME = InetAddress.localHost.hostName
    private static Logger log = LoggerFactory.getLogger(this)

//    主机名 IP 类名 方法名 操作类别 操作人 UA 平台 操作描述 错误信息 参数

    static String debug(Class clazz, String msg) {
        def message = "${HOST_NAME} - ${clazz.name} - - - - - - - ${msg}"
        log.debug message
    }

    static String debug(Class clazz, Map params) {
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def action = params.action
        def message = "${HOST_NAME} - ${clazz.name} ${action} ${params} - - - ${params?.platform ?: '-'} - -"
        log.debug message
    }

    static String debug(Class clazz, Map params, Throwable t) {
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def action = params.action
        def agent = '"-"'
        def message = "${HOST_NAME} - ${clazz.name} ${action} - - ${agent} ${params?.platform ?: '-'} - ${t.message} ${params}"
        log.debug message
    }

    static String debug(Class clazz, Map params, HttpServletRequest request) {
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def IP = request.getHeader("X-Real-IP")?:request.getRemoteAddr()
        IP = IP ?: '-'
        def action = params.action
        def agent = '"-"'
        def message = "${HOST_NAME} ${IP} ${clazz.name} ${action} - - ${agent} ${params?.platform ?: 'web'} - - ${params}"
        log.debug message
    }

    static String debug(Class clazz, Map params, HttpServletRequest request, Throwable t) {
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def IP = request.getHeader("X-Real-IP")?:request.getRemoteAddr()
        IP = IP ?: '-'
        def action = params.action
        def agent = '"-"'
        def message = "${HOST_NAME} ${IP} ${clazz.name} ${action} - - ${agent} ${params?.platform ?: 'web'} - ${t.message} ${params}"
        log.debug message
    }

    static String info(Class clazz, Map params, HttpServletRequest request, String type, String username, HttpSession session, String describe){
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def IP = request.getHeader("X-Real-IP")?:request.getRemoteAddr()
        IP = IP ?: '-'
        def action = params.action
        def agent = '"' + session.getAttribute('agent') + '"'
        def message = "${HOST_NAME} ${IP} ${clazz.name} ${action} ${type} ${username} ${agent} ${params?.platform ?: 'web'} ${describe} - ${params}"
        log.info message
    }

    static String error(Class clazz, Map params, HttpServletRequest request, String type, String username, HttpSession session, String describe, String exception) {
        if (!params) {
            throw new RuntimeException("params 不能为空")
        }
        def IP = request.getHeader("X-Real-IP")?:request.getRemoteAddr()
        IP = IP ?: '-'
        def action = params.action
        def agent = '"' + session.getAttribute('agent') + '"'
        def message = "${HOST_NAME} ${IP} ${clazz.name} ${action} ${type} ${username} ${agent} ${params?.platform ?: 'web'} ${describe} ${exception} ${params}"
        log.error message
    }

//    static String error(Class clazz, Map params, HttpServletRequest request, String type, String username, HttpSession session, String describe, Throwable t) {
//        if (!params) {
//            throw new RuntimeException("params 不能为空")
//        }
//        def IP = request.getHeader("X-Real-IP")?:request.getRemoteAddr()
//        def action = params.action
//        def agent = '"' + session.getAttribute('agent') + '"'
//        def message = "${HOST_NAME} ${IP} ${clazz.name} ${action} ${params} ${type} ${username} ${agent} ${params?.platform ?: 'web'} ${describe} ${t.message}"
//        log.error message
//    }

//    static String debug(Class clazz, String type, Map params, String platform) {
//        if (!params) {
//            throw new RuntimeException("params 不能为空")
//        }
//        def session = WebUtils.retrieveGrailsWebRequest().getSession()
//        def request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
//        def IP = request.getRequest().getHeader("X-Real-IP")?:request.getRequest().getRemoteAddr()
//        def action = params.action
//        def agent = '"' + session.getAttribute('agent') + '"'
//        def message =  "${HOST_NAME} ${IP} ${clazz.name} ${action} ${type} ${agent} ${platform} ${params}"
//        log.debug message
//    }
//
//
//    static String info(Class clazz, String type, Map params, String username, String platform, String describe) {
//        if (!params) {
//            throw new RuntimeException("params 不能为空")
//        }
//        def session = WebUtils.retrieveGrailsWebRequest().getSession()
//        def request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
//        def IP = request.getRequest().getHeader("X-Real-IP")?:request.getRequest().getRemoteAddr()
//        def action = params.action
//        def agent = '"' + session.getAttribute('agent') + '"'
//        def message =  "${HOST_NAME} ${IP} ${clazz.name} ${action} ${type} ${username ?: '-'} ${agent} ${platform} ${describe ?: '-'} ${params} "
//        log.info message
//    }
//
//    static String error(Class clazz, String type, Map params, String username, String platform, String describe, String exception) {
//        if (!params) {
//            throw new RuntimeException("params 不能为空")
//        }
//        def session = WebUtils.retrieveGrailsWebRequest().getSession()
//        def request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
//        def IP = request.getRequest().getHeader("X-Real-IP")?:request.getRequest().getRemoteAddr()
//        def action = params.action
//        def agent = '"' + session.getAttribute('agent') + '"'
//        def exceptionStr = '"' + exception + '"'
//        def message =  "${HOST_NAME} ${IP} ${clazz.name} ${action} ${type} ${username ?: '-'} ${agent} ${platform} ${exceptionStr ?: '-'} ${params} "
//        log.error message
//    }
}

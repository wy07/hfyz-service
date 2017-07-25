package com.commons.utils

import grails.util.GrailsStringUtils

/**
 * Created by zy on 17/6/15.
 */
class NumberUtils {
    static Integer toInteger(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).intValue()
        }

        if (obj != null) {
            try {
                String string = obj.toString()
                if (string != null) {
                    return Integer.parseInt(string)
                }
            } catch (NumberFormatException e) {
            }
        }
        return null
    }

    static Long toLong(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).longValue()
        }

        if (obj != null) {
            try {
                return Long.parseLong(obj.toString())

            } catch (NumberFormatException e) {
            }
        }
        return null
    }

    static Boolean getBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean) obj
        }

        if (obj != null) {
            try {
                String string = obj.toString()
                if (string != null) {
                    return GrailsStringUtils.toBoolean(string)
                }
            }
            catch (Exception e) {
            }
        }
        return null
    }

    //生成随机6位数
    static String genRandomCode(int code_len) {
        int maxNum = 9
        int i //生成的随机数
        int count = 0 //生成的密码的长度
        def str = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];

        StringBuffer code = new StringBuffer("")
        Random r = new Random() //随机数
        //生成6位随机数
        while (count < code_len) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.size()) {
                code.append(str[i])
                count++
            }
        }
        //返回此随机数
        code.toString()
    }

}

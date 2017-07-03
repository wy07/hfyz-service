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
}

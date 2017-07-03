package com.commons.utils

import org.apache.commons.lang.StringUtils

class URIUtils {
    String URIPartStr

    /**
     * 将中划线命名方式修改为驼峰式命名
     * @param separatorCaseStr
     * @return
     */
    URIUtils parseSeparatorToCamelCase() {
        this.URIPartStr = this.URIPartStr?.replaceAll(/-([a-z])/) { Object[] obj ->
            obj[0][1].toUpperCase()
        }
        return this
    }

    static String changeStyle(String str, boolean toCamel){
        if(!str || str.size() <= 1)
            return str

        if(toCamel){
            String r = str.toLowerCase().split('-').collect{cc -> StringUtils.capitalize(cc)}.join('')
            return r[0].toLowerCase() + r[1..-1]
        }else{
            str = str[0].toLowerCase() + str[1..-1]
            return str.collect{cc -> ((char)cc).isUpperCase() ? '-' + cc.toLowerCase() : cc}.join('')
        }
    }

    /**
     * 去掉单词最后的s
     * @param str
     * @return
     */
    URIUtils removeTheEndS() {
        if (!this.URIPartStr) {
            return this
        }

        if (this.URIPartStr[-1] == 's') {
            this.URIPartStr = this.URIPartStr[0..-2]
            return this
        } else {
            return this
        }
    }

    static String parseResourceToControllerName(String resource) {
        new URIUtils(URIPartStr: resource).parseSeparatorToCamelCase().removeTheEndS().toString()
    }

    @Override
    public String toString() {
        return URIPartStr
    }
}

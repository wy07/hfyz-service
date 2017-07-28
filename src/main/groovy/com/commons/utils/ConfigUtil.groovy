package com.commons.utils

import com.hfyz.support.Configure

class ConfigUtil {
    private static Map configItems = [:]

    def propertyMissing(String name) { configItems[name] }

    private static reLoadConfig() {
        Configure.list().each { config ->
            configItems[config.configKey] = config.configValue
        }
    }

    private static final ConfigUtil configUtil = new ConfigUtil()

    static getInstance() {
        if(!configItems.size()){
            reLoadConfig()
        }
        return configUtil
    }
}

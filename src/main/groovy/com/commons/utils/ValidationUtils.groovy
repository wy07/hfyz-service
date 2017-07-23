package com.commons.utils
class ValidationUtils {

    static boolean isStrongPassword(String password) {
        password = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$/
    }

}

package com.commons.utils

import java.security.SecureRandom
import sun.misc.BASE64Encoder
class ValidationUtils {

    static String getSecureRandomSalt() {
        byte[] bytes = new byte[24]
        new SecureRandom().nextBytes(bytes);
        new BASE64Encoder().encode(bytes);
    }

    static boolean isStrongPassword(String password) {

        password = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$/
    }

}

package com.commons.utils

import sun.misc.BASE64Encoder

import java.security.SecureRandom

class SaltUtils {

    static String getSecureRandomSalt() {
        byte[] bytes = new byte[24]
        new SecureRandom().nextBytes(bytes)
        new BASE64Encoder().encode(bytes)
    }

}

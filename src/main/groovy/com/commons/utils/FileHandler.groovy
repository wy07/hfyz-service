package com.commons.utils

import org.springframework.web.multipart.MultipartFile
import com.commons.exception.FileException

import java.security.MessageDigest

/**
文件保存路径：
    企业管理制度： $app/web-app/files/companyRegulation/$companyCode/$file
 */
class FileHandler {
    private static FILEMAXSIZE = 5242880

    static checkoutFile(MultipartFile file){
        if(file == null || file.empty) {
            throw new FileException('请先选择需要上传的文件！')
        } else if (file.size > FILEMAXSIZE) {
            throw new FileException('文件不能超过5M，请重新上传！')
        }
    }

    static findOrCreateDirectory(String directoryUrl) {
        def directory = new File(directoryUrl)
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdirs()
        }
        directory
    }

    private static String getMD5Code(InputStream inputStream) {
        String MD5Code = null
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5")
            byte[] dataBytes = new byte[1024]
            int nread = 0
            while ((nread = inputStream.read(dataBytes)) != -1) {
                md5.update(dataBytes, 0, nread)
            }
            BigInteger bi = new BigInteger(1, md5.digest())
            MD5Code = bi.toString(16).toUpperCase()
            if (MD5Code.size() < 32) {
                def prefix = ''
                for (int i = 0; i < (32 - MD5Code.size()); i++) {
                    prefix = '0' + prefix
                }
                MD5Code = prefix + MD5Code
            }
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close()
                } catch (IOException e) {
                    e.printStackTrace()
                }
            }
        }
        return MD5Code
    }
}

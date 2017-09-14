package com.commons.io

import org.springframework.web.multipart.MultipartFile

interface IFileManager {

    String saveFile(MultipartFile file, String category, String companyCode)

    boolean deleteFile(String fileRealPath)

    String getFileRealPath(MultipartFile file, String category, String companyCode)
}
package com.commons.io

import org.springframework.web.multipart.MultipartFile

interface IFileManager {

    String saveCompanyRegulationFile(MultipartFile file, String companyCode)

    boolean deleteFile(String fileRealPath)

    String getCompanyRegulationFileRealPath(MultipartFile file, String companyCode)
}
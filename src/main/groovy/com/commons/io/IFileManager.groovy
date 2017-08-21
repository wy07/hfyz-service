package com.commons.io

import org.springframework.web.multipart.MultipartFile

interface IFileManager {

    String saveCompanyRegulationFile(MultipartFile file, String companyCode)
}
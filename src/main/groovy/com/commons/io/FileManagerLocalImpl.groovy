package com.commons.io

import org.springframework.web.multipart.MultipartFile
import com.commons.utils.FileHandler

class FileManagerLocalImpl implements IFileManager{

    private def grailsApplication

    void setGrailsApplication(grailsApplication) {
        this.grailsApplication = grailsApplication
    }
    @Override
    String saveCompanyRegulationFile(MultipartFile file, String companyCode) {
        if(!file.empty) {
            FileHandler.checkoutFile(file)
            String directory = "${grailsApplication.config.getProperty("uploadFilePath")}${File.separator}companyRegulation${File.separator}${companyCode}"
            String fileName = "${FileHandler.getMD5Code(file.inputStream)}"
            FileHandler.findOrCreateDirectory(directory)

            String fileNamePath=System.getProperty("user.dir")+File.separator+directory+File.separator+fileName
            String fileExtension=file.originalFilename.substring(file.originalFilename.lastIndexOf('.')+1)
            String fileRealPath = "${fileNamePath}.${fileExtension}"
            File newFile = new File(fileRealPath)
            if (newFile.exists()) {
                def index = rename("${fileNamePath}", '', fileExtension, 1)
                newFile = new File("${fileNamePath}[${index}].${fileExtension}")
                fileRealPath = "${fileNamePath}[${index}].${fileExtension}"
            }
            file.transferTo(newFile)
            fileRealPath
        }
    }

    def rename={String name,String size,String extension,index->
        if(new File("${name}[${index}]${size}.${extension}").exists()){
            rename(name,size,extension,++index)
        }else{
            return index
        }
    }
}

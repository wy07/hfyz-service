package com.commons.io

import com.commons.exception.FileUploadException
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
            String fileRealPath = getCompanyRegulationFileRealPath(file, companyCode)
            File newFile = new File(fileRealPath)
            file.transferTo(newFile)
        }
    }

    boolean deleteFile(String fileRealPath) {
        try{
            if(new File(fileRealPath).exists()){
                FileHandler.deleteFile(new File(fileRealPath))
            }
            return true
        }catch (e){
            throw new FileUploadException("文件删除失败")
        }
    }

    String getCompanyRegulationFileRealPath(MultipartFile file, String companyCode) {
        if(!file.empty) {
            FileHandler.checkoutFile(file)
            String directory = "${grailsApplication.config.getProperty("uploadFilePath")}${File.separator}companyRegulation${File.separator}${companyCode}"
            String fileName = "${FileHandler.getMD5Code(file.inputStream)}"
            FileHandler.findOrCreateDirectory(directory)

            String fileNamePath = System.getProperty("user.dir") + File.separator + directory + File.separator + fileName
            String fileExtension = file.originalFilename.substring(file.originalFilename.lastIndexOf('.') + 1)
            String fileRealPath =  "${fileNamePath}.${fileExtension}"
            File newFile=new File("${fileNamePath}.${fileExtension}")
            if(newFile.exists()){
                def index=rename("${fileNamePath}",'',fileExtension,1)
                fileRealPath="${fileNamePath}[${index}].${fileExtension}"
            }
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

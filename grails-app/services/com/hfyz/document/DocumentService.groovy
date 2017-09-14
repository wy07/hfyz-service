package com.hfyz.document

import org.springframework.web.multipart.MultipartFile

class DocumentService {
    def fileManager
    def save(MultipartFile upload) {
        def originalFilename = upload.originalFilename
        String fileRealPath = fileManager.getFileRealPath(upload, 'document')

        Document document = new Document()

        document.fileName = originalFilename.substring(0, originalFilename.lastIndexOf('.'))
        document.fileType = originalFilename.substring(originalFilename.lastIndexOf('.') + 1, originalFilename.length())
        document.fileSize = (upload.getSize() / 1024).setScale(2, BigDecimal.ROUND_HALF_UP)
        document.fileRealPath = fileRealPath
        document.save(flush: true, failOnError: true)
        fileManager.saveFile(upload, 'document')
        document
    }

    def delete(Document document) {
        if(!document) {
            return
        }
        fileManager.deleteFile(document.fileRealPath)
        document.delete(flush: true)
    }
}

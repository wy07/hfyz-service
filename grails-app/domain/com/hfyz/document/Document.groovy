package com.hfyz.document

class Document {
    String fileRealPath
    String fileType
    String fileName
    Double fileSize

    static constraints = {
        fileRealPath nullable:false , blank:false
        fileType nullable: false, blank: false
        fileName nullable: false, blank:false
        fileSize nullable: false
    }

    static mapping = {
        comment '附件表'
        id generator:'native', params:[sequence:'document_seq'], defaultValue: "nextval('document_seq')"
        fileRealPath comment:'上传文件真实路径'
        fileType comment:'文件类型'
        fileName comment:'文件名称'
        fileSize comment:'文件大小'

    }
}

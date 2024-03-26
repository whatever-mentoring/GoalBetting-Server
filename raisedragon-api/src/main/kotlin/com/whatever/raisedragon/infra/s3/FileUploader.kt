package com.whatever.raisedragon.infra.s3

import org.springframework.web.multipart.MultipartFile

interface FileUploader {
    fun upload(multipartFile: MultipartFile): String
}
package com.whatever.raisedragon.infra.s3

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@Profile("test")
class FakeFileUploader: FileUploader {
    override fun upload(multipartFile: MultipartFile): String {
        return ""
    }
}
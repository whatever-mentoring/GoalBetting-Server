package com.whatever.raisedragon.infra.s3

import com.whatever.raisedragon.aws.s3.S3Agent
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class S3ApplicationService(
    private val s3Agent: S3Agent
) {

    fun upload(multipartFile: MultipartFile): String {
        return s3Agent.upload(S3_PREFIX_DIRECTORY, multipartFile)
    }

    companion object {
        private const val S3_PREFIX_DIRECTORY = "gifticon/"
    }
}
package com.whatever.raisedragon.aws.s3

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

@Component
class S3AgentImpl(
    private val amazonS3: AmazonS3,
    private val s3Properties: S3Config.S3Properties
) : S3Agent {
    override fun upload(directory: String, file: MultipartFile): String {
        val fileName = String.format("%s%s-%s", directory, UUID.randomUUID(), file.originalFilename)
        val bucket = s3Properties.bucketName
        runCatching {
            val objectMetadata = getObjectMetadataFromFile(file)
            val inputStream = file.inputStream
            amazonS3.putObject(bucket, fileName, inputStream, objectMetadata)
        }.onFailure { exception ->
            when (exception) {
                is IOException -> throw IllegalStateException("S3 File I/O Error", exception)
                is AmazonServiceException -> throw IllegalStateException(
                    "Failed to upload the file ($fileName)",
                    exception
                )
            }
        }
        return "${s3Properties.domain}/$fileName"
    }

    private fun getObjectMetadataFromFile(imageFile: MultipartFile): ObjectMetadata {
        return ObjectMetadata().apply {
            contentType = imageFile.contentType
            contentLength = imageFile.size
        }
    }
}
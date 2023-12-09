package com.whatever.raisedragon.s3

import org.springframework.web.multipart.MultipartFile

interface S3Agent {

    /**
     * @param multipartFile 사용자가 전송한 정적파일입니다.
     * 해당 파일을 S3에 업로드 한 이후, 해당 파일에 접근할 수 있는 PresignedURL을 반환해주세요.
     */
    fun upload(multipartFile: MultipartFile): PresignedURL
}
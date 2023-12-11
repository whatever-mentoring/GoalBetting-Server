package com.whatever.raisedragon.s3

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
interface S3Agent {

    /**
     * @param directory S3 버킷에 파일을 저장할 Directory 이름입니다.
     * @param multipartFile 사용자가 전송한 정적파일입니다.
     * @return 해당 파일을 S3에 업로드 한 이후, 해당 파일에 접근할 수 있는 url을 반환해합니다.
     */
    fun upload(directory: String, file: MultipartFile): String
}
package com.whatever.raisedragon.domain.auth.oauth.kakao

import com.whatever.raisedragon.domain.auth.AuthService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class KakaoOAuthService(
    private val restTemplate: RestTemplate,
) : AuthService {

    private val KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me"
    override fun verifyKaKao(userToken: String): String {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.setBearerAuth(userToken)

        val request = HttpEntity(
            null,
            headers
        )

        val response = restTemplate.postForEntity(
            KAKAO_USERINFO_REQUEST_URL,
            request,
            KakaoLoginResponse::class.java
        )

        return response.body?.id!!
    }
}
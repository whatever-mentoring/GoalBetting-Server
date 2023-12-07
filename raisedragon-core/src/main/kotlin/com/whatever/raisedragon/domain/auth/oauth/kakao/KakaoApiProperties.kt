package com.whatever.raisedragon.domain.auth.oauth.kakao

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class KakaoApiProperties(
    @Value("\${oauth.kakao.url.auth}")
    val authUrl: String,

    @Value("\${oauth.kakao.url.api}")
    val apiUrl: String,

    @Value("\${oauth.kakao.client-id}")
    val clientId: String,

    @Value("\${oauth.kakao.client-secret}")
    val clientSecret: String,

    @Value("\${oauth.kakao.url.redirection-uri}")
    val redirectUri: String,
) {

    companion object {
        val GRANT_TYPE: String = "authorization_code"
    }
}

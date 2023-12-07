package com.whatever.raisedragon.domain.auth

import com.whatever.raisedragon.domain.auth.oauth.kakao.KakaoApiProperties
import com.whatever.raisedragon.domain.auth.oauth.kakao.dto.KakaoAccessTokenResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.view.RedirectView

@Service
class KakaoOAuthService(
    private val kakaoApiProperties: KakaoApiProperties,
    private val restTemplate: RestTemplate,
) : AuthService {

    private val KAKAO_ACCESS_TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token"
    private val KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me"
    private val HEADER_AUTHORIZATION = "Authorization"
    private val BEARER_WITH_WHITESPACE = "Bearer "

    fun entryKakao(): RedirectView {
        return RedirectView("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${kakaoApiProperties.clientId}&redirect_uri=${kakaoApiProperties.redirectUri}")
    }

    override fun getAccessToken(code: String): String? {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val params = LinkedMultiValueMap<String, String>()
        params.add("grant_type", KakaoApiProperties.GRANT_TYPE)
        params.add("client_id", kakaoApiProperties.clientId)
        params.add("redirect_uri", kakaoApiProperties.redirectUri)
        params.add("code", code)

        val request = HttpEntity(params, headers)
        val response = restTemplate.postForEntity(
            KAKAO_ACCESS_TOKEN_REQUEST_URL,
            request,
            KakaoAccessTokenResponse::class.java
        )

        println("response = ${response}")

        return response.body!!.accessToken
    }
}
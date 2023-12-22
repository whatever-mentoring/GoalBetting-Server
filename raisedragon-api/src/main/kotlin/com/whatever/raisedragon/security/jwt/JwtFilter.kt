package com.whatever.raisedragon.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.security.authentication.JwtAuthentication
import com.whatever.raisedragon.security.jwt.JwtAgentImpl.Companion.BEARER_PREFIX
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    private val jwtAgent: JwtAgent,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(JwtFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        log.debug("JWT Filter doFilterInternal()")
        runCatching {
            setAuthenticationFromToken(request)
        }.onFailure { exception ->
            log.debug("Authentication Failed: Authorization value=${request.getAuthorization()}, Message=${exception.message}")
            SecurityContextHolder.clearContext()
            response.run {
                status = HttpStatus.UNAUTHORIZED.value()
                addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                val failResponse = Response.fail(ExceptionCode.E401_UNAUTHORIZED, exception.message)
                writer.write(objectMapper.writeValueAsString(failResponse))
            }
        }.onSuccess {
            filterChain.doFilter(request, response)
        }
    }

    private fun setAuthenticationFromToken(request: HttpServletRequest) {
        if (!isNotCheckEndpoint(request)) {
            val authorization =
                request.getAuthorization() ?: throw BadCredentialsException(ExceptionCode.E401_UNAUTHORIZED.message)
            log.debug("Parsing token in header: $authorization - Request path: ${request.requestURI}")
            getToken(authorization)?.let { token ->
                jwtAgent.extractUserFromToken(token)?.let { userInfo ->
                    SecurityContextHolder.getContext().authentication = JwtAuthentication(userInfo)
                }
            } ?: throw BaseException.of(ExceptionCode.E401_UNAUTHORIZED)
        }
    }

    private fun getToken(authorization: String): String? {
        val (provider, token) = authorization.split(AUTH_PROVIDER_SPLIT_DELIMITER)
        if (provider.uppercase() != BEARER_PREFIX.uppercase()) return null
        return token
    }

    private fun HttpServletRequest.getAuthorization(): String? = getHeader(HttpHeaders.AUTHORIZATION)

    private fun isNotCheckEndpoint(request: HttpServletRequest): Boolean {
        return NOT_CHECK_ENDPOINTS.any { request.requestURI.startsWith(it) }
    }

    companion object {
        private const val AUTH_PROVIDER_SPLIT_DELIMITER: String = " "
        private val NOT_CHECK_ENDPOINTS = listOf(
            // Common
            "/favicon.ico",
            "/error",
            // Swagger
            "/api-docs", "/swagger-ui", "/swagger-resources",
            // SignIn/SignUp Endpoints
            "/v1/auth",
            // Providing test api
            "/v1/test"
        )
    }
}
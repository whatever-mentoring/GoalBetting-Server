package com.whatever.raisedragon.common.aop

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.user.UserRepository
import com.whatever.raisedragon.domain.user.toDto
import com.whatever.raisedragon.security.jwt.JwtAgent
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Aspect
@Component
class AuthAspect(
    private val httpServletRequest: HttpServletRequest,
    private val userRepository: UserRepository,
    private val jwtAgent: JwtAgent
) {

    @Around("@annotation($BASE_PACKAGE)")
    fun userId(pjp: ProceedingJoinPoint): Any {
        val token = resolveToken(httpServletRequest) ?: throw BaseException.of(
            exceptionCode = ExceptionCode.E401_UNAUTHORIZED,
            "Request Header에 userId가 존재하지 않습니다."
        )

        val userId = jwtAgent.extractUserId(token).toString().toLong()
        val user = userRepository.findByIdOrNull(userId)?.toDto()
            ?: throw BaseException.of(
                exceptionCode = ExceptionCode.E404_NOT_FOUND,
                executionMessage = "존재하지 않는 유저입니다."
            )

        AuthContext.USER_CONTEXT.set(user)
        return pjp.proceed(pjp.args)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX_BEARER)) {
            return bearerToken.substring(7)
        }

        return null
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val PREFIX_BEARER = "Bearer "
        private const val BASE_PACKAGE = "com.whatever.raisedragon.common.aop.Auth"
    }
}
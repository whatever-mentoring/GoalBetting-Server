package com.whatever.raisedragon.security.resolver

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.security.authentication.JwtAuthentication
import com.whatever.raisedragon.security.authentication.UserInfo
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserInfoArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(GetAuth::class.java)
            && parameter.parameter.type == UserInfo::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        return when (val authentication = SecurityContextHolder.getContext().authentication) {
            null -> throw BaseException.of(ExceptionCode.E401_UNAUTHORIZED)
            is JwtAuthentication -> authentication.principal
            else -> throw BaseException.of(
                ExceptionCode.E401_UNAUTHORIZED,
                "The argument of GetAuth annotation is not of type UserInfo class."
            )
        }
    }
}
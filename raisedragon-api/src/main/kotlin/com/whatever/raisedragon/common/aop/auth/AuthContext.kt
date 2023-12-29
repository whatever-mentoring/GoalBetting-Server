package com.whatever.raisedragon.common.aop.auth

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.user.User

object AuthContext {

    val USER_CONTEXT: ThreadLocal<User> = ThreadLocal()

    fun getUser(): User {
        return USER_CONTEXT.get() ?: throw BaseException.of(
            exceptionCode = ExceptionCode.E401_UNAUTHORIZED,
            executionMessage = "인증 체크 중에 ThreadLocal 값을 꺼내오는 중에 문제가 발생했습니다."
        )
    }
}

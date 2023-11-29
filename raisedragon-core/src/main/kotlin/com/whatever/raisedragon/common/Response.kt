package com.whatever.raisedragon.common

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode


data class Response<T>(
    val status: Int,
    val data: T?,
    val exception: Exception?
) {
    companion object {
        fun of(exceptionCode: ExceptionCode, data: Any? = null, detailMessage: String? = null): Response<Any> {
            return Response(
                status = exceptionCode.httpStatus.value(),
                data = data,
                exception = Exception(
                    code = exceptionCode.exceptionCode,
                    detailMessage = detailMessage
                )
            )
        }
    }
}

data class Exception(
    val code: String,
    val detailMessage: String?
)

fun throwException(
    exception: ExceptionCode,
): Nothing {
    throw BaseException.from(
        exceptionCode = exception,
        executionMessage = exception.exceptionMessage
    )
}
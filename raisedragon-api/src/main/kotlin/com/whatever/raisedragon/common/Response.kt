package com.whatever.raisedragon.common

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

data class Response<T>(
    val isSuccess: Boolean,
    val data: T? = null,
    val errorResponse: ErrorResponse? = null
) {
    companion object {
        fun <T> success(data: T? = null): Response<T> {
            return Response(
                isSuccess = true,
                data = data
            )
        }

        fun success(): Response<Any> {
            return Response(isSuccess = true)
        }

        fun fail(exceptionCode: ExceptionCode, detailMessage: String? = null): Response<Any> {
            return Response(
                isSuccess = false,
                errorResponse = ErrorResponse(
                    code = exceptionCode.code,
                    detailMessage = detailMessage ?: exceptionCode.message
                )
            )
        }

        fun fail(exception: BaseException): Response<Unit> {
            return Response(
                isSuccess = false,
                errorResponse = ErrorResponse(
                    code = exception.exceptionCode.code,
                    detailMessage = exception.message
                )
            )
        }
    }
}

data class ErrorResponse(
    val code: String,
    val detailMessage: String?
)
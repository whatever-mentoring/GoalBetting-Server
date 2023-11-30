package com.whatever.raisedragon.common

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import org.springframework.http.HttpStatus

data class Response<T>(
    val status: Int,
    val data: T? = null,
    val errorResponse: ErrorResponse? = null
) {
    companion object {
        fun <T> success(data: T?): Response<T> {
            return Response(
                status = HttpStatus.OK.value(),
                data = data
            )
        }

        fun success(data: Any? = null): Response<Any> {
            return Response(
                status = HttpStatus.OK.value(),
                data = data
            )
        }

        fun fail(exceptionCode: ExceptionCode, detailMessage: String? = null): Response<Any> {
            return Response(
                status = exceptionCode.httpStatus.value(),
                errorResponse = ErrorResponse(
                    code = exceptionCode.code,
                    detailMessage = detailMessage ?: exceptionCode.message
                )
            )
        }

        fun fail(exception: BaseException): Response<Unit> {
            return Response(
                status = exception.exceptionCode.httpStatus.value(),
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
package com.whatever.raisedragon.common.exception

class BaseException(
    val exceptionCode: ExceptionCode,
    val exceptionMessage: String,
    override val cause: Throwable?,
) : RuntimeException(
    exceptionCode.toString(),
    cause
) {

    fun toExceptionResponse(): ExceptionResponse {
        return ExceptionResponse(
            status = exceptionCode.httpStatus.value(),
            exception = Exception(
                code = exceptionCode.exceptionCode,
                detailMessage = exceptionCode.exceptionMessage
            )
        )
    }

    companion object {
        fun from(
            exceptionCode: ExceptionCode,
            executionMessage: String,
            cause: Throwable? = null
        ): BaseException {
            return BaseException(
                exceptionCode = exceptionCode,
                exceptionMessage = executionMessage,
                cause = cause
            )
        }
    }
}
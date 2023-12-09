package com.whatever.raisedragon.common.exception

class BaseException(
    val exceptionCode: ExceptionCode,
    override val message: String,
    override val cause: Throwable?,
) : RuntimeException(
    message,
    cause
) {

    companion object {
        fun of(
            exceptionCode: ExceptionCode,
            executionMessage: String? = null,
            cause: Throwable? = null
        ): BaseException {
            return BaseException(
                exceptionCode = exceptionCode,
                message = executionMessage ?: exceptionCode.message,
                cause = cause
            )
        }
    }
}
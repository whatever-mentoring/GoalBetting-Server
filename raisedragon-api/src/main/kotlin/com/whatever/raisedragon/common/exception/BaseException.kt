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
            executionMessage: String,
            cause: Throwable? = null
        ): BaseException {
            return BaseException(
                exceptionCode = exceptionCode,
                message = executionMessage,
                cause = cause
            )
        }
    }
}
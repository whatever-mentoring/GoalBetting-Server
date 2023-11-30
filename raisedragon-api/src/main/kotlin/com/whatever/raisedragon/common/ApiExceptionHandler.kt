package com.whatever.raisedragon.common

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode.*
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import kotlin.Exception

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ConstraintViolationException::class)
    private fun handlerConstraintViolationException(
        exception: ConstraintViolationException,
    ): Response<Any> {
        return Response.of(
            exceptionCode = E400_BAD_REQUEST,
            detailMessage = DETAIL_MESSAGE_BY_PARAMETER_EXCEPTION
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    private fun handleMethodArgumentTypeMismatchException(
        exception: MethodArgumentTypeMismatchException,
    ): Response<Any> {
        return Response.of(
            exceptionCode = E400_BAD_REQUEST,
            detailMessage = DETAIL_MESSAGE_BY_PARAMETER_EXCEPTION
        )
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    private fun handleMissingServletRequestParameterException(
        exception: MissingServletRequestParameterException,
    ): Response<Any> {
        return Response.of(
            exceptionCode = E400_BAD_REQUEST,
            detailMessage = DETAIL_MESSAGE_BY_PARAMETER_EXCEPTION
        )
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    private fun httpRequestMethodNotSupportedException(
        exception: HttpRequestMethodNotSupportedException
    ): Response<Any> {
        return Response.of(
            exceptionCode = E405_METHOD_NOT_ALLOWED,
            detailMessage = DETAIL_MESSAGE_BY_PARAMETER_EXCEPTION
        )
    }

    @ExceptionHandler(BaseException::class)
    private fun handleBaseException(exception: BaseException): ResponseEntity<Response<Any>> {
        return ResponseEntity
            .status(exception.exceptionCode.httpStatus)
            .body(exception.toExceptionResponse())
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    private fun handleInternalServerException(exception: Exception): Response<Any> {
        return Response.of(E500_INTERNAL_SERVER_ERROR)
    }

    companion object {
        const val DETAIL_MESSAGE_BY_PARAMETER_EXCEPTION = "Please Check Your Request Parameter"
    }
}
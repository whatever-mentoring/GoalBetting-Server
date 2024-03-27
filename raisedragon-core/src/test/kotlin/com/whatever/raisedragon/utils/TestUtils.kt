package com.whatever.raisedragon.utils

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import org.assertj.core.api.AbstractThrowableAssert

fun AbstractThrowableAssert<*, *>.anyNotFoundException(): AbstractThrowableAssert<*, *> =
    isInstanceOf(BaseException::class.java)
        .hasMessage(ExceptionCode.E404_NOT_FOUND.message)

fun AbstractThrowableAssert<*, *>.anyInternalException(): AbstractThrowableAssert<*, *> =
    isInstanceOf(BaseException::class.java)
        .hasMessage(ExceptionCode.E500_INTERNAL_SERVER_ERROR.message)
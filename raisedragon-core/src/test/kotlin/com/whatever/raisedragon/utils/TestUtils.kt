package com.whatever.raisedragon.utils

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import org.assertj.core.api.AbstractThrowableAssert

fun AbstractThrowableAssert<*, *>.anyNotFoundException() = isInstanceOf(BaseException::class.java)
    .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
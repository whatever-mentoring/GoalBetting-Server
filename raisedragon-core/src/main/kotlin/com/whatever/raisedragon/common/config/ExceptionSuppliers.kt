package com.whatever.raisedragon.common.config

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Supplier

@Configuration
class ExceptionSuppliers {

    @Bean
    @Qualifier("notFoundExceptionSupplier")
    fun notFoundExceptionSupplier(): Supplier<BaseException> {
        return Supplier<BaseException> { BaseException.of(ExceptionCode.E404_NOT_FOUND) }
    }

    @Bean
    @Qualifier("InternalExceptionSupplier")
    fun internalExceptionSupplier(): Supplier<BaseException> {
        return Supplier<BaseException> { BaseException.of(ExceptionCode.E500_INTERNAL_SERVER_ERROR) }
    }
}
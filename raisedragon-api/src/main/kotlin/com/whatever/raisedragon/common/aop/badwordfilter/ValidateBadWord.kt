package com.whatever.raisedragon.common.aop.badwordfilter

import jakarta.validation.Constraint
import jakarta.validation.Payload
import org.springframework.core.annotation.Order
import kotlin.reflect.KClass

@Constraint(validatedBy = [BadWordValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Order(value = 1)
annotation class ValidateBadWord(
    val message: String = "비속어가 포함되어 있습니다.",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
)

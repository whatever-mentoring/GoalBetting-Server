package com.whatever.raisedragon.common.aop.auth

import org.springframework.core.annotation.Order

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Order(value = 2)
annotation class Auth

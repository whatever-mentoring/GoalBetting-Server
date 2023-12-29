package com.whatever.raisedragon.common.aop.badwordfilter

import com.whatever.raisedragon.common.BadWords.containsExplicit
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class BadWordFilterAspect {

    @Around("@annotation($BASE_PACKAGE)")
    fun filter(pjp: ProceedingJoinPoint): Any {
        val args = pjp.args
        for (arg in args) {
            val fields = arg.javaClass.declaredFields
            for (field in fields) {
                field.isAccessible = true
                if (field.isAnnotationPresent(BadWordFilter::class.java)) {
                    val value = field.get(arg)
                    if (value is String && containsExplicit(value)) {
                        throw IllegalArgumentException("비속어가 포함되어 있습니다.")
                    }
                }
            }
        }
        return pjp.proceed()
    }

    companion object {
        private const val BASE_PACKAGE = "com.whatever.raisedragon.common.aop.badwordfilter.BadWordFilter"
    }
}
package com.whatever.raisedragon.common.aop.badwordfilter

import com.whatever.raisedragon.common.BadWords.containsBadWords
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class BadWordValidator : ConstraintValidator<ValidateBadWord, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if (value == null) {
            return true
        }
        return !containsBadWords(value)
    }
}
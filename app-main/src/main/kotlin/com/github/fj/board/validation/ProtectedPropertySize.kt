/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.validation

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass
import javax.validation.constraints.Size

/**
 * Supports validation of ProtectedProperty&lt;T&gt;. Currently [String] generic type is supported.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 * @see Size
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ProtectedPropertySizeValidator::class])
@MustBeDocumented
annotation class ProtectedPropertySize(
    val message: String = "Not a valid email address",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],

    val min: Int = 0,
    val max: Int = Int.MAX_VALUE
)

private class ProtectedPropertySizeValidator : ConstraintValidator<ProtectedPropertySize, Any> {
    private var min = 0
    private var max = Int.MAX_VALUE

    override fun initialize(constraintAnnotation: ProtectedPropertySize?) = constraintAnnotation?.let {
        this.min = it.min
        this.max = it.max
    } ?: Unit

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true
        }

        if (value !is String) {
            throw UnsupportedOperationException(
                "ProtectedProperty with generic type ${value::class} validation is not supported."
            )
        }

        return value.length in (min + 1) until max
    }
}

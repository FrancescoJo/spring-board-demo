/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.validation

import com.github.fj.lib.util.ProtectedProperty
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.Size
import kotlin.reflect.KClass

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
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ProtectedPropertySizeValidator::class])
@MustBeDocumented
annotation class ProtectedPropertySize(
    val message: String = "Not a valid email address",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],

    val min: Long = 0,
    val max: Long = Long.MAX_VALUE
)

private class ProtectedPropertySizeValidator : ConstraintValidator<ProtectedPropertySize, ProtectedProperty<*>> {
    private var min = 0L
    private var max = Long.MAX_VALUE

    override fun initialize(constraintAnnotation: ProtectedPropertySize?) = constraintAnnotation?.let {
        this.min = it.min
        this.max = it.max
    } ?: Unit

    override fun isValid(value: ProtectedProperty<*>?, context: ConstraintValidatorContext?): Boolean {
        val enclosedValue = value?.value ?: return true

        return when (enclosedValue) {
            is String -> enclosedValue.length in min..max
            is Int    -> enclosedValue in min..max
            is Long   -> enclosedValue in min..max
            else      ->
                throw UnsupportedOperationException(
                    "ProtectedProperty with generic type ${value::class} validation is not supported."
                )
        }
    }
}

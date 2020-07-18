/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.validation

import com.github.fj.lib.text.unicodeGraphemeCount
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.Size
import kotlin.reflect.KClass

/**
 * Supports validation of [CharSequence]s that contain characters in Unicode extended plane. For example,
 * some emojis which represented as a single glyph but consisted of multiple unicode characters, is not validated
 * by [Size](i.e. 'Flag of wales': [U+1F3F4,󠁧 U+E0067, U+E0062, U+E0077, U+E006C, U+E0073, U+E007F]).
 * Use this validation if such input is expected.
 *
 * This validation working in time complexity of O(N). Therefore, you should consider carefully using this
 * on hot spots or very long [CharSequence]s.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
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
@Constraint(validatedBy = [UnicodeCharsLengthValidator::class])
@MustBeDocumented
annotation class UnicodeCharsLength(
    val message: String = "Not a valid email address",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],

    val min: Int = 0,
    val max: Int = Int.MAX_VALUE
)

private class UnicodeCharsLengthValidator : ConstraintValidator<UnicodeCharsLength, CharSequence> {
    private var min = 0
    private var max = Int.MAX_VALUE

    override fun initialize(constraintAnnotation: UnicodeCharsLength?) = constraintAnnotation?.let {
        this.min = it.min
        this.max = it.max
    } ?: Unit

    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext?): Boolean =
        if (value !is String) {
            true
        } else {
            value.unicodeGraphemeCount() in min..max
        }
}

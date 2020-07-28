/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity

import org.springframework.data.jpa.domain.AbstractPersistable
import java.io.Serializable
import javax.persistence.MappedSuperclass
import javax.persistence.Version

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 * @see AbstractPersistable
 */
@MappedSuperclass
abstract class AbstractIncrementalLockableEntity : Serializable {
    /**
     * Enables optimistic locking for underlying entity.
     * Of course, implementing table must have a column that could be mapped as this property.
     */
    @Version
    var version: Long = 0L
}

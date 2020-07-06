/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.post

import com.github.fj.board.persistence.converter.ByteArrayUuidConverter
import com.github.fj.lib.util.UuidExtensions
import java.io.Serializable
import java.util.*
import javax.persistence.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Jul - 2020
 */
@Entity
@Table(name = "attachments")
class Attachment : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Convert(converter = ByteArrayUuidConverter::class)
    @Column(name = "access_id", nullable = false, columnDefinition = "VARBINARY(16)")
    var accessId: UUID = UuidExtensions.EMPTY_UUID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    lateinit var post: Post

    @Column(columnDefinition = "CLOB")
    var name: String = ""

    @Column(columnDefinition = "CLOB")
    var uri: String = ""

    @Column(name = "mime_type", columnDefinition = "CLOB")
    var mimeType: String = ""

    override fun toString(): String {
        return "Attachment(id=$id, " +
                "accessId=$accessId, " +
                "board=${if (::post.isInitialized) {
                    post.id.toString()
                } else {
                    "<uninitialised>"
                }}, " +
                "name='$name', " +
                "uri='$uri', " +
                "mimeType='$mimeType')"
    }
}
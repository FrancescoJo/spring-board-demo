/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.board.endpoint.v1.post.request.AttachmentModeRequest
import com.github.fj.board.endpoint.v1.post.request.CreateAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.DeleteAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.UpdateAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.UpdatePostRequest
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.AttachmentNotFoundException
import com.github.fj.board.exception.client.post.CannotEditPostException
import com.github.fj.board.exception.client.post.PostNotFoundException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.post.Attachment
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.service.post.UpdatePostService
import com.github.fj.board.service.post.impl.UpdatePostServiceImpl
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.post.PostBriefInfo
import com.github.fj.lib.collection.iterationsOf
import com.nhaarman.mockitokotlin2.any
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import test.com.github.fj.board.endpoint.v1.post.dto.DeleteAttachmentRequestBuilder
import test.com.github.fj.board.endpoint.v1.post.dto.UpdateAttachmentRequestBuilder
import test.com.github.fj.board.endpoint.v1.post.dto.UpdateAttachmentRequestBuilder.createRandomBulk
import test.com.github.fj.board.endpoint.v1.post.dto.UpdatePostRequestBuilder
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.persistence.entity.post.AttachmentBuilder
import test.com.github.fj.board.persistence.entity.post.PostBuilder
import test.com.github.fj.board.persistence.entity.user.UserBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder
import java.util.*
import java.util.stream.Stream
import kotlin.reflect.KClass

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Jul - 2020
 */
class UpdatePostServiceTest : AbstractPostServiceTestTemplate() {
    private lateinit var sut: UpdatePostService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = UpdatePostServiceImpl(userRepo, boardRepo, postRepo, replyRepo, attachmentRepo)
    }

    @Test
    fun `fail is user is not authenticated`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = UpdatePostRequestBuilder.createRandom()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(null)

        // then:
        assertThrows<UserNotFoundException> {
            sut.update(UUID.randomUUID(), req, clientInfo)
        }
    }

    @Test
    fun `fail if target post is not found`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()
        val req = UpdatePostRequestBuilder.createRandom()

        // expect:
        assertThrows<PostNotFoundException> {
            sut.update(post.accessId, req, clientInfo)
        }
    }

    @Test
    fun `fail if target post is not owned`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()
        val otherUserPost = PostBuilder.createRandomOf(post.board, UserBuilder.createRandom())
        val req = UpdatePostRequestBuilder.createRandom()

        // when:
        `when`(postRepo.findByAccessId(otherUserPost.accessId)).thenReturn(otherUserPost)

        // then:
        assertThrows<CannotEditPostException> {
            sut.update(otherUserPost.accessId, req, clientInfo)
        }
    }

    @ParameterizedTest(name = "Cannot update post({1}) for \"{0}\" board")
    @MethodSource("testCannotUpdatePost")
    fun `fail if board has some constraints`(
        constraint: String,
        expectedException: KClass<out Exception>,
        board: Board
    ) {
        // given:
        val (clientInfo, user) = prepareSelf()
        val post = PostBuilder.createRandomOf(board, user)
        val req = UpdatePostRequestBuilder.createRandom()

        // when:
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)
        `when`(boardRepo.findByAccessId(post.board.accessId)).thenReturn(board)

        // then:
        Assertions.assertThrows(expectedException.java) {
            sut.update(post.accessId, req, clientInfo)
        }
    }

    @Test
    fun `fail if target attachments for deletion are not found`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()
        val board = post.board
        val req = UpdatePostRequestBuilder(UpdatePostRequestBuilder.createRandom())
            .attachments(createRandomBulk(AttachmentModeRequest.DELETE))
            .build()

        // when:
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)
        `when`(boardRepo.findByAccessId(board.accessId)).thenReturn(board)

        // then:
        assertThrows<AttachmentNotFoundException> {
            sut.update(post.accessId, req, clientInfo)
        }
    }

    @Test
    fun `fail if total count of attachments exceeds 10`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()
        val board = post.board
        val attachmentsSize = 9
        val iterations = attachmentsSize - post.attachments.size
        post.attachments.addAll(iterations.iterationsOf { AttachmentBuilder.createRandomOf(post) })
        val attachmentRequest =
            2.iterationsOf { UpdateAttachmentRequestBuilder.createRandom(AttachmentModeRequest.CREATE) }

        // and
        val req = UpdatePostRequestBuilder(UpdatePostRequestBuilder.createRandom())
            .attachments(attachmentRequest) // Expected attachments size after edit: 11
            .build()

        // when:
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)
        `when`(boardRepo.findByAccessId(board.accessId)).thenReturn(board)
        `when`(attachmentRepo.getCountOf(post)).thenReturn(attachmentsSize.toLong())

        // then:
        assertThrows<IllegalRequestException> {
            sut.update(post.accessId, req, clientInfo)
        }
    }

    @Test
    fun `post is updated if request is valid`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()

        // and:
        val req = UpdatePostRequestBuilder(UpdatePostRequestBuilder.createRandom())
            .attachments(emptyList())
            .build()

        // when:
        val (result, actual) = runSuccessfulUpdate(clientInfo, post, req)

        // then: "Ensure save is called only once to prove mocking save operation reflects actual result"
        verify(postRepo, times(1)).save(any())

        // expect:
        assertThat(result.accessId, `is`(post.accessId))
        assertThat(result.title, `is`(req.title))
        assertThat(result.mode, `is`(req.mode))
        assertThat(actual.contents, `is`(req.contents))
    }

    @Test
    fun `attachment is added if request contains additional attachment`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()
        post.attachments = ArrayList()

        // and:
        val addRequest = createRandomBulk(AttachmentModeRequest.CREATE)
        val creationRequest = addRequest.map { it.payload as CreateAttachmentRequest }

        val req = UpdatePostRequestBuilder(UpdatePostRequestBuilder.createRandom())
            .attachments(addRequest)
            .build()

        // when:
        `when`(attachmentRepo.findAllByAccessIds(anyList())).thenReturn(emptyList())

        // then:
        val (_, actual) = runSuccessfulUpdate(clientInfo, post, req)

        // expect: "Ensure save is called only once to prove mocking save operation reflects actual result"
        verify(postRepo, times(1)).save(any())

        // when:
        val expectedUris = creationRequest.map { it.uri }
        val actualUris = actual.attachments.map { it.uri }
        val expectedMimeTypes = creationRequest.map { it.mimeType }
        val actualMimeTypes = actual.attachments.map { it.mimeType }
        val expectedNames = creationRequest.map { it.name }
        val actualNames = actual.attachments.map { it.name }

        // then:
        assertThat(actualUris, `is`(expectedUris))
        assertThat(actualMimeTypes, `is`(expectedMimeTypes))
        assertThat(actualNames, `is`(expectedNames))
    }

    @Test
    fun `attachment is deleted if post has requested attachments for deletion`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()

        // and:
        val req = UpdatePostRequestBuilder(UpdatePostRequestBuilder.createRandom())
            .attachments(post.attachments.toDeleteRequests())
            .build()

        // when:
        `when`(attachmentRepo.findAllByAccessIds(post.attachments.map { it.accessId })).thenReturn(post.attachments)

        // then:
        val (_, actual) = runSuccessfulUpdate(clientInfo, post, req)

        // expect: "Ensure save is called only once to prove mocking save operation reflects actual result"
        verify(postRepo, times(1)).save(any())

        // and:
        assertTrue(actual.attachments.isEmpty())
    }

    private fun runSuccessfulUpdate(
        clientInfo: ClientAuthInfo,
        targetPost: Post,
        request: UpdatePostRequest
    ): Pair<PostBriefInfo, Post> {
        // given:
        val expected = targetPost.applyRequest(request)

        // when:
        `when`(postRepo.findByAccessId(targetPost.accessId)).thenReturn(targetPost)
        `when`(postRepo.save(any())).thenReturn(expected)

        // then:
        val actual = sut.update(targetPost.accessId, request, clientInfo)

        return actual to expected
    }

    private fun Post.applyRequest(req: UpdatePostRequest) = PostBuilder(this)
        .mode(req.mode)
        .title(req.title)
        .contents(req.contents)
        .attachments(attachments.applyRequest(this, req.attachments))
        .build()

    private fun List<Attachment>.applyRequest(parentPost: Post, req: List<UpdateAttachmentRequest>?): List<Attachment> {
        if (req.isNullOrEmpty()) {
            return this
        }

        val reqGroup = req.groupBy { it.mode }
        val (deleteReqs, createReqs) = with(sut as UpdatePostServiceImpl) {
            reqGroup.getMode(AttachmentModeRequest.DELETE) to reqGroup.getMode(AttachmentModeRequest.CREATE)
        }

        @Suppress("UNCHECKED_CAST")
        val deleteIds = (deleteReqs as List<DeleteAttachmentRequest>).map { UUID.fromString(it.accessId) }

        @Suppress("UNCHECKED_CAST")
        val additions = (createReqs as List<CreateAttachmentRequest>).map { it.toEntityOf(parentPost) }

        return ArrayList(this).apply {
            // O(N²) efficiency - not for production
            removeIf { deleteIds.contains(it.accessId) }
            addAll(additions)
        }
    }

    private fun List<Attachment>.toDeleteRequests(): List<UpdateAttachmentRequest> = this.map {
        UpdateAttachmentRequestBuilder()
            .mode(AttachmentModeRequest.DELETE)
            .payload(
                DeleteAttachmentRequestBuilder()
                    .accessId(it.accessId.toString())
                    .build()    // DeleteAttachmentRequest
            )
            .build()    // UpdateAttachmentRequest
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testCannotUpdatePost(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "Status:CLOSED", BoardNotFoundException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.CLOSED)
                        .mode(BoardMode.FREE_STYLE)
                        .build()
                ),
                Arguments.of(
                    "Mode:READ_ONLY", CannotEditPostException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.NORMAL)
                        .mode(BoardMode.READ_ONLY)
                        .build()
                ),
                Arguments.of(
                    "Mode:WRITE_ONCE", CannotEditPostException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.NORMAL)
                        .mode(BoardMode.WRITE_ONCE)
                        .build()
                )
            )
        }
    }
}

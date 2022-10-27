package edu.heitorquaglia.post.api.responses

import edu.heitorquaglia.post.domain.Post
import java.time.OffsetDateTime

data class PostDetailedResponse(
    val id: Long,
    val createdAt: OffsetDateTime,
    val title: String,
    val editor: EditorResponse,
    val content: String
) {
    companion object{
        fun of(post: Post): PostDetailedResponse {
            return PostDetailedResponse(
                post.id!!,
                post.createdAt!!,
                post.title!!,
                EditorResponse.anonymousEditor(post.editorId!!),
                post.content!!
            )
        }

        fun of(post: Post, editor: EditorResponse): PostDetailedResponse {
            return PostDetailedResponse(
                post.id!!,
                post.createdAt!!,
                post.title!!,
                editor,
                post.content!!
            )
        }
    }
}
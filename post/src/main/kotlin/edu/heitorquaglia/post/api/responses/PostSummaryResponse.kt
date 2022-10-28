package edu.heitorquaglia.post.api.responses

import edu.heitorquaglia.post.domain.Post

class PostSummaryResponse(val id: Long, val title: String) {
    companion object {
        fun of(post: Post): PostSummaryResponse {
            return PostSummaryResponse(post.id!!, post.title!!)
        }
    }
}
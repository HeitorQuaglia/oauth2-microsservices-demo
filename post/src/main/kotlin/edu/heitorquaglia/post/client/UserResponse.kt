package edu.heitorquaglia.post.client

data class UserResponse(
    var id: Long? = null,
    var email: String? = null,
    var name: String? = null,
    var type: UserType? = null,
)
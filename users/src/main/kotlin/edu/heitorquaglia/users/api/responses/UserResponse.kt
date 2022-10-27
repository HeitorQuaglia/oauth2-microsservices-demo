package edu.heitorquaglia.users.api.responses

import edu.heitorquaglia.users.domain.UserEntity

class UserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val type: UserEntity.Type,
) {
    companion object {
        fun of(user: UserEntity): UserResponse {
            return UserResponse(
                user.id!!,
                user.email!!,
                user.name!!,
                user.type!!
            )
        }
    }
}
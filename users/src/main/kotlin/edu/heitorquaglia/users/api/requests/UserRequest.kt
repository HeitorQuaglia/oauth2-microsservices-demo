package edu.heitorquaglia.users.api.requests

import edu.heitorquaglia.users.domain.UserEntity

data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
    val type: UserEntity.Type
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            id = null,
            name = this.name,
            email = this.email,
            password = this.password,
            type = this.type,
            createdAt = null
        )
    }
}
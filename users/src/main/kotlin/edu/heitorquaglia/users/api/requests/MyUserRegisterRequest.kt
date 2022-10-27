package edu.heitorquaglia.users.api.requests

import edu.heitorquaglia.users.domain.UserEntity

data class MyUserRegisterRequest(
    val name: String,
    val email: String,
    val password: String
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            id = null,
            name = this.name,
            email = this.email,
            password = this.password,
            type = UserEntity.Type.CLIENT,
            createdAt = null
        )
    }
}
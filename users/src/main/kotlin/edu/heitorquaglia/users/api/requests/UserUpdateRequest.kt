package edu.heitorquaglia.users.api.requests

import edu.heitorquaglia.users.domain.UserEntity

class UserUpdateRequest(
    val name: String,
    val email: String,
    val type: UserEntity.Type
) {
    fun update(currentUser: UserEntity) {
        currentUser.email = email
        currentUser.name = name
        currentUser.type = type
    }
}
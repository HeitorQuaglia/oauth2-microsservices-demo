package edu.heitorquaglia.users.api.requests

import edu.heitorquaglia.users.domain.UserEntity

class MyUserUpdateRequest(
    val name: String,
    val email: String
) {
    fun update(currentUser: UserEntity) {
        currentUser.email = email
        currentUser.name = name
    }
}
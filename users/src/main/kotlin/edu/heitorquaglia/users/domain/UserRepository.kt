package edu.heitorquaglia.users.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(username: String?): Optional<UserEntity>
}
package edu.heitorquaglia.users.domain.repository

import edu.heitorquaglia.users.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(username: String?): Optional<UserEntity>
}
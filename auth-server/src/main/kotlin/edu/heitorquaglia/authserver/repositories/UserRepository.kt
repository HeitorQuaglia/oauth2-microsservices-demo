package edu.heitorquaglia.authserver.repositories

import edu.heitorquaglia.authserver.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String?): Optional<UserEntity>
}
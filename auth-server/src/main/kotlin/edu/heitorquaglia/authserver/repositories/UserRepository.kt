package edu.heitorquaglia.authserver.repositories

import edu.heitorquaglia.authserver.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

abstract class UserRepository : JpaRepository<UserEntity, Long> {
    abstract fun findByEmail(email: String?): Optional<UserEntity>
}
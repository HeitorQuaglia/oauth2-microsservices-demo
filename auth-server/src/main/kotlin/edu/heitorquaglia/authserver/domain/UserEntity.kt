package edu.heitorquaglia.authserver.domain

import org.springframework.data.annotation.CreatedDate
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
data class UserEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,
    var name: String? = null,
    @Column(unique = true)
    var email: String? = null,
    var password: String? = null,
    @Enumerated(EnumType.STRING)
    var type: Type? = null,
    @CreatedDate
    var createdAt: OffsetDateTime? = null
) {
    enum class Type {
        ADMIN, CLIENT
    }
}
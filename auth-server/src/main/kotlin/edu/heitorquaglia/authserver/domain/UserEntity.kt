package edu.heitorquaglia.authserver.domain

import org.springframework.data.annotation.CreatedDate
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
data class UserEntity(
    @Id
    @GeneratedValue
    private var id: Long? = null,
    private var name: String? = null,
    @Column(unique = true)
    private var email: String? = null,
    private var password: String? = null,
    @Enumerated(EnumType.STRING)
    private var type: Type? = null,
    @CreatedDate
    private var createdAt: OffsetDateTime? = null
) {
    enum class Type {
        ADMIN, CLIENT
    }
}
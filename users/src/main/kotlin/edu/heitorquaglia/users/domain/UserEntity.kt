package edu.heitorquaglia.users.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.*


@Entity
@EntityListeners(AuditingEntityListener::class)
data class UserEntity(
    @Id
    @GeneratedValue
    var id: Long?,
    var name: String?,
    @Column(unique = true)
    var email: String?,
    var password: String?,
    @Enumerated(EnumType.STRING)
    var type: Type?,
    @CreatedDate
    var createdAt: OffsetDateTime?,
) {
    enum class Type {
        ADMIN, CLIENT
    }
}
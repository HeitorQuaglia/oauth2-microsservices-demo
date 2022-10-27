package edu.heitorquaglia.post.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Post(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @CreatedDate
    var createdAt: OffsetDateTime? = null,
    var editorId: Long? = null,
    var title: String? = null,
    var content: String? = null,
)
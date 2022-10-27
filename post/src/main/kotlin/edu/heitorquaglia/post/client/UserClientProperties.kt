package edu.heitorquaglia.post.client

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties("user-api")
@Component
data class UserClientProperties(
    val url: String? = null,
    val encodedCredentials: String? = null,
)
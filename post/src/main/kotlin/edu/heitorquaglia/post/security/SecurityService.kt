package edu.heitorquaglia.post.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
class SecurityService {
    val userId: Long
        get() = ((SecurityContextHolder
            .getContext()
            ?.authentication
            ?.principal as Jwt)
            .claims["user_id"] as String).toLongOrNull() ?: kotlin.run {
                throw Exception("Invalid Token")
        }
}
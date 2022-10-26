package edu.heitorquaglia.authserver.security

import edu.heitorquaglia.authserver.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class JPAUserDetailsService(UserRepository: UserRepository) {
    private final val userRepository: UserRepository = UserRepository

    final var simpleGrantedAuthority = SimpleGrantedAuthority("ROLE_" + user.getType().name());
}
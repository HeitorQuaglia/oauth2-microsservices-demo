package edu.heitorquaglia.authserver.security

import edu.heitorquaglia.authserver.domain.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class JPAUserDetailsService(UserRepository: UserRepository) {
    private final val userRepository: UserRepository = UserRepository

    @Throws(UsernameNotFoundException::class)
    fun loadUserByUsername(email: String?): UserDetails? {
        val user = userRepository.findByEmail(email)
            .orElseThrow {
                UsernameNotFoundException(
                    email
                )
            }
        val simpleGrantedAuthority = SimpleGrantedAuthority("ROLE_" + user.type?.name)
        return User(
            user.email,
            user.password,
            listOf(simpleGrantedAuthority)
        )
    }
}
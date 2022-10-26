package edu.heitorquaglia.users.security

import edu.heitorquaglia.users.domain.UserEntity
import edu.heitorquaglia.users.domain.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class JPAUserDetailsService(val userRepository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): User? {
        val user: UserEntity? = userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException(email) }
        val simpleGrantedAuthority = SimpleGrantedAuthority("ROLE_" + user?.type?.name)
        return if (user != null) {
            User(
                user.email,
                user.password,
                listOf(simpleGrantedAuthority)
            )
        } else null
    }
}
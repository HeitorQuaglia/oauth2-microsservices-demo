package edu.heitorquaglia.users.config

import edu.heitorquaglia.authserver.domain.UserEntity
import edu.heitorquaglia.authserver.repositories.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class FirstUserConfig(val userRepository: UserRepository, val passwordEncoder: PasswordEncoder) : ApplicationRunner {
    val logger: Logger = LoggerFactory.getLogger(FirstUserConfig::class.java)


    override fun run(args: ApplicationArguments?) {
        if (userRepository.count() != 0L) {
            return
        }
        logger.info("Nenhum usuário encontrado, cadastrando usuários padrão.")
        userRepository.save(
            UserEntity(
                0,
                "", //
                "admin@email.com",
                passwordEncoder.encode("password"),
                UserEntity.Type.ADMIN
            )
        )
        userRepository.save(
            UserEntity(
                1,
                "",
                "example@email.com",
                passwordEncoder.encode("password"),
                UserEntity.Type.CLIENT
            )
        )
    }
}
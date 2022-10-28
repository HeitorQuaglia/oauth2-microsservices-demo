package edu.heitorquaglia.authserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["edu.heitorquaglia.authserver.repositories"])
class AuthServer

fun main(args: Array<String>) {
    runApplication<AuthServer>(*args)
}

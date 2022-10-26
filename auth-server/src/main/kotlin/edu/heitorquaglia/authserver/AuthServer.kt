package edu.heitorquaglia.authserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthServer

fun main(args: Array<String>) {
    runApplication<AuthServer>(*args)
}

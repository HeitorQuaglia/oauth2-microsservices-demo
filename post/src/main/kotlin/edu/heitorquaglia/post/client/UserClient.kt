package edu.heitorquaglia.post.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*


@Component
class UserClient(val restTemplate: RestTemplate, val properties: UserClientProperties) {

    val logger: Logger = LoggerFactory.getLogger(UserClient::class.java)

    fun findById(id: Long?): Optional<UserResponse> {
        return try {
            val url = properties.url + "/users/{id}"
            val response = restTemplate.getForObject(
                url,
                UserResponse::class.java, id
            )
            Optional.ofNullable(response)
        } catch (e: Exception) {
            logger.error(String.format("Erro ao buscar usuário de id %s", id), e)
            Optional.empty()
        }
    }
}
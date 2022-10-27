package edu.heitorquaglia.post.config

import edu.heitorquaglia.post.client.UserClientProperties

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.client.RestTemplateRequestCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequest

import org.springframework.web.client.RestTemplate


@Configuration
class RestTemplateConfig {
    @Bean
    fun restTemplate(builder: RestTemplateBuilder, properties: UserClientProperties): RestTemplate {
        return builder.additionalRequestCustomizers(RestTemplateRequestCustomizer { request: ClientHttpRequest ->
            request.headers.setBasicAuth(properties.encodedCredentials)
        }).build()
    }
}
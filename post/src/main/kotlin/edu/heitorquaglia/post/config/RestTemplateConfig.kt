package edu.heitorquaglia.post.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest

import org.springframework.web.client.RestTemplate


@Configuration
class RestTemplateConfig {
    @Bean
    fun restTemplate(builder: RestTemplateBuilder, clientManager: AuthorizedClientServiceOAuth2AuthorizedClientManager): RestTemplate {
        return builder.additionalRequestCustomizers({ request ->
            val auth2AuthorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("posts-registration")
                .principal("PostService")
                .build()

            clientManager.authorize(auth2AuthorizeRequest)?.accessToken?.tokenValue?.run {
                request.headers.setBearerAuth(this)
            }
        }).build()
    }
}
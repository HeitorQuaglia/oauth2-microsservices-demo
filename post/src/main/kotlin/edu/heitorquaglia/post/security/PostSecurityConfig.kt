package edu.heitorquaglia.post.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.client.AuthorizationCodeOAuth2AuthorizedClientProvider
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import java.util.*


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class PostSecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain {
        http.authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .csrf()
            .disable()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter())

        return http.build()
    }

    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        return JwtAuthenticationConverter()
            .apply {
                setJwtGrantedAuthoritiesConverter { jwt ->
                    val userAuthorities = jwt.claims["authorities"] as List<String>? ?: emptyList()

                    userAuthorities.run {
                        val scopesConverter = JwtGrantedAuthoritiesConverter()

                        val scopeAuthorities = scopesConverter.convert(jwt)

                        scopeAuthorities?.apply {
                            addAll(userAuthorities.map { authority -> GrantedAuthority { authority as String } })
                        }
                    }
                }
            }
    }

    @Bean
    fun oAuth2AuthorizedClientService(
        clientRegistrationRepository: ClientRegistrationRepository
    ): OAuth2AuthorizedClientService {
        return InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository)
    }

    @Bean
    fun clientManager(
        clientRegistrationRepository: ClientRegistrationRepository,
        auth2AuthorizedClientService: OAuth2AuthorizedClientService
    ) : AuthorizedClientServiceOAuth2AuthorizedClientManager {
        val authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
            .builder()
            .clientCredentials()
            .build()

        val clientManager = AuthorizedClientServiceOAuth2AuthorizedClientManager(
            clientRegistrationRepository,
            auth2AuthorizedClientService
        )

        clientManager.setAuthorizedClientProvider(authorizedClientProvider)


        return clientManager
    }
}
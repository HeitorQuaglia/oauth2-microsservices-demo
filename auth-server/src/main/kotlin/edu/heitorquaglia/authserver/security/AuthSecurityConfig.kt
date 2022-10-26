package edu.heitorquaglia.authserver.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import org.springframework.security.web.SecurityFilterChain
import java.time.Duration

@EnableWebSecurity
@Configuration
class AuthSecurityConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun defaultFilter(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        return http.formLogin(Customizer.withDefaults()).build()
    }

    @Bean
    fun authFilter(http: HttpSecurity): SecurityFilterChain {
        //Add your custom filters here
        http.authorizeRequests().anyRequest().authenticated()
        return http.formLogin(Customizer.withDefaults()).build()
    }

    @Bean
    fun registeredClientRepository(passwordEncoder: PasswordEncoder): InMemoryRegisteredClientRepository {

        val userClient = RegisteredClient.withId("user-client")
            .clientId("user-client")
            .clientSecret(passwordEncoder.encode("user-secret")) //Use a strong password
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(1)).build())
            .scope("users:read")
            .scope("users:write")
            .clientSettings(
                ClientSettings
                    .builder()
                    .requireAuthorizationConsent(false) //This is for testing purposes only
                    .build()
            )
            .build()

        return InMemoryRegisteredClientRepository(
            listOf(userClient)
        )
    }

    @Bean
    fun providerSettings(): ProviderSettings {
        return ProviderSettings
            .builder()
            .issuer("http://localhost:8080") //This is for testing purposes only
            .build()
    }
}
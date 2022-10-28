package edu.heitorquaglia.authserver.security

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import edu.heitorquaglia.authserver.repositories.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.web.SecurityFilterChain
import java.security.KeyStore
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
    fun jwtEncodingTokenCustomizer(userRepository: UserRepository): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer { context ->  
            val authentication = context.getPrincipal<Authentication>()

            if (authentication.principal is User) {
                val user = authentication.principal as User

                val userEntity = userRepository.findByEmail(user.username!!).orElseThrow()

                val authorities = emptySet<String>()

                for (authority in user.authorities) {
                    authorities.plus(authority.toString())
                }

                context.claims.claim("user_id", userEntity.id.toString())
                context.claims.claim("user_fullname", userEntity.name)
                context.claims.claim("authorities", authorities)
            }
        }
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

        val client = RegisteredClient
            .withId("2")
            .clientId("client")
            .clientSecret(passwordEncoder.encode("password")) //Use a strong password
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:3000/authorized") //just simulate a redirect uri in a frontend
            .scope("myuser:read")
            .scope("myuser:write")
            .scope("posts:write")
            .tokenSettings(
                TokenSettings
                    .builder()
                    .accessTokenTimeToLive(Duration.ofHours(1))
                    .reuseRefreshTokens(false)
                    .build()
            )
            .clientSettings(
                ClientSettings
                    .builder()
                    .requireAuthorizationConsent(true)
                    .build()
            )
            .build()

        return InMemoryRegisteredClientRepository(
            listOf(userClient, client)
        )
    }

    @Bean
    fun providerSettings(authProperties: AuthProperties): ProviderSettings {
        return ProviderSettings
            .builder()
            .issuer(authProperties.providerUri)
            .build()
    }

    @Bean
    fun jwkSet(authProperties: AuthProperties): JWKSet {
        val path = authProperties.jksProperties.path
        val inputStream = ClassPathResource(path).inputStream

        val keyStore = KeyStore.getInstance("JKS")
        keyStore.load(inputStream, authProperties.jksProperties.storepass.toCharArray())

        val rsaKey = RSAKey.load(
            keyStore,
            authProperties.jksProperties.alias,
            authProperties.jksProperties.storepass.toCharArray()
        )

        return JWKSet(rsaKey)
    }

    @Bean
    fun jwkSource(jwkSet: JWKSet): JWKSource<SecurityContext> {
        return JWKSource { jwkSelector, _ ->
            jwkSelector.select(jwkSet)
        }
    }

    @Bean
    fun jwtEncoder(jwkSource: JWKSource<SecurityContext>): JwtEncoder {
        return NimbusJwtEncoder(jwkSource)
    }
}
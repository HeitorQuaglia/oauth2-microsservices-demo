package edu.heitorquaglia.users.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import java.util.*


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class UserSecurityConfig {
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
}
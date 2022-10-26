package edu.heitorquaglia.users.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class UserSecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(
        http: HttpSecurity,
        userDetailsService: UserDetailsService?
    ): SecurityFilterChain {
        return http.userDetailsService(userDetailsService)
            .csrf().disable()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .httpBasic(withDefaults())
            .build()
    }
}
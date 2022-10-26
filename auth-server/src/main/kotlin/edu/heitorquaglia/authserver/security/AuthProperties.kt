package edu.heitorquaglia.authserver.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Component
@Configuration
@ConfigurationProperties(prefix = "oauth.auth")
class AuthProperties {

    lateinit var providerUri: String
    lateinit var jksProperties: JksProperties

    class JksProperties {
        lateinit var keystore: String
        lateinit var storepass: String
        lateinit var alias: String
        lateinit var path: String
    }
}
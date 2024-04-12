package jp.inaba.identity.service.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import java.net.URI

@Configuration
@Profile("app-local")
class LocalConfiguration {
    @Bean
    fun cognitoClient(): CognitoIdentityProviderClient {
        return CognitoIdentityProviderClient.builder()
            .endpointOverride(URI.create("localhost:9229"))
            .build()
    }
}
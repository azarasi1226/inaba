package jp.inaba.service.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import java.net.URI

@Configuration
@Profile("local")
class LocalConfiguration {
    @Bean
    fun cognitoClient(
        @Value("\${aws.cognito.endpoint}")
        endpoint: String,
    ): CognitoIdentityProviderClient {
        return CognitoIdentityProviderClient.builder()
            .region(Region.AP_NORTHEAST_1)
            .credentialsProvider(
                AnonymousCredentialsProvider.create(),
            )
            .endpointOverride(URI.create(endpoint))
            .build()
    }
}

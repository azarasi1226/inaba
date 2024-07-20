package jp.inaba.datakey.service.jp.inaba.datakey.service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider
import software.amazon.awssdk.services.kms.KmsClient
import java.net.URI

@Configuration
@Profile("local")
class LocalConfig {
    @Bean
    fun kmsClient(): KmsClient {
        return KmsClient
            .builder()
            .credentialsProvider(AnonymousCredentialsProvider.create())
            .endpointOverride(URI.create("http://localhost:8080"))
            .build()
    }
}
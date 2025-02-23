package jp.inaba.apigateway.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
@Profile("local")
class LocalConfiguration {
    @Bean
    fun s3Client(
        @Value("\${minio.endpoint}")
        endpoint: String,
        @Value("\${minio.username}")
        userName: String,
        @Value("\${minio.password}")
        password: String
    ): S3Client {
        return S3Client.builder()
            .region(Region.AP_NORTHEAST_1)
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(userName, password)
                )
            )
            .forcePathStyle(true)
            .build()
    }
}
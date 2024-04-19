package jp.inaba.identity.service.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("app-test", "app-prod")
class AwsConfiguration {
}
package jp.inaba.service.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("local", "local-with-axoniq")
class LocalConfiguration {
}

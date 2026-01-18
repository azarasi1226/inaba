package jp.inaba.service.config

import org.jooq.conf.RenderNameCase
import org.jooq.impl.DefaultConfiguration
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// JooqConfigurationという名前のBeenがすでに登録されていらしく、競合するためCustomをつけている。
@Configuration
class CustomJooqConfiguration {
    @Bean
    fun jooqCustomizer(): DefaultConfigurationCustomizer {
        return DefaultConfigurationCustomizer { configuration: DefaultConfiguration ->
            configuration.settings()
                .withRenderNameCase(RenderNameCase.LOWER)
        }
    }
}
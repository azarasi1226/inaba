package jp.inaba.service2.config

import org.jooq.conf.RenderNameCase
import org.jooq.impl.DefaultConfiguration
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// JooqConfigurationという名前のBeenがすでに登録されていらしく、競合するためCustomをつけている。
@Configuration
class CustomJooqConfiguration {
    // mysqlはテーブル名が小文字で定義されている際、大文字でクエリが発行されるとエラーになるので、クエリを小文字に変換するカスタマイザを定義
    @Bean
    fun jooqCustomizer(): DefaultConfigurationCustomizer =
        DefaultConfigurationCustomizer { configuration: DefaultConfiguration ->
            configuration
                .settings()
                .withRenderNameCase(RenderNameCase.LOWER)
        }
}

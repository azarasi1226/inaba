package jp.inaba.service2.config

import org.jooq.conf.RenderNameCase
import org.jooq.impl.DefaultConfiguration
import org.springframework.boot.jooq.autoconfigure.DefaultConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class JooqConfig {
  @Bean
  fun jooqCustomizer(): DefaultConfigurationCustomizer =
    DefaultConfigurationCustomizer { configuration: DefaultConfiguration ->
      configuration
        .settings()
        // MySQLはテーブル名の大文字や小文字を区別するため、意図しないクエリの失敗を防ぐために、クエリのテーブル名やプロパティ名を小文字に変換する設定
        .withRenderNameCase(RenderNameCase.LOWER)
    }
}

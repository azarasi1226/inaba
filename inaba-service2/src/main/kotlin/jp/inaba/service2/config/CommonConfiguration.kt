package jp.inaba.service2.config

import jakarta.annotation.PostConstruct
import jp.inaba.core.domain.basket.BasketIdFactory
import jp.inaba.core.domain.basket.BasketIdFactoryImpl
import org.axonframework.common.jdbc.ConnectionProvider
import org.axonframework.conversion.Converter
import org.axonframework.extension.spring.jdbc.SpringDataSourceConnectionProvider
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.TokenStore
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.jdbc.GenericTokenTableFactory
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.jdbc.JdbcTokenStore
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.jdbc.JdbcTokenStoreConfiguration
import org.jooq.conf.RenderNameCase
import org.jooq.impl.DefaultConfiguration
import org.springframework.boot.jooq.autoconfigure.DefaultConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class CommonConfiguration() {
    @Bean
    fun basketIdFactory(): BasketIdFactory = BasketIdFactoryImpl()
}

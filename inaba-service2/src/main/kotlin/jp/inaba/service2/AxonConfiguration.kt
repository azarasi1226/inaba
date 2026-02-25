package jp.inaba.service2

import org.axonframework.common.jdbc.ConnectionProvider
import org.axonframework.conversion.Converter
import org.axonframework.extension.spring.jdbc.SpringDataSourceConnectionProvider
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.TokenStore
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.jdbc.GenericTokenTableFactory
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.jdbc.JdbcTokenStore
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.jdbc.JdbcTokenStoreConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class AxonConfiguration {
    /**
     * DataSource -> ConnectionProvider に変換（Axon が DB コネクションを取るための口）
     */
    @Bean
    fun axonConnectionProvider(dataSource: DataSource): ConnectionProvider = SpringDataSourceConnectionProvider(dataSource)
}

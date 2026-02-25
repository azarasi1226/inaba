package jp.inaba.service2.config

import jp.inaba.core.domain.basket.BasketIdFactory
import jp.inaba.core.domain.basket.BasketIdFactoryImpl
import org.axonframework.common.jdbc.ConnectionProvider
import org.axonframework.conversion.Converter
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.TokenStore
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.jdbc.GenericTokenTableFactory
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.jdbc.JdbcTokenStore
import org.axonframework.messaging.eventhandling.processing.streaming.token.store.jdbc.JdbcTokenStoreConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommonConfiguration {
    @Bean
    fun basketIdFactory(): BasketIdFactory = BasketIdFactoryImpl()

    // JPAは使いたくないのでJDBCトークンストアを使用
    @Bean
    fun tokenStore(
        connectionProvider: ConnectionProvider,
        converter: Converter,
    ): TokenStore {
        val tokenStore = JdbcTokenStore(connectionProvider, converter, JdbcTokenStoreConfiguration.DEFAULT)
        // TokenStoreテーブルを作成する内部では IF NOT EXITSでテーブルが作成されてるので、存在しなかった場合のみ作成される。
        tokenStore.createSchema(
            // 汎用的なDBに対応したTableFactory
            GenericTokenTableFactory.INSTANCE,
        )

        return tokenStore
    }
}

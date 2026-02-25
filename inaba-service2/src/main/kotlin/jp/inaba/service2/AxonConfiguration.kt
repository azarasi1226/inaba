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

//    /**
//     * Axon5 の Converter（デフォルトは JacksonConverter）
//     * 既に ObjectMapper を Spring が提供しているならそれを流用。
//     */
//    @Bean
//    fun axonConverter(objectMapper: ObjectMapper): Converter =
//        JacksonConverter(objectMapper)

    /**
     * TokenStore 設定。まずは DEFAULT でOK。
     * claimTimeout だけ変えたい例も載せてます。
     */
    @Bean
    fun jdbcTokenStoreConfiguration(): JdbcTokenStoreConfiguration = JdbcTokenStoreConfiguration.DEFAULT
    // .claimTimeout(Duration.ofSeconds(30)) // 必要なら
    // .nodeId("my-node-id")               // 必要なら
    // .schema(TokenSchema.builder()....)  // 必要なら

    /**
     * これで TokenStore として DI される
     */
    @Bean
    fun tokenStore(
        connectionProvider: ConnectionProvider,
        converter: Converter,
        config: JdbcTokenStoreConfiguration,
    ): TokenStore {
        val tokenStore = JdbcTokenStore(connectionProvider, converter, config)

        // TokenStoreテーブルを作成する
        tokenStore.createSchema(
            // 汎用的なDBに対応したTableFactory
            GenericTokenTableFactory.INSTANCE,
        )

        return tokenStore
    }
}

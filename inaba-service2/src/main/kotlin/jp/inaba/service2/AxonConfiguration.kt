package jp.inaba.service2

import org.axonframework.common.jdbc.ConnectionProvider
import org.axonframework.extension.spring.jdbc.SpringDataSourceConnectionProvider
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

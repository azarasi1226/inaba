package jp.inaba.service.config

import jp.inaba.core.domain.stock.StockIdFactory
import jp.inaba.core.domain.stock.StockIdFactoryImpl
import jp.inaba.core.domain.user.UserIdFactory
import jp.inaba.core.domain.user.UserIdFactoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommonConfiguration {
    @Bean
    fun userIdFactory(): UserIdFactory {
        return UserIdFactoryImpl()
    }

    @Bean
    fun stockIdFactory(): StockIdFactory {
        return StockIdFactoryImpl()
    }
}

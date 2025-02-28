package jp.inaba.service.config

import jp.inaba.core.domain.basket.BasketIdFactory
import jp.inaba.core.domain.basket.BasketIdFactoryImpl
import jp.inaba.core.domain.stock.StockIdFactory
import jp.inaba.core.domain.stock.StockIdFactoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommonConfiguration {
    @Bean
    fun basketIdFactory(): BasketIdFactory {
        return BasketIdFactoryImpl()
    }

    @Bean
    fun stockIdFactory(): StockIdFactory {
        return StockIdFactoryImpl()
    }
}

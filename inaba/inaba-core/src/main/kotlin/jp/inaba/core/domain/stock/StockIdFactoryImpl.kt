package jp.inaba.core.domain.stock

class StockIdFactoryImpl : StockIdFactory {
    override fun handle(): StockId {
        return StockId()
    }
}
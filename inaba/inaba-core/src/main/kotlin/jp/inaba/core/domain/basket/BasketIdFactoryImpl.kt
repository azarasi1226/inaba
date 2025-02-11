package jp.inaba.core.domain.basket

class BasketIdFactoryImpl: BasketIdFactory {
    override fun handle(): BasketId {
        return BasketId()
    }
}
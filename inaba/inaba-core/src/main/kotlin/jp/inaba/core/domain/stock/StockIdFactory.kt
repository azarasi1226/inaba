package jp.inaba.core.domain.stock

interface StockIdFactory {
    fun handle(): StockId
}
package jp.inaba.service.infrastructure.domain.stock

import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.stock.CanCreateStockVerifier
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaRepository
import org.springframework.stereotype.Service

@Service
class CanCreateStockVerifierImpl(
    private val repository: LookupProductJpaRepository,
) : CanCreateStockVerifier {
    override fun isProductNotFound(productId: ProductId): Boolean {
        // DB書き込みが追いついてない場合があるのでスリープして待機。
        // TODO:ぶっちゃけこのやり方よくないだろ...てかリードレプリカがあるようなDBでも同じような問題あるよな...どうすんだ？
        Thread.sleep(50)
        return repository.findById(productId.value).isEmpty
    }
}

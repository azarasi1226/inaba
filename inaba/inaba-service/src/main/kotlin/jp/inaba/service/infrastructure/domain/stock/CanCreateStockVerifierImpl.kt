package jp.inaba.service.infrastructure.domain.stock

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.CreateStockError
import jp.inaba.service.domain.stock.CanCreateStockVerifier
import jp.inaba.service.infrastructure.jpa.product.ProductJpaRepository
import org.springframework.stereotype.Service

@Service
class CanCreateStockVerifierImpl(
    private val productJpaRepository: ProductJpaRepository,
) : CanCreateStockVerifier {
    override fun checkProductExits(productId: ProductId): Result<Unit, CreateStockError> {
        return if(productJpaRepository.existsById(productId.value)) {
            Ok(Unit)
        } else {
            Err(CreateStockError.ProductNotExits)
        }
    }
}
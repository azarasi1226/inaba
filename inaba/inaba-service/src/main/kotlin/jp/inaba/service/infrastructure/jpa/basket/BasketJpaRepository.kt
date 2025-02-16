package jp.inaba.service.infrastructure.jpa.basket

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BasketJpaRepository : JpaRepository<BasketJpaEntity, BasketItemId> {
    fun deleteByBasketItemId_BasketId(basketId: String)

    fun deleteByBasketItemId_ProductId(productId: String)

    @Modifying
    @Transactional
    @Query("DELETE FROM BasketJpaEntity b WHERE b.basketItemId.basketId = :basketId AND b.basketItemId.productId = :productId")
    fun deleteByBasketIdAndProductId(
        @Param("basketId") basketId: String,
        @Param("productId") productId: String,
    )
}

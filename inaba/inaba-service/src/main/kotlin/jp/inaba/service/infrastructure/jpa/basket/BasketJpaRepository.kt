package jp.inaba.service.infrastructure.jpa.basket

import jakarta.transaction.Transactional
import jp.inaba.service.infrastructure.jpa.basket.BasketItemId
import jp.inaba.service.infrastructure.jpa.basket.BasketJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BasketJpaRepository : JpaRepository<BasketJpaEntity, BasketItemId> {
    @Modifying
    @Transactional
    @Query("DELETE FROM BasketJpaEntity b WHERE b.product.id = :productId")
    fun deleteByProductId(
        @Param("productId") productId: String,
    )

    @Modifying
    @Transactional
    @Query("DELETE FROM BasketJpaEntity b WHERE b.basketId = :basketId")
    fun deleteByBasketId(
        @Param("basketId") basketId: String,
    )

    @Modifying
    @Transactional
    @Query("DELETE FROM BasketJpaEntity b WHERE b.basketId = :basketId AND b.product.id = :productId")
    fun deleteByBasketIdAndProductId(
        @Param("basketId") basketId: String,
        @Param("productId") productId: String,
    )
}

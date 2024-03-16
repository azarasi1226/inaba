package jp.inaba.basket.service.infrastructure.jpa.basketitem

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BasketItemJpaRepository : JpaRepository<BasketItemJpaEntity, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM BasketItemJpaEntity b WHERE b.basket.basketId = :basketId")
    fun deleteByBasketId(@Param("basketId") basketId: String)

    @Modifying
    @Transactional
    @Query("DELETE FROM BasketItemJpaEntity b WHERE b.basket.basketId = :basketId AND b.item.itemId = :itemId")
    fun deleteByBasketIdAndItemId(@Param("basketId") basketId: String, @Param("itemId") itemId: String)
}
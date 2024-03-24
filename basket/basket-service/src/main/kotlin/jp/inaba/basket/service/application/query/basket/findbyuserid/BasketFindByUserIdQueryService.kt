package jp.inaba.basket.service.application.query.basket.findbyuserid

import jakarta.persistence.EntityManager
import jp.inaba.basket.api.domain.basket.BasketQueries
import jp.inaba.basket.service.application.query.basket.BasketNotFoundException
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaRepository
import jp.inaba.common.domain.shared.Page
import jp.inaba.common.domain.shared.Paging
import jp.inaba.common.domain.shared.PagingCondition
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class BasketFindByUserIdQueryService(
    private val entityManager: EntityManager,
    private val basketJpaRepository: BasketJpaRepository,
) {
    @QueryHandler
    fun handle(query: BasketQueries.FindByUserIdQuery): BasketQueries.FindByUserIdResult {
        val basketJpaEntity = basketJpaRepository.findByUserId(query.userId)
            .orElseThrow { BasketNotFoundException("UserId:${query.userId}のBasketは存在しません") }

        val nativeQuery = entityManager.createNativeQuery("""
            with target_basket as (
                SELECT
                    basket.id AS basket_id,
                    user_id
                FROM basket
                WHERE
                    user_id = :userId
            )
            
            SELECT
                tb.basket_id AS ${BasketQueryResult::basketId.name},
                tb.user_id AS ${BasketQueryResult::userId.name},
                i.id AS ${BasketQueryResult::itemId.name},
                i.name AS ${BasketQueryResult::itemName.name},
                i.price AS ${BasketQueryResult::itemPrice.name},
                i.image_url AS ${BasketQueryResult::itemPictureUrl.name},
                bi.item_quantity AS ${BasketQueryResult::itemQuantity.name},
                COUNT(*) OVER() AS ${BasketQueryResult::totalCount.name}
            FROM target_basket tb
            INNER JOIN basket_item bi
                ON tb.basket_id = bi.basket_id
            INNER JOIN product i
                ON bi.product_id = i.id
            LIMIT :offset, :pageSize
        """, BasketQueryResult::class.java)
            .setParameter("userId", query.userId)
            .setParameter("offset", query.pagingCondition.offset)
            .setParameter("pageSize", query.pagingCondition.pageSize)

        val results = try {
            nativeQuery.resultList as List<BasketQueryResult>
        }
        catch(e: Exception) {
           emptyList()
        }

        return convertToOutputData(basketJpaEntity.id, results, query.pagingCondition)
    }

    private fun convertToOutputData(basketId: String, results : List<BasketQueryResult>, pagingCondition: PagingCondition): BasketQueries.FindByUserIdResult {
        val totalCount = if(results.isNotEmpty()) {
            results.first().totalCount
        }
        else {
            0
        }

        return BasketQueries.FindByUserIdResult(
            basketId = basketId,
            page = Page(
                items = results.map {
                    BasketQueries.ItemDataModel(
                        itemId = it.itemId,
                        itemName = it.itemName,
                        itemPrice = it.itemPrice,
                        itemPictureUrl = it.itemPictureUrl,
                        itemQuantity = it.itemQuantity
                    )
                },
                paging = Paging(
                    totalCount = totalCount,
                    pageSize = pagingCondition.pageSize,
                    pageNumber= pagingCondition.pageNumber
                )
            )
        )
    }

    private data class BasketQueryResult(
        val basketId: String,
        val userId: String,
        val itemId: String,
        val itemName: String,
        val itemPrice: Int,
        val itemPictureUrl: String,
        val itemQuantity: Int,
        val totalCount: Long
    )
}
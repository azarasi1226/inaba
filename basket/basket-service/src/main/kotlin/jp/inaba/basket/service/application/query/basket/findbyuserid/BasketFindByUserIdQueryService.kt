package jp.inaba.basket.service.application.query.basket.findbyuserid

import jakarta.persistence.EntityManager
import jp.inaba.basket.api.domain.basket.BasketQueries
import jp.inaba.common.domain.shared.Page
import jp.inaba.common.domain.shared.Paging
import jp.inaba.common.domain.shared.PagingCondition
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class BasketFindByUserIdQueryService(
    private val entityManager: EntityManager,
) {
    @QueryHandler
    fun handle(query: BasketQueries.FindByUserIdQuery): BasketQueries.FindByUserIdResult {
        val nativeQuery = entityManager.createNativeQuery("""
            SELECT
                p.id AS ${BasketQueryResult::itemId.name},
                p.name AS ${BasketQueryResult::itemName.name},
                p.price AS ${BasketQueryResult::itemPrice.name},
                p.image_url AS ${BasketQueryResult::itemPictureUrl.name},
                bi.item_quantity AS ${BasketQueryResult::itemQuantity.name},
                COUNT(*) OVER() AS ${BasketQueryResult::totalCount.name}
            FROM basket_item bi
            INNER JOIN product p
                ON bi.basket_id = :basketId
                AND bi.product_id = p.id
            LIMIT :offset, :pageSize
        """, BasketQueryResult::class.java)
            .setParameter("basketId", query.basketId.value)
            .setParameter("offset", query.pagingCondition.offset)
            .setParameter("pageSize", query.pagingCondition.pageSize)

        @Suppress("UNCHECKED_CAST")
        return convertToOutputData(
            results = nativeQuery.resultList as List<BasketQueryResult>,
            pagingCondition = query.pagingCondition
        )
    }

    private fun convertToOutputData(results : List<BasketQueryResult>, pagingCondition: PagingCondition): BasketQueries.FindByUserIdResult {
        val totalCount = if(results.isNotEmpty()) {
            results.first().totalCount
        }
        else {
            0
        }

        return BasketQueries.FindByUserIdResult(
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
        val itemId: String,
        val itemName: String,
        val itemPrice: Int,
        val itemPictureUrl: String,
        val itemQuantity: Int,
        val totalCount: Long
    )
}
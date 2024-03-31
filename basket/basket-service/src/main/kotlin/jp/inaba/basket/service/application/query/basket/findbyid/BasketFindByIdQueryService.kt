package jp.inaba.basket.service.application.query.basket.findbyid

import jakarta.persistence.EntityManager
import jp.inaba.basket.api.domain.basket.BasketQueries
import jp.inaba.common.domain.shared.Page
import jp.inaba.common.domain.shared.Paging
import jp.inaba.common.domain.shared.PagingCondition
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class BasketFindByIdQueryService(
    private val entityManager: EntityManager,
) {
    @QueryHandler
    fun handle(query: BasketQueries.FindByIdQuery): BasketQueries.FindByIdResult {
        val nativeQuery = entityManager.createNativeQuery("""
            SELECT
                p.id AS ${BasketFindByIdSqlResult::itemId.name},
                p.name AS ${BasketFindByIdSqlResult::itemName.name},
                p.price AS ${BasketFindByIdSqlResult::itemPrice.name},
                p.image_url AS ${BasketFindByIdSqlResult::itemPictureUrl.name},
                bi.item_quantity AS ${BasketFindByIdSqlResult::itemQuantity.name},
                COUNT(*) OVER() AS ${BasketFindByIdSqlResult::totalCount.name}
            FROM basket_item bi
            INNER JOIN product p
                ON bi.basket_id = :basketId
                AND bi.product_id = p.id
            LIMIT :offset, :pageSize
        """, BasketFindByIdSqlResult::class.java)
            .setParameter("basketId", query.basketId.value)
            .setParameter("offset", query.pagingCondition.offset)
            .setParameter("pageSize", query.pagingCondition.pageSize)

        @Suppress("UNCHECKED_CAST")
        return convertToOutputData(
            results = nativeQuery.resultList as List<BasketFindByIdSqlResult>,
            pagingCondition = query.pagingCondition
        )
    }

    private fun convertToOutputData(results : List<BasketFindByIdSqlResult>, pagingCondition: PagingCondition): BasketQueries.FindByIdResult {
        val totalCount = if(results.isNotEmpty()) {
            results.first().totalCount
        }
        else {
            0
        }

        return BasketQueries.FindByIdResult(
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
}
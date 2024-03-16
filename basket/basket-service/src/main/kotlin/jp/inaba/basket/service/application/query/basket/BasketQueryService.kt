package jp.inaba.basket.service.application.query.basket

import jakarta.persistence.EntityManager
import jp.inaba.basket.api.domain.basket.BasketFindByUserIdQuery
import jp.inaba.basket.api.domain.basket.BasketFindByUserIdResult
import jp.inaba.basket.api.domain.basket.ItemDataModel
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaRepository
import jp.inaba.common.domain.shared.Page
import jp.inaba.common.domain.shared.Paging
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class BasketQueryService(
    private val entityManager: EntityManager,
    private val basketJpaRepository: BasketJpaRepository,
) {
    @QueryHandler
    fun handle(query: BasketFindByUserIdQuery): BasketFindByUserIdResult {
        if(!basketJpaRepository.existsByUserId(query.userId)) {
            throw Exception("UserId:${query.userId}のBasketは存在しません")
        }

        val query = entityManager.createNativeQuery("""
            with target_basket as (
                SELECT
                    basket.,
                    user_id
                FROM BasketEntity basket
                WHERE
                    user_id = :userId
            )
            
            SELECT 
                tb.basket_id AS ${BasketQueryResult::basketId.name},
                tb.user_id AS ${BasketQueryResult::userId.name},
                i.item_id AS ${BasketQueryResult::itemId.name},
                i.item_name AS ${BasketQueryResult::itemName.name},
                i.item_price AS ${BasketQueryResult::itemPrice.name},
                i.item_picture_url AS ${BasketQueryResult::itemPictureUrl.name},
                i.item_stock_quantity AS ${BasketQueryResult::itemStockQuantity.name},
                bi.item_quantity AS ${BasketQueryResult::itemQuantity.name}
            FROM target_basket tb
            LEFT OUTER JOIN basket_item bi
                ON tb.basket_id = bi.basket_id
            LEFT OUTER JOIN item i
                ON bi.item_id = i.item_id
        """, BasketQueryResult::class.java)
            .setParameter("userId", query.userId)

        try {
            query.resultList
        }
        catch(e: Exception) {
            println(e)
        }
        return convertToOutputData(query.resultList as List<BasketQueryResult>)
    }

    private fun convertToOutputData(results : List<BasketQueryResult>): BasketFindByUserIdResult {
        return BasketFindByUserIdResult(
            //TODO(resultsが存在しないとき死ぬ)
            basketId = results.first().basketId,
            userId = results.first().userId,
            page = Page<ItemDataModel>(
                items = results.map {
                    ItemDataModel(
                        itemId = it.itemId,
                        itemName = it.itemName,
                        itemPrice = it.itemPrice,
                        itemPictureUrl = it.itemPictureUrl,
                        itemStockQuantity = it.itemStockQuantity,
                        itemQuantity = it.itemQuantity
                    )
                },
                //TODO(pageing番号決定)
                paging = Paging(
                    totalCount = 250,
                    pageSize = 2,
                    pageNumber= 3
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
        val itemStockQuantity: Int,
        val itemQuantity: Int,
    )
}
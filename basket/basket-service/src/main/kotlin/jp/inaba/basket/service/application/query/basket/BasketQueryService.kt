package jp.inaba.basket.service.application.query.basket

import jakarta.persistence.EntityManager
import jp.inaba.basket.api.domain.basket.BasketQueries
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
    fun handle(query: BasketQueries.FindByUserIdQuery): BasketQueries.FindByUserIdResult {
        if(!basketJpaRepository.existsByUserId(query.userId)) {
            throw Exception("UserId:${query.userId}のBasketは存在しません")
        }

        val query = entityManager.createNativeQuery("""
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
                bi.item_quantity AS ${BasketQueryResult::itemQuantity.name}
            FROM target_basket tb
            LEFT OUTER JOIN basket_item bi
                ON tb.basket_id = bi.basket_id
            LEFT OUTER JOIN product i
                ON bi.product_id = i.id
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

    private fun convertToOutputData(results : List<BasketQueryResult>): BasketQueries.FindByUserIdResult {
        return BasketQueries.FindByUserIdResult(
            //TODO(resultsが存在しないとき死ぬ)
            basketId = results.first().basketId,
            page = Page<BasketQueries.ItemDataModel>(
                items = results.map {
                    BasketQueries.ItemDataModel(
                        itemId = it.itemId,
                        itemName = it.itemName,
                        itemPrice = it.itemPrice,
                        itemPictureUrl = it.itemPictureUrl,
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
        val itemQuantity: Int,
    )
}
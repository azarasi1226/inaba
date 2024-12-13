package jp.inaba.basket.service.presentation.basket.get

import jp.inaba.basket.api.domain.basket.query.FindBasketByIdResult
import jp.inaba.common.domain.shared.Page

data class GetBasketResponse(
    val page: Page<FindBasketByIdResult.BasketItem>,
)

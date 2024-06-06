package jp.inaba.basket.service.presentation.basket.get

import jp.inaba.basket.api.domain.basket.FindBasketByIdSummary
import jp.inaba.common.domain.shared.Page

data class GetBasketResponse(
    val page: Page<FindBasketByIdSummary>,
)

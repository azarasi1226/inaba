package jp.inaba.basket.service.application.command.basket.setbasketitem

import jp.inaba.basket.api.domain.basket.BasketId

class BasketNotFoundException(basketId: BasketId)
    : Exception("basketId:[${basketId}]を持つ買い物かごは見つかりませんでした。")
package jp.inaba.basket.service.application.query.basket

//TODO(値オブジェクト)
class BasketNotFoundException(userId: String)
    : Exception("userId:[${userId}]を持つ買い物かごが見つかりませんでした。")
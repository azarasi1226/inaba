package jp.inaba.core.domain.basket

import jp.inaba.core.domain.common.UseCaseError

enum class CreateBasketError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    BASKET_ALREADY_EXISTS("1", "同じIDで買い物かごが存在しています"),
    USER_NOT_FOUND("2", "ユーザーが存在しませんでした"),
    BASKET_ALREADY_LINKED_TO_USER("3", "別の買い物かごとすでにリンクされたユーザーです"),
}

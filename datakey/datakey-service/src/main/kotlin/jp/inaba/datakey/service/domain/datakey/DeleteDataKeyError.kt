package jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey

import jp.inaba.common.domain.shared.DomainError

enum class DeleteDataKeyError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    DATAKEY_NOT_FOUND("1", "データキーが存在しません"),
}

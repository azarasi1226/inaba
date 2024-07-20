package jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey

import jp.inaba.common.domain.shared.DomainError

enum class CreateDataKeyError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    DATAKEY_ALREADY_EXISTS("1", "指定のRelationIdですでにデータキーが存在します")
}
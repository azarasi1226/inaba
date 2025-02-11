package jp.inaba.core.domain.user

import jp.inaba.core.domain.common.UseCaseError

enum class GetUserMetadataError(
    override val errorCode: String,
    override val errorMessage: String,
): UseCaseError {
    USER_METADATA_NOT_FOUND("1", "ユーザーメタデータが存在しません")
}
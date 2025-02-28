package jp.inaba.core.domain.common

enum class CommonError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    AGGREGATE_DUPLICATED("c1", "集約IDがEventStore内で重複しました"),
    AGGREGATE_NOT_FOUND("c2", "対象の集約が存在しませんでした"),
    AGGREGATE_DELETED("c3", "すでに削除された集約です"),
    AGGREGATE_INCOMPATIBLE("c4", "別の種類の集約に対して操作仕様としています"),
}

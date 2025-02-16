package jp.inaba.core.domain.common

enum class CommonError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    AGGREGATE_NOT_FOUND("c1", "対象の集約が存在しませんでした"),
    AGGREGATE_DELETED("c2", "すでに削除された集約です"),
    AGGREGATE_INCOMPATIBLE("c3", "別の種類の集約に対して操作仕様としています"),
}

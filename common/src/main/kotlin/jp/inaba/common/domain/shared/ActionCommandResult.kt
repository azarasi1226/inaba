package jp.inaba.common.domain.shared

/**
 * エラーを起こす可能性があるCommandの戻り値
 */
class ActionCommandResult private constructor(
    // なぜジェネリクスで持たないのかというと、jacksonのバグ？かなんかでジェネリクスでEnumを持とうとしたら変換できなかったから...
    // マジで意味わからんぜBaby...
    val errorCode: String? = null,
) {
    companion object {
        fun ok(): ActionCommandResult {
            return ActionCommandResult()
        }

        fun error(errorCode: String): ActionCommandResult {
            return ActionCommandResult(errorCode)
        }
    }

    fun isOk(): Boolean {
        return errorCode == null
    }
}

package jp.inaba.common.presentation.shared

data class ErrorResponse(
    val errorMessage: String? = null,
    val errorCode: String? = null,
)

package jp.inaba.common.presentation.shared

import jp.inaba.common.domain.shared.DomainError

data class ErrorResponse(
    val errorCode: String,
    val errorMessage: String,
) {
    constructor(error: DomainError) : this(error.errorCode, error.errorMessage)
}

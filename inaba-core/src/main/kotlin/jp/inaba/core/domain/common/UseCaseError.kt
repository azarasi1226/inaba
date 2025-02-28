package jp.inaba.core.domain.common

interface UseCaseError {
    val errorCode: String
    val errorMessage: String
}

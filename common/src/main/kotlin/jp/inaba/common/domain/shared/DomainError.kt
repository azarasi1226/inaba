package jp.inaba.common.domain.shared

interface DomainError {
    val errorCode: String
    val errorMessage: String
}

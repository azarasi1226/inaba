package jp.inaba.core.domain.common

interface DomainError {
    val errorCode: String
    val errorMessage: String
}

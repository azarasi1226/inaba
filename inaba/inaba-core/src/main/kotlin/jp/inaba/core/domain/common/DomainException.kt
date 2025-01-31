package jp.inaba.core.domain.common

class DomainException(
    val errorMessage: String
) : Exception("errorMessage[$errorMessage]")
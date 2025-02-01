package jp.inaba.core.domain.common

class UseCaseException(
    val error: UseCaseError
) : Exception("code:[${error.errorCode}] message:[${error.errorMessage}]")
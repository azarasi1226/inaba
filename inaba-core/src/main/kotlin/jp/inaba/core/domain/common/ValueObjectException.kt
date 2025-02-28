package jp.inaba.core.domain.common

class ValueObjectException(
    val errorMessage: String,
) : Exception(errorMessage)

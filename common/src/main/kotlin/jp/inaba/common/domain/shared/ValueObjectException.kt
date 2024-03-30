package jp.inaba.common.domain.shared

class ValueObjectException(
    val errorMessage: String,
    val errorCode: String? = null,
) : Exception("errorMessage[${errorMessage}], errorCode[${errorCode}]")
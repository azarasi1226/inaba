package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ValueObjectException
import java.net.URI

data class ProductImageURL(
    val value: String?,
) {
    init {
        if (value != null) {

            try {
                URI(value).toURL()
            } catch (ex: Exception) {
                throw ValueObjectException("ProductImageURLはURLの形式である必要があります。value:[$value]")
            }
        }
    }
}

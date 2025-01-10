package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.DomainException
import java.net.URI
import java.net.URL

data class ProductImageURL(val value: String?) {
    init {
        if (value != null) {

            try {
                URI(value).toURL()
            } catch (ex: Exception) {
                throw DomainException("ProductImageURLはURLの形式である必要があります。value:[$value]")
            }
        }
    }
}

package jp.inaba.catalog.api.domain.product

import jp.inaba.common.domain.shared.DomainException
import java.net.URL

data class ProductImageURL(val value: String?) {
    init {
        if (value != null) {

            try {
                URL(value).toURI()
            } catch (ex: Exception) {
                throw DomainException("ProductImageURLはURLの形式である必要があります。value:[$value]")
            }
        }
    }
}

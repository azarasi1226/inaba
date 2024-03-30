package jp.inaba.catalog.api.domain.product

import jp.inaba.common.domain.shared.ValueObjectException
import java.net.URL

data class ProductImageURL(val value: String?) {
    init{
        if (value != null){

            try {
                URL(value).toURI();
            }
            catch (ex: Exception){
                throw ValueObjectException("ProductImageURLはURLの形式である必要があります。現在の値[${value}]")
            }
        }
    }
}
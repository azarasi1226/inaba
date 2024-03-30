package jp.inaba.catalog.api.domain.product

import com.thoughtworks.xstream.mapper.Mapper.Null
import java.net.MalformedURLException
import java.net.URL

data class ProductImageURL(val value: String?) {
    init{
        if (value != null){
            URL(value)
        }
        // Todo あとで考える
    }
}
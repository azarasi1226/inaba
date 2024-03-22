package jp.inaba.catalog.api.domain.product

import com.thoughtworks.xstream.mapper.Mapper.Null
import java.net.MalformedURLException
import java.net.URL

data class ProductImageURL(val value: String) {
    // ************ START CLASS DEFINITION ************
    private class IsUrlResult(val result: Boolean, ex :Exception?){
        var Result: Boolean = result
            private set

        var Ex: Exception? = ex
            private set
    }
    // ************ END CLASS DEFINITION ************
    companion object {
    }
    init{
        val isUrlResult: IsUrlResult = isURL(value)
        if (!isUrlResult.Result){
            throw Exception("URLが不正です。\r\n${value}")
        }
    }
    private fun isURL(value: String): IsUrlResult{
        lateinit var isUrlResult: IsUrlResult
        try{
            URL(value)
            isUrlResult = IsUrlResult(result=true, ex=null)
        }catch(ex: MalformedURLException){
            isUrlResult = IsUrlResult(result=false, ex=ex)
        }catch(ex: IllegalArgumentException){
            isUrlResult = IsUrlResult(result=false, ex=ex)
        }
        return isUrlResult
    }
}
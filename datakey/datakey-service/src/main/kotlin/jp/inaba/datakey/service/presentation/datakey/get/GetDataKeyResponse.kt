package jp.inaba.datakey.service.jp.inaba.datakey.service.presentation.datakey.get

import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.get.GetDataKeyOutput

data class GetDataKeyResponse(
    val base64DataKey: String,
) {
    companion object {
        fun create(output: GetDataKeyOutput): GetDataKeyResponse {
            return GetDataKeyResponse(base64DataKey = output.base64PlaneDataKey.value)
        }
    }
}
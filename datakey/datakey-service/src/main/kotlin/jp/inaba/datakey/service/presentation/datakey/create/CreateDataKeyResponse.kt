package jp.inaba.datakey.service.jp.inaba.datakey.service.presentation.datakey.create

import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.create.CreateDataKeyOutput

data class CreateDataKeyResponse(
    val base64DataKey: String,
) {
    companion object {
        fun create(output: CreateDataKeyOutput): CreateDataKeyResponse {
            return CreateDataKeyResponse(base64DataKey = output.base64PlaneDataKey.value)
        }
    }
}

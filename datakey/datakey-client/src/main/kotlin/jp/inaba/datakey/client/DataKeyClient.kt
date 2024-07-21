package jp.inaba.datakey.client

import java.util.Base64

class DataKeyClient() {
    fun getOrCreateDataKey(relationId: String): DataKey {
        // 存在していなかったら新規作成
        var base64DataKey = getDataKey()
        if (base64DataKey == null) {
            createDataKey(relationId)
            base64DataKey = getDataKey()
        }

        val binaryDataKey = Base64.getDecoder().decode(base64DataKey)
        return DataKey(binaryDataKey)
    }

    private fun createDataKey(relationId: String) {
        // TODO(GRPC)
    }

    private fun getDataKey(): String? {
        // TODO(GRPC)
        return null
    }
}

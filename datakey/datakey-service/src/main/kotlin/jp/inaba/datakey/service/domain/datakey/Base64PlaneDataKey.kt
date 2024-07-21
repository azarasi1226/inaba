package jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey

import java.util.Base64

class Base64PlaneDataKey private constructor(
    val value: String,
) {
    companion object {
        fun create(binaryDataKey: PlanDataKey): Base64PlaneDataKey {
            val encoder = Base64.getEncoder()
            val value = encoder.encodeToString(binaryDataKey.value)

            return Base64PlaneDataKey(value)
        }
    }
}

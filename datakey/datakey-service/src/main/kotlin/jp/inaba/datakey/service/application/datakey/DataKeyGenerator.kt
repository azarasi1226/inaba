package jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey

import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.EncryptedDataKey
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.PlanDataKey

interface DataKeyGenerator {
    fun handle(): DataKeyPair
}

data class DataKeyPair(
    val planDataKey: PlanDataKey,
    val encryptedDataKey: EncryptedDataKey,
)

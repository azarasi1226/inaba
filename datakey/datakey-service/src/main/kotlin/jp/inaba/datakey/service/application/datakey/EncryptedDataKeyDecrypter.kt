package jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey

import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.EncryptedDataKey
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.PlanDataKey

interface EncryptedDataKeyDecrypter {
    fun handle(dataKey: EncryptedDataKey): PlanDataKey
}
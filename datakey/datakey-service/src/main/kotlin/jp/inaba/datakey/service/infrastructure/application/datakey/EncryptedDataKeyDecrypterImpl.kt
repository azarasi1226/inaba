package jp.inaba.datakey.service.jp.inaba.datakey.service.infrastructure.application.datakey

import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.EncryptedDataKeyDecrypter
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.EncryptedDataKey
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.PlanDataKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kms.KmsClient
import software.amazon.awssdk.services.kms.model.DecryptRequest

@Service
class EncryptedDataKeyDecrypterImpl(
    @Value("\${aws.kms.master-key.id}")
    private val masterKeyId: String,
    private val kmsClient: KmsClient,
) : EncryptedDataKeyDecrypter {
    override fun handle(dataKey: EncryptedDataKey): PlanDataKey {
        val request =
            DecryptRequest.builder()
                .ciphertextBlob(SdkBytes.fromByteArray(dataKey.value))
                .keyId(masterKeyId)
                .build()

        val response = kmsClient.decrypt(request)

        return PlanDataKey(response.plaintext().asByteArray())
    }
}

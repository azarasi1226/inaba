package jp.inaba.datakey.service.jp.inaba.datakey.service.infrastructure.application.datakey

import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.DataKeyGenerator
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.DataKeyPair
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.EncryptedDataKey
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.PlanDataKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.kms.KmsClient
import software.amazon.awssdk.services.kms.model.DataKeySpec
import software.amazon.awssdk.services.kms.model.GenerateDataKeyRequest

@Service
class DataKeyGeneratorImpl(
    @Value("\${aws.kms.master-key.id}")
    private val masterKeyId: String,
    private val kmsClient: KmsClient,
) : DataKeyGenerator {
    override fun handle(): DataKeyPair {
        val request =
            GenerateDataKeyRequest.builder()
                .keyId(masterKeyId)
                .keySpec(DataKeySpec.AES_256)
                .build()

        val response = kmsClient.generateDataKey(request)

        return DataKeyPair(
            planDataKey = PlanDataKey(response.plaintext().asByteArray()),
            encryptedDataKey = EncryptedDataKey(response.ciphertextBlob().asByteArray()),
        )
    }
}

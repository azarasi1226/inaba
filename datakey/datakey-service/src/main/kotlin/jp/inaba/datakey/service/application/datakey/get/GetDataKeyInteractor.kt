package jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.get

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.EncryptedDataKeyDecrypter
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.Base64PlaneDataKey
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.EncryptedDataKey
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.GetDataKeyError
import jp.inaba.datakey.service.jp.inaba.datakey.service.infrastructure.jpa.datakey.DataKeyJpaRepository
import org.springframework.stereotype.Service

@Service
class GetDataKeyInteractor(
    private val encryptedDataKeyDecrypter: EncryptedDataKeyDecrypter,
    private val dataKeyJpaRepository: DataKeyJpaRepository,
) {
    fun handle(input: GetDataKeyInput): Result<GetDataKeyOutput, GetDataKeyError> {
        val maybeEntity = dataKeyJpaRepository.findById(input.relationId.value)

        if (maybeEntity.isEmpty) {
            return Err(GetDataKeyError.DATAKEY_NOT_FOUND)
        }

        val encryptedDataKey = EncryptedDataKey(maybeEntity.get().encryptedDataKey)
        val planDataKey = encryptedDataKeyDecrypter.handle(encryptedDataKey)

        val base64PlaneDataKey = Base64PlaneDataKey.create(planDataKey)
        return Ok(GetDataKeyOutput(base64PlaneDataKey))
    }
}

package jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.create

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.DataKeyGenerator
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.Base64PlaneDataKey
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.CanCreateDataKeyVerifier
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.CreateDataKeyError
import jp.inaba.datakey.service.jp.inaba.datakey.service.infrastructure.jpa.datakey.DataKeyJpaEntity
import jp.inaba.datakey.service.jp.inaba.datakey.service.infrastructure.jpa.datakey.DataKeyJpaRepository
import org.springframework.stereotype.Service

@Service
class CreateDataKeyInteractor(
    private val canCreateDataKeyVerifier: CanCreateDataKeyVerifier,
    private val dataKeyGenerator: DataKeyGenerator,
    private val dataKeyJpaRepository: DataKeyJpaRepository,
) {
    fun handle(input: CreateDataKeyInput): Result<CreateDataKeyOutput, CreateDataKeyError> {
        canCreateDataKeyVerifier.checkDataKeyNotExits(input.relationId)
            .onFailure { return Err(it) }

        val dataKey = dataKeyGenerator.handle()

        val entity =
            DataKeyJpaEntity(
                id = input.relationId.value,
                encryptedDataKey = dataKey.encryptedDataKey.value,
            )
        dataKeyJpaRepository.save(entity)

        val base64PlaneDataKey = Base64PlaneDataKey.create(dataKey.planDataKey)
        return Ok(CreateDataKeyOutput(base64PlaneDataKey))
    }
}

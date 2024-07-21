package jp.inaba.datakey.service.jp.inaba.datakey.service.infrastructure.domain.datakey

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.CanCreateDataKeyVerifier
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.CreateDataKeyError
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.RelationId
import jp.inaba.datakey.service.jp.inaba.datakey.service.infrastructure.jpa.datakey.DataKeyJpaRepository
import org.springframework.stereotype.Service

@Service
class CanCreateDataKeyVerifierImpl(
    private val dataKeyJpaRepository: DataKeyJpaRepository
) : CanCreateDataKeyVerifier {
    override fun checkDataKeyNotExits(relationId: RelationId): Result<Unit, CreateDataKeyError> {
        return if(dataKeyJpaRepository.existsById(relationId.value)){
            Err(CreateDataKeyError.DATAKEY_ALREADY_EXISTS)
        }
        else {
            Ok(Unit)
        }
    }
}
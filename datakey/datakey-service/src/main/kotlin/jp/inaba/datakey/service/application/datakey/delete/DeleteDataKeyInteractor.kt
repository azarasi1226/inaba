package jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.delete

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.DeleteDataKeyError
import jp.inaba.datakey.service.jp.inaba.datakey.service.infrastructure.jpa.datakey.DataKeyJpaRepository
import org.springframework.stereotype.Service

@Service
class DeleteDataKeyInteractor(
    private val dataKeyJpaRepository: DataKeyJpaRepository,
) {
    fun handle(input: DeleteDataKeyInput): Result<Unit, DeleteDataKeyError> {
        val maybeEntity = dataKeyJpaRepository.findById(input.relationId.value)

        if (maybeEntity.isEmpty) {
            return Err(DeleteDataKeyError.DATAKEY_NOT_FOUND)
        }

        dataKeyJpaRepository.delete(maybeEntity.get())

        return Ok(Unit)
    }
}

package jp.inaba.datakey.service.jp.inaba.datakey.service.presentation.datakey.create

import com.github.michaelbull.result.mapBoth
import jp.inaba.common.presentation.shared.ErrorResponse
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.create.CreateDataKeyInput
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.create.CreateDataKeyInteractor
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.CreateDataKeyError
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.RelationId
import jp.inaba.datakey.service.jp.inaba.datakey.service.presentation.datakey.DataKeyController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateDataKeyController(
    private val createDataKeyInteractor: CreateDataKeyInteractor,
) : DataKeyController {
    @PostMapping
    fun handle(
        @RequestBody
        request: CreateDataKeyRequest,
    ): ResponseEntity<Any> {
        val relationId = RelationId(request.relationId)
        val input = CreateDataKeyInput(relationId)

        val result = createDataKeyInteractor.handle(input)

        return result.mapBoth(
            success = {
                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(CreateDataKeyResponse.create(it))
            },
            failure = {
                when (it) {
                    CreateDataKeyError.DATAKEY_ALREADY_EXISTS ->
                        ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(ErrorResponse(it))
                }
            },
        )
    }
}

package jp.inaba.datakey.service.jp.inaba.datakey.service.presentation.datakey.get

import com.github.michaelbull.result.mapBoth
import jp.inaba.common.presentation.shared.ErrorResponse
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.get.GetDataKeyInput
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.get.GetDataKeyInteractor
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.GetDataKeyError
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.RelationId
import jp.inaba.datakey.service.jp.inaba.datakey.service.presentation.datakey.DataKeyController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetDataKeyController(
    private val getDataKeyInteractor: GetDataKeyInteractor,
) : DataKeyController {
    @GetMapping("/{relationId}")
    fun handle(
        @PathVariable("relationId")
        rawRelationId: String,
    ): ResponseEntity<Any> {
        val relationId = RelationId(rawRelationId)
        val input = GetDataKeyInput(relationId)

        val result = getDataKeyInteractor.handle(input)

        return result.mapBoth(
            success = {
                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(result.value)
            },
            failure = {
                when (it) {
                    GetDataKeyError.DATAKEY_NOT_FOUND ->
                        ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(ErrorResponse(it))
                }
            },
        )
    }
}
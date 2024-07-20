package jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey

import com.github.michaelbull.result.Result

interface CanCreateDataKeyVerifier {
    fun checkDataKeyExits(relationId: RelationId) : Result<Unit, CreateDataKeyError>
}
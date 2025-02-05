package jp.inaba.service.infrastructure.application.external.auth

import jp.inaba.service.application.external.auth.getauthuser.CognitoGetAuthUserService
import org.springframework.stereotype.Service

@Service
class CognitoGetAuthUserServiceImpl(

) : CognitoGetAuthUserService {
    override fun handle(emailAddress: String): String {
        TODO("Not yet implemented")
    }
}
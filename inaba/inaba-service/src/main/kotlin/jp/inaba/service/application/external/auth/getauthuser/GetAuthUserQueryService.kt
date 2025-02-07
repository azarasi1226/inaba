package jp.inaba.service.application.external.auth.getauthuser

import jp.inaba.message.auth.query.GetAuthUserQuery
import jp.inaba.message.auth.query.GetAuthUserResult
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class GetAuthUserQueryService(
    private val cognitoGetAuthUserService: CognitoGetAuthUserService,
) {
    @QueryHandler
    fun handle(query: GetAuthUserQuery): GetAuthUserResult {
        val subject = cognitoGetAuthUserService.handle(query.emailAddress)

        return GetAuthUserResult(subject)
    }
}

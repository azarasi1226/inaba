package jp.inaba.service.application.command.user

import jp.inaba.core.domain.common.CommonError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.user.CreateUserError
import jp.inaba.message.user.command.CreateUserCommand
import jp.inaba.service.domain.UniqueAggregateIdVerifier
import jp.inaba.service.domain.user.CreateUserVerifier
import jp.inaba.service.domain.user.InternalCreateUserCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreateUserInteractor(
    private val uniqueAggregateIdVerifier: UniqueAggregateIdVerifier,
    private val createUserVerifier: CreateUserVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: CreateUserCommand) {
        if (uniqueAggregateIdVerifier.hasDuplicateAggregateId(command.id.value)) {
            throw UseCaseException(CommonError.AGGREGATE_DUPLICATED)
        }
        if (createUserVerifier.isLinkedSubject(command.subject)) {
            throw UseCaseException(CreateUserError.USER_ALREADY_LINKED_TO_SUBJECT)
        }

        // Subject(認証ユーザーに紐づいている一意の値)が存在するか、IDProviderに問い合わせ確認もしたほうがいいような気もしたんやけど...
        // * AccessTokenだとCognitoのLambdaハンドラーから問い合わせできない。
        // * 通常AccessTokenで可能なUseInfoエンドポイントへのSubjectでのユーザー問い合わせはCognitoでは対応していない。
        // 上記２点を踏まえてやめている。万が一存在しないSubjectが渡されたとしても、ゴミデータが作られるだけで、システムの整合性的には問題ない認識。

        val internalCommand =
            InternalCreateUserCommand(
                id = command.id,
                subject = command.subject,
            )

        commandGateway.sendAndWait<Any>(internalCommand)
    }
}

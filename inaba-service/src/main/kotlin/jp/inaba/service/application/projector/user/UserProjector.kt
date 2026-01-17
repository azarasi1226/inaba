package jp.inaba.service.application.projector.user

import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.references.USER
import jp.inaba.service.utlis.toTokyoLocalDateTime
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.Timestamp
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@ProcessingGroup(UserProjectorEventProcessor.PROCESSOR_NAME)
class UserProjector(
    private val dsl: DSLContext,
) {
    @ResetHandler
    fun reset() {
        dsl.deleteFrom(USER).execute()
    }

    @EventHandler
    fun on(
        event: UserCreatedEvent,
        @Timestamp timestamp: Instant,
    ) {
        dsl
            .insertInto(
                USER,
                USER.ID,
                USER.USER_NAME,
                USER.CREATED_AT,
                USER.UPDATED_AT,
            ).values(
                event.id,
                // TODO (仮のユーザー名になってるというかユーザ名って扱いどうするの？初期にメールアドレスとかも送ってもらう？)
                // 一般的なサービスだと、初回のemailアドレスの@から前をユーザ名にすることが多い気がする
                // そんで住所とかはログインが完了してから入力してもらうみたいな。
                "ユーザーネームどうしようか",
                timestamp.toTokyoLocalDateTime(),
                timestamp.toTokyoLocalDateTime(),
            )
            // 冪等性の考慮
            .onDuplicateKeyIgnore()
            .execute()
    }
}

package jp.inaba.service2.features.user.create

import jp.inaba.service.infrastructure.jooq.generated.tables.references.LOOKUP_USERS
import org.jooq.DSLContext

class SubjectLinkedChecker(
    private val dsl: DSLContext,
) {
    fun handle(subject: String): Boolean =
        dsl.fetchExists(
            dsl.selectOne().from(LOOKUP_USERS).where(LOOKUP_USERS.SUBJECT.eq(subject)),
        )
}

package jp.inaba.service.infrastructure.domain.user

import jp.inaba.service.domain.user.CreateUserVerifier
import jp.inaba.service.infrastructure.jooq.generated.tables.references.LOOKUP_USERS
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
class CreateUserVerifierImpl(
    private val dsl: DSLContext,
) : CreateUserVerifier {
    override fun isLinkedSubject(subject: String): Boolean =
        dsl.fetchExists(
            dsl.selectOne().from(LOOKUP_USERS).where(LOOKUP_USERS.SUBJECT.eq(subject)),
        )
}

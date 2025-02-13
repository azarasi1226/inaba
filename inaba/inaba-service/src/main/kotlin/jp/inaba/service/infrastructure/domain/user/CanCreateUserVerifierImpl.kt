package jp.inaba.service.infrastructure.domain.user

import jp.inaba.core.domain.user.UserId
import jp.inaba.service.domain.user.CreateUserVerifier
import jp.inaba.service.infrastructure.jpa.lookupuser.LookupUserJpaRepository
import org.springframework.stereotype.Service

@Service
class CreateUserVerifierImpl(
    private val repository: LookupUserJpaRepository,
) : CreateUserVerifier {
    override fun isUserExists(userId: UserId): Boolean {
        return repository.existsById(userId.value)
    }

    override fun isLinkedSubject(subject: String): Boolean {
        return repository.existsBySubject(subject)
    }
}

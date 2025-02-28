package jp.inaba.service.domain.user

interface CreateUserVerifier {
    fun isLinkedSubject(subject: String): Boolean
}

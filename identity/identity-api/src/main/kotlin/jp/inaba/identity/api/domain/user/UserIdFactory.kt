package jp.inaba.identity.api.domain.user

interface UserIdFactory {
    fun handle(): UserId
}

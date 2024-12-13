package jp.inaba.identity.share.domain.user

interface UserIdFactory {
    fun handle(): UserId
}

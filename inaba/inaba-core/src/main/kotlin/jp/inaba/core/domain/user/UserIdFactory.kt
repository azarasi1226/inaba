package jp.inaba.core.domain.user

interface UserIdFactory {
    fun handle(): UserId
}

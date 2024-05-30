package jp.inaba.identity.api.domain.user

class UserIdFactoryImpl : UserIdFactory {
    override fun handle(): UserId {
        return UserId()
    }
}

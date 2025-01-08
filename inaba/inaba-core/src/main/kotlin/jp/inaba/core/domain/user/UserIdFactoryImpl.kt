package jp.inaba.core.domain.user

class UserIdFactoryImpl : UserIdFactory {
    override fun handle(): UserId {
        return UserId()
    }
}

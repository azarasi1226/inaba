package jp.inaba.identity.share.domain.user

class UserIdFactoryImpl : UserIdFactory {
    override fun handle(): UserId {
        return UserId()
    }
}

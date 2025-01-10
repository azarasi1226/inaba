package jp.inaba.core.domain.user

// UserIdはSagaの中でインスタンスを構築します。
// その際、テストのAssertフェーズでUserIdを特定することができず正確なテストが行えないため、Factoryを用意しています。
interface UserIdFactory {
    fun handle(): UserId
}

### 冪等性の実装
* InteractorようにDB利用した冪等性を調査するサービスを作るべきやな
* Aggregateは集約内で冪等性を検証するMAPを持たせてコマンドとイベントに冪等性の実装する。
* Aggregateの基底クラス作って、そこで冪等性の管理したくね? (正直あんまりできないんじゃねって思ってる)

### 細かい
* Dockerfile or Docker-composeのlatest運用をやめたい。バージョン固定がやっぱり安心
* region情報も環境変数で入れたら？ 基本ap-northeast-1しか使わんと思うけど、DR対策とか考えると差し替えられるようにしたほうがええんかなぁ。
* SQLの間違いをJPAの機能で防げないかな？　JPA独自のSQLとかなんかあった気がするけど。どうなんやろう
* JPAのEnittyってkotlinのdataclass使わないほうがいいらしいぜ。
* 多分PRojectorないってあえて例外出してでっとレターに補足されたほうが後々楽だと思う。


### 追加したい
* リトライ不許可例外を出して、それ以外はリトライさせるようにしよう。基本的にUsecaseExcpetion、DomainExcpedtionはリトライさせない。
* LookupTableの実装周り見直したい。　DB消されたときSubsciption`rocessero反応しないから、Trakingも付け足したほうがいいのかな?
* findをgetに
### 修正したい
* JPAのEntityのID値オブジェクトにしたんだけどできないのかな？

# memo
## リトライ条件
RuntimeExceptionであろうが、Axonサーバーを使ってる限りすべてリトライ対象となる！！！！ドキュメントに書いとけや！

 * 集約のコマンドハンドラがーない　→　リトライ
 * 集約のコマンドハンドラー内でException→ リトライ
 * 集約のコマンドハンドラナーないでRuntimeException → リトライ
 * 集約のコマンドハンドラー内で, UseCaseException。　除外登録　→　リトライされず。
 * 外部コマンドハンドラー内で Excetion → リトライ
 * 外部コマンドハンドラー内で RuntimeException →　リトライ

* cognito修正するべし。

https://localhost:5052/oauth2/authorize?response_type=token&client_id=example-client-name&redirect_uri=http://localhost:8082






package jp.inaba.service.application.saga.createuser

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.auth.event.SignupConfirmedEvent
import jp.inaba.message.basket.event.BasketCreatedEvent
import jp.inaba.message.user.event.UserCreatedEvent

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class UserSetupSagaState private constructor(
val emailAddress: String,
) {
private var _userId: UserId? = null

    @get:JsonIgnore
    val userId: UserId
        get() = _userId ?: throw IllegalStateException("userId が初期化されていません。associateUserCreatedEventが呼ばれていません。")

    private var _basketId: BasketId? = null

    @get:JsonIgnore
    val basketId: BasketId
        get() = _basketId ?: throw IllegalStateException("basketId が初期化されていません。associateBasketCreatedEventが呼ばれていません。")

    constructor(event: SignupConfirmedEvent) : this(emailAddress = event.emailAddress)

    fun associateUserCreatedEvent(event: UserCreatedEvent) {
        _userId = UserId(event.id)
    }

    fun associateBasketCreatedEvent(event: BasketCreatedEvent) {
        _basketId = BasketId(event.id)
    }
}

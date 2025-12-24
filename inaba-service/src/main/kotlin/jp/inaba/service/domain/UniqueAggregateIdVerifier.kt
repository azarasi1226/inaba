package jp.inaba.service.domain

interface UniqueAggregateIdVerifier {
    /**
     * EventStore内では種別が違う集約だったとしても同じIDを使用することができない制約がある。
     *
     * また、AxonFrameworkの性質なのか、AggregateIdの重複だけは CommandHandlerInterceptorの中でキャッチできず、
     * エラー処理の共通化ができなかった。そのため苦肉の策としてこのようなクラスを作っている。
     */
    fun hasDuplicateAggregateId(id: String): Boolean
}

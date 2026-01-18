package jp.inaba.service.domain

// TODO: CreateAggregateVerifierに名前変更検討
interface UniqueAggregateIdVerifier {
    /**
     * EventStore内では種類が違う集約だったとしても同じAggregateIdを使用できない制約がある。
     *
     * またAxonFrameworkの性質なのか、AggregateIdの重複だけは CommandHandlerInterceptorの中でキャッチできず、
     * エラー処理の共通化ができなかった。そのため苦肉の策としてこのようなインタフェースを用意し、Aggregateの作成前に検証をするようにする。
     */
    fun hasDuplicateAggregateId(id: String): Boolean
}

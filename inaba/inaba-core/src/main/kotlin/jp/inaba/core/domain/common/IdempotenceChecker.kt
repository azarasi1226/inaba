package jp.inaba.core.domain.common

class IdempotenceChecker {
    companion object {
        // 最高100個のCommandHandlerを持つAggregateに対応できる。
        // あまり増えすぎてもメモリを圧迫するのでとりあえず、100..!!
        private const val MAX_ID_COUNT = 100
    }

    private val idempotencyIds = LinkedHashSet<IdempotencyId>()

    fun register(idempotencyId: IdempotencyId) {
        if (idempotencyIds.size >= MAX_ID_COUNT) {
            val lastId = idempotencyIds.last()
            idempotencyIds.remove(lastId)
        }

        idempotencyIds.add(idempotencyId)
    }

    fun isIdempotent(idempotencyId: IdempotencyId): Boolean {
        return idempotencyIds.contains(idempotencyId)
    }
}

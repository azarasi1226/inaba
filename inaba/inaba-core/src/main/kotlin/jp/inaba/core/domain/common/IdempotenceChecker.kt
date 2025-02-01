package jp.inaba.core.domain.common

class IdempotenceChecker {
    companion object {
        // さすがに100あれば足りるやろ...
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

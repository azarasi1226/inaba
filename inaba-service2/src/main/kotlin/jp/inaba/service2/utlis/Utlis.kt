package jp.inaba.service2.utlis

import jp.inaba.core.domain.common.SortCondition
import jp.inaba.core.domain.common.SortDirection
import org.jooq.SortField
import org.jooq.impl.DSL
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Instant.toTokyoLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.of("Asia/Tokyo"))

fun SortCondition.toOrderField(): SortField<*> {
    val field = DSL.field(dbColumnName)
    return when (sortDirection) {
        SortDirection.ASC -> field.asc()
        SortDirection.DESC -> field.desc()
    }
}

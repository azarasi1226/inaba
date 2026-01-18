package jp.inaba.service.utlis

import jp.inaba.core.domain.common.SortCondition
import jp.inaba.core.domain.common.SortDirection
import org.jooq.SortField
import org.jooq.impl.DSL

fun SortCondition.toOrderField(): SortField<*> {
    val field = DSL.field(dbColumnName)
    return when (sortDirection) {
        SortDirection.ASC -> field.asc()
        SortDirection.DESC -> field.desc()
    }
}

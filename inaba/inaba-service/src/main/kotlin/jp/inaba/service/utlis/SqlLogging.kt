package jp.inaba.service.utlis

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.Query
import org.hibernate.query.sql.internal.NativeQueryImpl

private val logger = KotlinLogging.logger {}

fun Query.sqlDebugLogging() {
    val sql = (this as NativeQueryImpl<*>).queryString
    logger.debug { sql }
}
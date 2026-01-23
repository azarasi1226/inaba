package jp.inaba.service.utlis

import jp.inaba.core.domain.common.UseCaseError
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.query
import org.axonframework.messaging.HandlerExecutionException
import org.axonframework.queryhandling.QueryGateway
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun HandlerExecutionException.isWrapUseCaseError(): Boolean = this.getDetails<UseCaseError>().isPresent

fun HandlerExecutionException.getWrapUseCaseError(): UseCaseError = this.getDetails<UseCaseError>().get()

fun Instant.toTokyoLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.of("Asia/Tokyo"))

/**
 * QueryGatewayでクエリを指数関数バックオフするユーティリティ関数
 */
inline fun <reified R : Any, reified Q : Any> QueryGateway.retryQuery(
  request: Q,
  maxAttempts: Int = 3,
  baseDelayMs: Long = 100L,
): R {
  for (attempt in 1..maxAttempts) {
    try {
      return this.query<R, Q>(request).get()
    } catch (e: Exception) {
      if (attempt == maxAttempts) throw e
      val delay = baseDelayMs * (1L shl (attempt - 1))
      Thread.sleep(delay)
    }
  }
  throw IllegalStateException("unreachable")
}
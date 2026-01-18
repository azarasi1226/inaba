package jp.inaba.service.utlis

import jp.inaba.core.domain.common.UseCaseError
import org.axonframework.messaging.HandlerExecutionException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun HandlerExecutionException.isWrapUseCaseError(): Boolean = this.getDetails<UseCaseError>().isPresent

fun HandlerExecutionException.getWrapUseCaseError(): UseCaseError = this.getDetails<UseCaseError>().get()

fun Instant.toTokyoLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.of("Asia/Tokyo"))

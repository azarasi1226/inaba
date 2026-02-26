package jp.inaba.service2.utlis

import org.axonframework.messaging.queryhandling.gateway.QueryGateway
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Instant.toTokyoLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.of("Asia/Tokyo"))

package jp.inaba.service.application.saga

import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.modelling.saga.SagaLifecycle

private val logger = KotlinLogging.logger {}

abstract class SagaBase {
    fun fatalError() {
        logger.error { "Saga: [${this::class.simpleName}] a fatal error" }
        logger.error { "補償トランザクションがすべて失敗しました。データの整合性を確認してください。" }
        SagaLifecycle.end()
    }
}

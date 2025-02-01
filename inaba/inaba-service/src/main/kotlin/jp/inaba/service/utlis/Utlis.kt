package jp.inaba.service.utlis

import jp.inaba.core.domain.common.UseCaseError
import org.axonframework.messaging.HandlerExecutionException

fun HandlerExecutionException.isWrapUseCaseError() : Boolean {
    return this.getDetails<UseCaseError>().isPresent
}

fun HandlerExecutionException.getWrapUseCaseError(): UseCaseError {
    return this.getDetails<UseCaseError>().get()
}
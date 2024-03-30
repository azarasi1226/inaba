package jp.inaba.basket.service.presentation.basket.setbasketitem

import jp.inaba.basket.service.application.command.basket.setbasketitem.ProductNotFoundException
import jp.inaba.basket.service.application.command.basket.setbasketitem.UserNotFoundException
import jp.inaba.common.presentation.shared.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [SetBasketItemController::class])
class SetBasketItemControllerAdvice {
    @ExceptionHandler(ProductNotFoundException::class)
    fun handle(ex: ProductNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(ex.message),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handle(ex: UserNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(ex.message),
            HttpStatus.NOT_FOUND
        )
    }
}
package jp.inaba.basket.service.presentation.controller.basket

import org.axonframework.modelling.command.AggregateNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommonAdvice {
    @ExceptionHandler(AggregateNotFoundException::class)
    fun handle(ex: AggregateNotFoundException): ResponseEntity<String> {
        //TODO(レスポンス構造考える)
        return ResponseEntity("対象の集約が見つかりませんでした", HttpStatus.NOT_FOUND)
    }
}
package jp.inaba.basket.service.presentation.common

import jp.inaba.basket.service.application.query.basket.BasketNotFoundException
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.modelling.command.AggregateNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommonAdvice {
    @ExceptionHandler(CommandExecutionException::class)
    fun handle(ex: CommandExecutionException): ResponseEntity<String> {

        ex.getDetails<BasketNotFoundException>()
            .map {
                println(it)
            }

        val a = ex.cause?.cause

        println(a)

        (ex.cause as? AggregateNotFoundException)?.run {
            return ResponseEntity("対象の集約が見つかりませんでした", HttpStatus.NOT_FOUND)
        }

        return ResponseEntity("謎", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(AggregateNotFoundException::class)
    fun handle(ex: AggregateNotFoundException): ResponseEntity<String> {
        //TODO(レスポンス構造考える)
        return ResponseEntity("対象の集約が見つかりませんでした", HttpStatus.NOT_FOUND)
    }
}
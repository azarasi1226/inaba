package jp.inaba.catalog.service.presentation.controller.product

import jp.inaba.catalog.service.application.query.product.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [ProductController::class])
class ProductControllerAdvice {
    @ExceptionHandler(ProductNotFoundException::class)
    fun handle(ex: ProductNotFoundException): ResponseEntity<String> {
        //TODO(レスポンス構造考える)
        return ResponseEntity("そんなの無いよ", HttpStatus.NOT_FOUND)
    }
}
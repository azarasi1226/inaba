package jp.inaba.apigateway.application.product.create

import org.springframework.web.multipart.MultipartFile

data class CreateProductInput(
    val id: String,
    val brandId: String,
    val name: String,
    val description: String,
    val image: MultipartFile?,
    val price: Int,
    val quantity: Int,
)

package jp.inaba.apigateway.application.product.update

import org.springframework.web.multipart.MultipartFile

data class UpdateProductInput(
    val id: String,
    val name: String,
    val description: String,
    val image: MultipartFile?,
    val price: Int,
)
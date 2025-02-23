package jp.inaba.apigateway.presentation.product.update

import jp.inaba.apigateway.application.product.update.UpdateProductInput
import jp.inaba.apigateway.application.product.update.UpdateProductInteractor
import jp.inaba.apigateway.presentation.product.ProductController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class UpdateProductController(
    private val interactor: UpdateProductInteractor
) : ProductController {
    @PutMapping("/api/products/{id}")
    fun handle(
        @PathVariable("id")
        id: String,
        @RequestPart("name")
        name: String,
        @RequestPart("description")
        description: String,
        @RequestPart("image", required = false)
        image: MultipartFile?,
        // TODO(なぜかIntにするとデータ送信時に失敗する？？なぜ???)
        @RequestPart("price")
        price: String,
    ) {
        val input = UpdateProductInput(
            id = id,
            name = name,
            description = description,
            image = image,
            price = price.toInt()
        )

        interactor.handle(input)
    }
}

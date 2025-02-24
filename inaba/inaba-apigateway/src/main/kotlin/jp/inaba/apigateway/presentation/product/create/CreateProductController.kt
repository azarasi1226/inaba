package jp.inaba.apigateway.presentation.product.create

import io.swagger.v3.oas.annotations.Operation
import jp.inaba.apigateway.application.product.create.CreateProductInput
import jp.inaba.apigateway.application.product.create.CreateProductInteractor
import jp.inaba.apigateway.presentation.product.ProductController
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class CreateProductController(
    private val interactor: CreateProductInteractor,
) : ProductController {
    @PostMapping("/api/products", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        operationId = "createProduct"
    )
    fun handle(
        @RequestPart("id")
        id: String,
        @RequestPart("brandId")
        brandId: String,
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
        val input =
            CreateProductInput(
                id = id,
                brandId = brandId,
                name = name,
                description = description,
                image = image,
                price = price.toInt(),
            )

        interactor.handle(input)
    }
}

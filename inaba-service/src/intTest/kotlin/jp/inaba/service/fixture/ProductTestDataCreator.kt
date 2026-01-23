package jp.inaba.service.fixture

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.service.domain.product.InternalCreateProductCommand
import org.axonframework.commandhandling.gateway.CommandGateway

class ProductTestDataCreator(
    private val commandGateway: CommandGateway,
) {
    fun create(
        id: ProductId = ProductId(),
        brandId: BrandId = BrandId(),
        name: ProductName = ProductName("testProduct1"),
        description: ProductDescription = ProductDescription("testDescription1"),
        imageUrl: ProductImageURL = ProductImageURL("http://example.com/image.png"),
        price: ProductPrice = ProductPrice(100),
        quantity: StockQuantity = StockQuantity(10),
    ): ProductId {
        val command =
            InternalCreateProductCommand(
                id = id,
                brandId = brandId,
                name = name,
                description = description,
                imageUrl = imageUrl,
                price = price,
                quantity = quantity,
            )

        commandGateway.sendAndWait<Any>(command)

        return id
    }
}

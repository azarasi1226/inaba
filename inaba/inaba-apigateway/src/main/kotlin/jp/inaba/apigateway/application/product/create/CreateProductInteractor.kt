package jp.inaba.apigateway.application.product.create

import jp.inaba.apigateway.application.product.WebpConverter
import jp.inaba.apigateway.application.product.WebpUploader
import jp.inaba.grpc.product.CreateProductGrpc
import jp.inaba.grpc.product.CreateProductRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
class CreateProductInteractor(
    private val webpConverter: WebpConverter,
    private val webpUploader: WebpUploader,
    @GrpcClient("global")
    private val grpcService: CreateProductGrpc.CreateProductBlockingStub
) {
    fun handle(input: CreateProductInput) {
        val imageUrl = if(input.image != null) {
            val webp = webpConverter.handle(input.image)
            webpUploader.handle(webp)
        }
        else {
            null
        }

        val grpcRequest = CreateProductRequest.newBuilder()
            .setId(input.id)
            .setBrandId(input.brandId)
            .setName(input.name)
            .setDescription(input.description)
            .apply { if (imageUrl != null) setImageUrl(imageUrl) }
            .setPrice(input.price)

            .build()


        grpcService.handle(grpcRequest)
    }
}
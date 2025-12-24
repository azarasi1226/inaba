package jp.inaba.apigateway.application.product.update

import jp.inaba.apigateway.application.product.WebpConverter
import jp.inaba.apigateway.application.product.WebpUploader
import jp.inaba.grpc.product.UpdateProductGrpc
import jp.inaba.grpc.product.UpdateProductRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
class UpdateProductInteractor(
    private val webpConverter: WebpConverter,
    private val webpUploader: WebpUploader,
    @GrpcClient("global")
    private val grpcService: UpdateProductGrpc.UpdateProductBlockingStub,
) {
    fun handle(input: UpdateProductInput) {
        val imageUrl =
            input.image
                ?.let(webpConverter::handle)
                ?.let(webpUploader::handle)

        val grpcRequest =
            UpdateProductRequest
                .newBuilder()
                .setId(input.id)
                .setName(input.name)
                .setDescription(input.description)
                .apply { if (imageUrl != null) setImageUrl(imageUrl) }
                .setPrice(input.price)
                .build()

        grpcService.handle(grpcRequest)
    }
}

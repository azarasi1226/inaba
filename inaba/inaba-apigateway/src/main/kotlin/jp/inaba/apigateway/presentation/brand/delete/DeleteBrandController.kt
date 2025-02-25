package jp.inaba.apigateway.presentation.brand.delete

import io.swagger.v3.oas.annotations.Operation
import jp.inaba.apigateway.presentation.brand.BrandController
import jp.inaba.grpc.brand.DeleteBrandGrpc
import jp.inaba.grpc.brand.DeleteBrandRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteBrandController(
    @GrpcClient("global")
    private val grpcService: DeleteBrandGrpc.DeleteBrandBlockingStub
) : BrandController {
    @DeleteMapping("/api/brands/{id}")
    @Operation(
        operationId = "deleteBrand"
    )
    fun handle(
        @PathVariable("id")
        id: String
    ) {
        val grpcRequest = DeleteBrandRequest.newBuilder()
            .setId(id)
            .build()

        grpcService.handle(grpcRequest)
    }
}
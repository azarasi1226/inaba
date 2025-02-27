package jp.inaba.apigateway.presentation.brand.findbyid

import io.swagger.v3.oas.annotations.Operation
import jp.inaba.apigateway.presentation.brand.BrandController
import jp.inaba.grpc.brand.FindBrandByIdGrpc
import jp.inaba.grpc.brand.FindBrandByIdRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class FindBrandByIdController(
    @GrpcClient("global")
    private val grpcService: FindBrandByIdGrpc.FindBrandByIdBlockingStub,
) : BrandController {
    @GetMapping("/api/brands/{id}")
    @Operation(
        operationId = "findBrandById",
    )
    fun handle(
        @PathVariable("id")
        id: String,
    ): FindBrandByIdHttpResponse {
        val grpcRequest =
            FindBrandByIdRequest.newBuilder()
                .setId(id)
                .build()

        val grpcResponse = grpcService.handle(grpcRequest)

        return FindBrandByIdHttpResponse(grpcResponse.name)
    }
}

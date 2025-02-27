package jp.inaba.apigateway.presentation.brand.create

import io.swagger.v3.oas.annotations.Operation
import jp.inaba.apigateway.presentation.brand.BrandController
import jp.inaba.grpc.brand.CreateBrandGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateBrandController(
    @GrpcClient("global")
    private val grpcService: CreateBrandGrpc.CreateBrandBlockingStub,
) : BrandController {
    @PostMapping("/api/brands")
    @Operation(
        operationId = "createBrand",
    )
    fun handle(
        @RequestBody
        request: CreateBrandHttpRequest,
    ) {
        val grpcRequest = request.toGrpcRequest()

        grpcService.handle(grpcRequest)
    }
}

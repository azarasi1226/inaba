package jp.inaba.apigateway.brand.create

import jp.inaba.apigateway.brand.BrandController
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
    fun handle(
        @RequestBody
        request: CreateBrandHttpRequest
    ) {
        val grpcRequest = request.toGrpcRequest()

        grpcService.handle(grpcRequest)
    }
}
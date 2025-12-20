package jp.inaba.apigateway.application.admin.createtestdata

import de.huxhorn.sulky.ulid.ULID
import jp.inaba.grpc.brand.CreateBrandGrpc
import jp.inaba.grpc.brand.CreateBrandRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
class BrandTestDataCreator(
    @GrpcClient("global")
    private val grpcService: CreateBrandGrpc.CreateBrandBlockingStub,
) {
    fun handle(): String {
        val id = ULID().nextULID()
        val grpcRequest =
            CreateBrandRequest
                .newBuilder()
                .setId(id)
                .setName("ASUS")
                .build()

        grpcService.handle(grpcRequest)

        return id
    }
}

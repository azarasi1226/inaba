package jp.inaba.datakey.service.jp.inaba.datakey.service.presentation.datakey.get

import com.github.michaelbull.result.mapBoth
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import jp.inaba.datakey.grpc.GetDataKeyGrpc
import jp.inaba.datakey.grpc.GetDataKeyRequest
import jp.inaba.datakey.grpc.GetDataKeyResponse
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.get.GetDataKeyInput
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.get.GetDataKeyInteractor
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.GetDataKeyError
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.RelationId
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GetDataKeyGrpcService(
    private val getDataKeyInteractor: GetDataKeyInteractor
) : GetDataKeyGrpc.GetDataKeyImplBase() {
     override fun handle(request: GetDataKeyRequest, responseObserver: StreamObserver<GetDataKeyResponse>) {
        val relationId = RelationId(request.relationId)
        val input = GetDataKeyInput(relationId)

        val result = getDataKeyInteractor.handle(input)

        result.mapBoth(
            success = {
                val response = GetDataKeyResponse
                    .newBuilder()
                    .setBase64PlaneDataKey(result.value.base64PlaneDataKey.value)
                    .build()

                responseObserver.onNext(response)
            },
            failure = {
                when(it) {
                    GetDataKeyError.DATAKEY_NOT_FOUND -> {
                        val status = Status.NOT_FOUND
                            .withDescription(it.errorMessage)
                        responseObserver.onError(StatusRuntimeException(status))
                    }
                }
            }
        )
        responseObserver.onCompleted()
    }
}
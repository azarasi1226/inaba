package jp.inaba.datakey.service.jp.inaba.datakey.service.presentation.datakey.create

import com.github.michaelbull.result.mapBoth
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import jp.inaba.datakey.grpc.CreateDataKeyGrpc
import jp.inaba.datakey.grpc.CreateDataKeyRequest
import jp.inaba.datakey.grpc.CreateDataKeyResponse
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.create.CreateDataKeyInput
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.create.CreateDataKeyInteractor
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.CreateDataKeyError
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.RelationId
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class CreateDataKeyGrpcService(
    private val createDataKeyInteractor: CreateDataKeyInteractor,
) : CreateDataKeyGrpc.CreateDataKeyImplBase() {
    override fun handle(
        request: CreateDataKeyRequest,
        responseObserver: StreamObserver<CreateDataKeyResponse>,
    ) {
        val relationId = RelationId(request.relationId)
        val input = CreateDataKeyInput(relationId)

        val result = createDataKeyInteractor.handle(input)

        result.mapBoth(
            success = {
                val response =
                    CreateDataKeyResponse
                        .newBuilder()
                        .setBase64PlaneDataKey(result.value.base64PlaneDataKey.value)
                        .build()
                responseObserver.onNext(response)
            },
            failure = {
                when (it) {
                    CreateDataKeyError.DATAKEY_ALREADY_EXISTS -> {
                        val status =
                            Status.ALREADY_EXISTS
                                .withDescription(it.errorMessage)
                        responseObserver.onError(StatusRuntimeException(status))
                    }
                }
            },
        )
        responseObserver.onCompleted()
    }
}

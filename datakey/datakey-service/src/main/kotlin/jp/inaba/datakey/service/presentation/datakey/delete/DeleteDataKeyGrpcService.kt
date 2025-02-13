package jp.inaba.datakey.service.jp.inaba.datakey.service.presentation.datakey.delete

import com.github.michaelbull.result.mapBoth
import com.google.protobuf.Empty
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import jp.inaba.datakey.grpc.DeleteDataKeyGrpc
import jp.inaba.datakey.grpc.DeleteDataKeyRequest
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.delete.DeleteDataKeyInput
import jp.inaba.datakey.service.jp.inaba.datakey.service.application.datakey.delete.DeleteDataKeyInteractor
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.DeleteDataKeyError
import jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey.RelationId
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class DeleteDataKeyGrpcService(
    private val deleteDataKeyInteractor: DeleteDataKeyInteractor,
) : DeleteDataKeyGrpc.DeleteDataKeyImplBase() {
    override fun handle(
        request: DeleteDataKeyRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val relationId = RelationId(request.relationId)
        val input = DeleteDataKeyInput(relationId)

        val result = deleteDataKeyInteractor.handle(input)

        result.mapBoth(
            success = {
                val response = Empty.getDefaultInstance()
                responseObserver.onNext(response)
            },
            failure = {
                when (it) {
                    DeleteDataKeyError.DATAKEY_NOT_FOUND -> {
                        val status =
                            Status.NOT_FOUND
                                .withDescription(it.errorMessage)
                        responseObserver.onError(StatusRuntimeException(status))
                    }
                }
            },
        )
        responseObserver.onCompleted()
    }
}

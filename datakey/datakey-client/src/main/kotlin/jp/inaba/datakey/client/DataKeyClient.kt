package jp.inaba.datakey.client

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.StatusRuntimeException
import jp.inaba.datakey.grpc.CreateDataKeyGrpc
import jp.inaba.datakey.grpc.CreateDataKeyRequest
import jp.inaba.datakey.grpc.GetDataKeyGrpc
import jp.inaba.datakey.grpc.GetDataKeyRequest
import java.util.Base64

class DataKeyClient(hostName: String, port: Int) {
  private val  channel: ManagedChannel = ManagedChannelBuilder
      .forAddress(hostName, port)
      .usePlaintext()
      .build()

    fun getOrCreateDataKey(relationId: String): DataKey {
        // 存在していなかったら新規作成
        var base64DataKey = getDataKey(relationId)
        if (base64DataKey == null) {
            base64DataKey = createDataKey(relationId)
        }

        val binaryDataKey = Base64.getDecoder().decode(base64DataKey)
        return DataKey(binaryDataKey)
    }

    private fun createDataKey(relationId: String): String {
        val request = CreateDataKeyRequest.newBuilder()
            .setRelationId(relationId)
            .build()

        val response = CreateDataKeyGrpc.newBlockingStub(channel).handle(request)

        return response.base64PlaneDataKey
    }

    private fun getDataKey(relationId: String): String? {
        val request = GetDataKeyRequest.newBuilder()
            .setRelationId(relationId)
            .build()

        return try{
            val response = GetDataKeyGrpc.newBlockingStub(channel).handle(request)

            response.base64PlaneDataKey
        } catch (e: StatusRuntimeException) {
            if(e.status == Status.NOT_FOUND) {
                null
            }
            throw e
        }
    }
}

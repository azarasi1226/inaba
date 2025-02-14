package jp.inaba.apigateway

import de.huxhorn.sulky.ulid.ULID
import io.grpc.StatusRuntimeException
import jp.inaba.grpc.user.CreateUserGrpc
import jp.inaba.grpc.user.CreateUserRequest
import jp.inaba.grpc.user.FindUserMetadataBySubjectGrpc
import jp.inaba.grpc.user.FindUserMetadataBySubjectRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service


//TODO("適当すぎるし、ダメダメなので後でちゃんと書こう")
@Service
class AuthenticatedUserService(
    @GrpcClient("global")
    private val grpcService: FindUserMetadataBySubjectGrpc.FindUserMetadataBySubjectBlockingStub,

    @GrpcClient("global")
    private val crateUserGrpcService: CreateUserGrpc.CreateUserBlockingStub
) {
    // ユーザーに紐づいたUserMetadataを取得。
    fun getUserMetadata(): UserMetaData {
        val sub = getJwt().subject
        val grpcRequest = FindUserMetadataBySubjectRequest.newBuilder()
            .setSubject(sub)
            .build()

        try {
            val grpcResponse = grpcService.handle(grpcRequest)

            return UserMetaData(
                userId = grpcResponse.userId,
                basketId = grpcResponse.basketId
            )
        } catch (e: StatusRuntimeException) {
            val errorCodeKey = io.grpc.Metadata.Key.of("error-code", io.grpc.Metadata.ASCII_STRING_MARSHALLER)
            val errorCode = e.trailers?.get(errorCodeKey)

            //ユーザーが登録されていない場合作成しに行く。
            // 初回リクエストはユーザーが作成されていないため呼ぶ必要がある。
            // "1" は存在していないを表す UseCaseError
            // ぶっちゃけこの辺はもうちょっとエラーコード考えたほうがいいと思う。適当すぎるから
            if (errorCode == "1") {
                val userId = ULID().nextULID()
                val createUserGrpcRequest = CreateUserRequest.newBuilder()
                    .setId(userId)
                    .setSubject(sub)
                    .build()
                crateUserGrpcService.handle(createUserGrpcRequest)
                Thread.sleep(100)
                val grpcResponse = grpcService.handle(grpcRequest)

                return UserMetaData(
                    userId = grpcResponse.userId,
                    basketId = grpcResponse.basketId
                )
            } else {
                throw Exception("ユーザー情報が取得できませんでした。")
            }
        }
    }

    // ログイン中のユーザーのJWTを取得
    private fun getJwt(): Jwt {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication.principal is Jwt) {
            return authentication.principal as Jwt
        } else {
            throw IllegalStateException("Authentication principal is not of type Jwt")
        }
    }
}

data class UserMetaData(
    val userId: String,
    val basketId: String
)
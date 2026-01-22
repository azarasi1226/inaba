package jp.inaba.apigateway

import io.grpc.Metadata
import io.grpc.StatusRuntimeException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(StatusRuntimeException::class)
    fun handle(e: StatusRuntimeException): ResponseEntity<Any> {
        // よく使われるキーを取り出す
        val trailers = e.trailers
        val errorTypeKey = Metadata.Key.of("error-type", Metadata.ASCII_STRING_MARSHALLER)
        val errorCodeKey = Metadata.Key.of("error-code", Metadata.ASCII_STRING_MARSHALLER)
        val errorMessageKey = Metadata.Key.of("error-message-bin", Metadata.BINARY_BYTE_MARSHALLER)

        val errorType = trailers?.get(errorTypeKey)
        val errorCode = trailers?.get(errorCodeKey)
        val errorMessage = trailers?.get(errorMessageKey)?.toString(Charsets.UTF_8)

        val body =
            linkedMapOf<String, Any?>().apply {
                put("code", e.status.code)
                put("description", e.status.description)
                put("errorType", errorType)
                put("errorCode", errorCode)
                put("errorMessage", errorMessage)
            }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(body)
    }
}

// @RestControllerAdvice
// class ControllerAdvice {
//    @ExceptionHandler(StatusRuntimeException::class)
//    fun handle(e: StatusRuntimeException): ResponseEntity<Any> {
//        // gRPC のステータス情報
//        val statusCode = e.status.code
//        val statusDescription = e.status.description
//
//        // トレーラー（metadata）を抽出（null の可能性あり）
//        val trailers = e.trailers
//
//        // よく使われるキーを取り出す
//        val errorTypeKey = Metadata.Key.of("error-type", Metadata.ASCII_STRING_MARSHALLER)
//        val errorMessageKey = Metadata.Key.of("error-message", Metadata.ASCII_STRING_MARSHALLER)
//        val errorMessageKey2222 = Metadata.Key.of("error-messagsssssssse", Metadata.ASCII_STRING_MARSHALLER)
//        val errorType = trailers?.get(errorTypeKey)
//        val errorMessageFromTrailer = trailers?.get(errorMessageKey)dsafd aa
//
//        val errorMessageFromTrailer2222 = trailers?.get(errorMessageKey2222)
//
//        // すべてのトレーラーエントリを収集（文字列として取り出せるものをベストエフォートで）
//        val trailerMap = linkedMapOf<String, Any?>()
//        if (trailers != null) {
//            try {
//                // Metadata.keys() はメタデータ内のキー一覧を返す
//                for (key in trailers.keys()) {
//                    try {
//                        val metaKey = Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER)
//                        trailerMap[key] = trailers.get(metaKey)
//                    } catch (inner: Exception) {
//                        // バイナリ値や対応していないマーシャラの可能性があるためプレースホルダを入れる
//                        trailerMap[key] = "<non-text or unsupported marshaller>"
//                    }
//                }
//            } catch (ignored: Exception) {
//                // keys() の呼び出しが例外を投げる実装がある可能性があるため無視して続行
//            }
//        }
//
//        // 入手できたエラー要素を明示的にレスポンスボディに組み立てる
//        val body = linkedMapOf<String, Any?>().apply {
//            put("grpcStatusCode", statusCode.name)
//            put("grpcStatusDescription", statusDescription ?: e.message)
//            put("errorType", errorType)
//            put("errorMessage", errorMessageFromTrailer ?: e.message)
//            put("rawTrailers", trailers?.toString())
//            put("trailers", trailerMap)
//        }
//
//        val httpStatus = if (statusCode == Status.INVALID_ARGUMENT.code) {
//            HttpStatus.BAD_REQUEST
//        } else {
//            HttpStatus.INTERNAL_SERVER_ERROR
//        }
//
//        return ResponseEntity.status(httpStatus).body(body)
//    }
// }

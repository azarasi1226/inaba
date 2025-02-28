package jp.inaba.apigateway.application.product

import de.huxhorn.sulky.ulid.ULID
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class WebpUploader(
    private val s3Client: S3Client,
    @Value("\${minio.bucket}")
    private val bucketName: String,
) {
    fun handle(content: ByteArray): String {
        val key = ULID().nextULID()
        val filename = "$key.webp"

        uploadImage(filename, content)

        return getFileUrl(filename)
    }

    private fun uploadImage(
        key: String,
        content: ByteArray,
    ) {
        val putObjectRequest =
            PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("image/webp")
                .build()
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content))
    }

    private fun getFileUrl(key: String): String {
        val url =
            s3Client.utilities().getUrl {
                it.bucket(bucketName).key(key)
            }

        return url.toString()
    }
}

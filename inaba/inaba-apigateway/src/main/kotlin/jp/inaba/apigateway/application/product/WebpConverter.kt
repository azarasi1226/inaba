package jp.inaba.apigateway.application.product

import org.bytedeco.javacpp.BytePointer
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.opencv_core.Mat
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.Locale

@Service
class WebpConverter {
    fun handle(imageFile: MultipartFile): ByteArray {
        if(!canConvertToWebp(imageFile)) {
            throw Exception("変換不可能なファイルです。")
        }

        return convertToWebp(imageFile)
    }

    private fun canConvertToWebp(file: MultipartFile): Boolean {
        // 拡張子で変換可能か判断
        val allowedExtensions = listOf("jpg", "jpeg", "png")
        val fileExtension = file.originalFilename?.substringAfterLast(".")?.lowercase(Locale.getDefault())

        return fileExtension in allowedExtensions
    }

    private fun convertToWebp(file: MultipartFile): ByteArray {
        // バイト配列から Matオブジェクト(C++の画像変換処理のラッパーらしい)に変換する
        val bytePointer = BytePointer(*file.bytes)
        val fromImageMat = opencv_imgcodecs.imdecode(
            Mat(bytePointer),
            opencv_imgcodecs.IMREAD_UNCHANGED
        )

        try {
            // C++ のラッパーなので、変換処理の成功可否はsuccess戻り値でやるらしい。
            val outputPointer = BytePointer()
            val success = opencv_imgcodecs.imencode(
                ".webp",
                fromImageMat,
                outputPointer
            )
            if(!success) {
                throw Exception("画像の変換に失敗しました。")
            }

            return outputPointer.stringBytes
        } finally {
            // Matオブジェクトの解放 (これやらないとどうなるんだろう...?ガベージコレクションとかされないのかな？)
            // ChatGPTはC++で定義されたメモリが解放されなくてたまっていく的なこと言ってるんだけど.....
            fromImageMat.release()
        }
    }
}
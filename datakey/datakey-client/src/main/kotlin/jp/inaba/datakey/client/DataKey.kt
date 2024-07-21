package jp.inaba.datakey.client

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class DataKey(private val dataKey: ByteArray) {
    companion object {
        private const val cipherAlgorithm = "AES"
    }

    fun encrypt(planText: String): EncryptedText {
        val result = encrypt(planText.toByteArray())

        return EncryptedText(Base64.getEncoder().encodeToString(result))
    }

    fun decrypt(encryptedText: EncryptedText): String {
        val result = decrypt(encryptedText.value.toByteArray())

        return String(result)
    }

    private fun encrypt(planeValue: ByteArray): ByteArray {
        return SecretKeySpec(dataKey, cipherAlgorithm)
            .let {
                val cipher = Cipher.getInstance(cipherAlgorithm)
                cipher.init(Cipher.ENCRYPT_MODE, it)

                cipher
            }.doFinal(planeValue)
    }

    private fun decrypt(encryptedValue: ByteArray): ByteArray {
        return SecretKeySpec(dataKey, cipherAlgorithm)
            .let {
                val cipher = Cipher.getInstance(cipherAlgorithm)
                cipher.init(Cipher.DECRYPT_MODE, it)

                cipher
            }
            .doFinal(encryptedValue)
    }
}
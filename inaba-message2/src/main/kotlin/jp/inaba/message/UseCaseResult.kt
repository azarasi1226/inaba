package jp.inaba.message


interface QueryResult<T> {
  val success: Boolean
  val data: T?
  val error: Error?
}

//TODO:
abstract class UseCaseResult {
  abstract val success: Boolean
  abstract val error: Error?

  companion object {
    fun success(): UseCaseResult = object : UseCaseResult() {
      override val success: Boolean = true
      override val error: Error? = null
    }
  }

  override fun toString(): String {
    return "UseCaseResult(success=$success, error=$error)"
  }
}

class UseCaseException(
  val error: Error,
) : Exception( "message:[${error.message}]")

fun UseCaseResult.throwIfError() {
  if (!this.success) throw UseCaseException(this.error!!)
}


// 将来の拡張性を考慮して、Errorを別クラスに分離しています。
data class Error(
  val message: String,
)
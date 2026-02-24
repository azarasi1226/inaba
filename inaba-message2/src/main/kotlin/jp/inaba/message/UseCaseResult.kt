package jp.inaba.message


//TODO:
interface UseCaseResult {
  val success: Boolean
  val error: Error?
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
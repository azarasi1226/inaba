package jp.inaba.message

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


class QueryResult<T>(
  val success: Boolean,
  val data: T?,
  val error: Error?,
) {
  companion object {
    fun <T> success(data: T): QueryResult<T> {
      return QueryResult(success = true, data = data, error = null)
    }

    fun <T> faile(error: Error): QueryResult<T> {
      return QueryResult(success = false, data = null, error = error)
    }
  }

  override fun toString(): String {
    return "QueryResult(success=$success, data=$data, error=$error)"
  }
}

class CommandResult private constructor(
  val success: Boolean,
  val error: Error? = null
) {
  companion object {
    fun success(): CommandResult {
      return CommandResult(success = true)
    }

    fun faile(error: Error): CommandResult {
      return CommandResult(success = false, error = error)
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

fun CommandResult.throwIfError() {
  if (!this.success) throw UseCaseException(this.error!!)
}



// 将来の拡張性を考慮して、Errorを別クラスに分離しています。
data class Error(
  val message: String,
)
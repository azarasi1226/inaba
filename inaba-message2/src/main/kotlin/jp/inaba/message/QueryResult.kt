package jp.inaba.message

interface QueryResult2<T> {
  val success: Boolean
  val data: T?
  val error: Error?
}

fun <T> QueryResult2<T>.getOrThrow(): T {
  if (!success) {
    throw UseCaseException(error!!)
  }
  return data!!
}
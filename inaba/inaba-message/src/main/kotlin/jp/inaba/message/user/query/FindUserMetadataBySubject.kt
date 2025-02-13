package jp.inaba.message.user.query

data class FindUserMetadataBySubjectQuery(
    val subject: String,
)

data class FindUserMetadataBySubjectResult(
    val userId: String,
    val basketId: String,
)

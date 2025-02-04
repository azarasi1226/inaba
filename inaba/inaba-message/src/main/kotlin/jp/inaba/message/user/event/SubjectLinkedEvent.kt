package jp.inaba.message.user.event

data class SubjectLinkedEvent(
    override val id: String,
    val subject: String,
): UserEvent
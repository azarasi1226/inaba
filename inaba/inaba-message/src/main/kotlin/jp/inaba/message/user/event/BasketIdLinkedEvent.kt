package jp.inaba.message.user.event

data class BasketIdLinkedEvent(
    override val id: String,
    val basketId: String,
) : UserEvent

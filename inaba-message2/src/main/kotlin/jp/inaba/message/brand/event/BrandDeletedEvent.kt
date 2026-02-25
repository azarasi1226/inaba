package jp.inaba.message.brand.event

data class BrandDeletedEvent(
    override val id: String,
) : BrandEvent

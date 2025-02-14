package jp.inaba.message.brand.event

data class BrandCreatedEvent(
    override val id: String,
    val name: String,
) : BrandEvent
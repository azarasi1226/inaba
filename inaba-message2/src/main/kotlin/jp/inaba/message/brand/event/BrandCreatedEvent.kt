package jp.inaba.message.brand.event

import jp.inaba.message.InabaEventTag
import org.axonframework.eventsourcing.annotation.EventTag

data class BrandCreatedEvent(
    @EventTag(key = InabaEventTag.BRAND_ID)
    val id: String,
    val name: String,
)

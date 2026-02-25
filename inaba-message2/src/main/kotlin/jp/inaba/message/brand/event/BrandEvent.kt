package jp.inaba.message.brand.event

import jp.inaba.message.InabaEventTag
import org.axonframework.eventsourcing.annotation.EventTag

interface BrandEvent {
    @get:EventTag(key = InabaEventTag.BRAND_ID)
    val id: String
}

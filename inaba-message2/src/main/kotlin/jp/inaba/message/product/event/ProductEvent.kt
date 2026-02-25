package jp.inaba.message.product.event

import jp.inaba.message.InabaEventTag
import org.axonframework.eventsourcing.annotation.EventTag


interface ProductEvent {
  @get:EventTag(key = InabaEventTag.PRODUCT_ID)
  val id: String
}
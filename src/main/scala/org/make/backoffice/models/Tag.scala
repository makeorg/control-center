package org.make.backoffice.models

import io.circe.{Decoder, Encoder, Json}
import org.make.core.StringValue

import scala.scalajs.js

@js.native
trait Tag extends js.Object {
  val id: String
  val label: String
}

object Tag {
  def apply(tagId: TagId, label: String): Tag = js.Dynamic.literal(id = tagId.value, label = label).asInstanceOf[Tag]
}

@js.native
trait TagId extends js.Object with StringValue {
  override val value: String
}

object TagId {
  def apply(value: String): TagId = js.Dynamic.literal(value = value).asInstanceOf[TagId]

  implicit lazy val tagIdEncoder: Encoder[TagId] = (a: TagId) => Json.fromString(a.value)
  implicit lazy val tagIdDecoder: Decoder[TagId] = Decoder.decodeString.map(TagId(_))
}

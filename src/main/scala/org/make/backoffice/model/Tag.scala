package org.make.backoffice.model

import io.circe.{Decoder, Encoder, Json}

import scala.scalajs.js
import js.JSConverters._

@js.native
trait TagTypeId extends js.Object with StringValue {
  override val value: String
}

object TagTypeId {
  def apply(value: String): TagTypeId = js.Dynamic.literal(value = value).asInstanceOf[TagTypeId]

  implicit lazy val tagTypeIdEncoder: Encoder[TagTypeId] = (a: TagTypeId) => Json.fromString(a.value)
  implicit lazy val tagTypeIdDecoder: Decoder[TagTypeId] = Decoder.decodeString.map(TagTypeId(_))
}

@js.native
trait TagType extends js.Object {
  val tagTypeId: String
  val label: String
  val display: String
  val weight: Float
}

object TagType {
  def apply(tagTypeId: TagTypeId, label: String, display: String, weight: Float): TagType =
    js.Dynamic
      .literal(tagTypeId = tagTypeId.value, label = label, display = display, weight = weight)
      .asInstanceOf[TagType]
}

@js.native
trait TagTypeResponse extends js.Object {
  val id: String
  val label: String
  val display: String
  val weight: Float
}

object TagTypeResponse {
  def apply(id: TagTypeId, label: String, display: String, weight: Float): TagTypeResponse =
    js.Dynamic
      .literal(id = id.value, label = label, display = display, weight = weight)
      .asInstanceOf[TagTypeResponse]

  def toTagType(tagTypeResponse: TagTypeResponse): TagType = {
    TagType(
      tagTypeId = TagTypeId(tagTypeResponse.id),
      label = tagTypeResponse.label,
      display = tagTypeResponse.display,
      weight = tagTypeResponse.weight
    )
  }
}

@js.native
trait Tag extends js.Object {
  val id: String
  val label: String
  val display: String
  val tagTypeId: String
  val weight: Float
  val operationId: js.UndefOr[String]
  val themeId: js.UndefOr[String]
  val country: String
  val language: String
}

object Tag {
  def apply(tagId: TagId,
            label: String,
            display: String,
            tagTypeId: TagTypeId,
            weight: Float,
            operationId: Option[OperationId],
            themeId: Option[ThemeId],
            country: String,
            language: String): Tag =
    js.Dynamic
      .literal(
        id = tagId.value,
        label = label,
        display = display,
        tagTypeId = tagTypeId.value,
        weight = weight,
        operationId = operationId.map(_.value).orUndefined,
        themeId = themeId.map(_.value).orUndefined,
        country = country,
        language = language
      )
      .asInstanceOf[Tag]
}

@js.native
trait TagResponse extends js.Object {
  val id: String
  val label: String
  val display: String
  val tagTypeId: String
  val weight: Float
  val operationId: js.UndefOr[String]
  val themeId: js.UndefOr[String]
  val country: String
  val language: String
}

object TagResponse {
  def apply(id: TagId,
            label: String,
            display: String,
            tagTypeId: TagTypeId,
            weight: Float,
            operationId: Option[OperationId],
            themeId: Option[ThemeId],
            country: String,
            language: String): TagResponse =
    js.Dynamic
      .literal(
        id = id.value,
        label = label,
        display = display,
        tagTypeId = tagTypeId.value,
        weight = weight,
        operationId = operationId.map(_.value).orUndefined,
        themeId = themeId.map(_.value).orUndefined,
        country = country,
        language = language
      )
      .asInstanceOf[TagResponse]

  def toTag(tagResponse: TagResponse): Tag = {
    Tag(
      tagId = TagId(tagResponse.id),
      label = tagResponse.label,
      display = tagResponse.display,
      tagTypeId = TagTypeId(tagResponse.tagTypeId),
      weight = tagResponse.weight,
      operationId = tagResponse.operationId.toOption.map(OperationId(_)),
      themeId = tagResponse.themeId.toOption.map(ThemeId(_)),
      country = tagResponse.country,
      language = tagResponse.language
    )
  }
}

@js.native
trait IndexedTag extends js.Object {
  val id: String
  val label: String
  val display: Boolean
}

object IndexedTag {
  def apply(tagId: TagId, label: String, display: Boolean): IndexedTag =
    js.Dynamic.literal(id = tagId.value, label = label, display = display).asInstanceOf[IndexedTag]
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

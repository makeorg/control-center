/*
 * Make.org Control Center
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

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
}

object TagType {
  def apply(tagTypeId: TagTypeId, label: String, display: String): TagType =
    js.Dynamic.literal(tagTypeId = tagTypeId.value, label = label, display = display).asInstanceOf[TagType]
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

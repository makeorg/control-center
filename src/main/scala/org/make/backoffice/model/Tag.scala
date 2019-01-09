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
  val id: String
  val label: String
  val display: String
  val weight: Float
}

object TagType {
  def apply(id: TagTypeId, label: String, display: String, weight: Float): TagType =
    js.Dynamic
      .literal(id = id.value, label = label, display = display, weight = weight)
      .asInstanceOf[TagType]
}

@js.native
trait Tag extends js.Object {
  val id: String
  val label: String
  val display: String
  val tagTypeId: String
  val weight: Float
  val questionId: js.UndefOr[String]
}

object Tag {
  def apply(id: TagId,
            label: String,
            display: String,
            tagTypeId: TagTypeId,
            weight: Float,
            questionId: Option[QuestionId]): Tag =
    js.Dynamic
      .literal(
        id = id.value,
        label = label,
        display = display,
        tagTypeId = tagTypeId.value,
        weight = weight,
        questionId = questionId.map(_.value).orUndefined
      )
      .asInstanceOf[Tag]
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

@js.native
trait PredictedTag extends js.Object {
  val id: String
  val label: String
  val tagTypeId: String
  val weight: Float
  val questionId: js.UndefOr[String]
  val checked: Boolean
  val predicted: Boolean

}

object PredictedTag {
  def apply(id: TagId,
            label: String,
            tagTypeId: TagTypeId,
            weight: Float,
            questionId: Option[QuestionId],
            checked: Boolean,
            predicted: Boolean): PredictedTag =
    js.Dynamic
      .literal(
        id = id.value,
        label = label,
        tagTypeId = tagTypeId.value,
        weight = weight,
        questionId = questionId.map(_.value).orUndefined,
        checked = checked,
        predicted = predicted
      )
      .asInstanceOf[PredictedTag]

  implicit lazy val decoder: Decoder[PredictedTag] =
    Decoder.forProduct7("id", "label", "tagTypeId", "weight", "questionId", "checked", "predicted")(PredictedTag.apply)
}

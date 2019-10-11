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
import scala.scalajs.js.JSConverters._
@js.native
trait Feature extends js.Object {
  val id: String
  val slug: String
  val name: String
}

@js.native
trait ActiveFeature extends js.Object {
  val activeFeatureId: String
  val featureId: String
  val maybeQuestionId: js.UndefOr[String]
}

object ActiveFeature {
  def apply(activeFeatureId: ActiveFeatureId,
            featureId: FeatureId,
            maybeQuestionId: Option[QuestionId]): ActiveFeature = {
    js.Dynamic
      .literal(
        activeFeatureId = activeFeatureId.value,
        featureId = featureId.value,
        maybeQuestionId = maybeQuestionId.map(_.value).orUndefined
      )
      .asInstanceOf[ActiveFeature]
  }
}

@js.native
trait ActiveFeatureId extends js.Object with StringValue {
  val value: String
}

object ActiveFeatureId {
  def apply(value: String): ActiveFeatureId = js.Dynamic.literal(value = value).asInstanceOf[ActiveFeatureId]

  implicit lazy val activeFeatureIdEncoder: Encoder[ActiveFeatureId] = (a: ActiveFeatureId) => Json.fromString(a.value)
  implicit lazy val activeFeatureIdDecoder: Decoder[ActiveFeatureId] = Decoder.decodeString.map(ActiveFeatureId(_))
}

@js.native
trait FeatureId extends js.Object with StringValue {
  val value: String
}

object FeatureId {
  def apply(value: String): FeatureId = js.Dynamic.literal(value = value).asInstanceOf[FeatureId]

  implicit lazy val featureIdEncoder: Encoder[FeatureId] = (a: FeatureId) => Json.fromString(a.value)
  implicit lazy val featureIdDecoder: Decoder[FeatureId] = Decoder.decodeString.map(FeatureId(_))
}

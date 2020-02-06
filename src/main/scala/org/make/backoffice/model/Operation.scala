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

import java.time.ZonedDateTime

import io.circe.{Decoder, Encoder, Json}
import org.make.backoffice.facade.Choice
import org.make.backoffice.util.JSConverters._

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.scalajs.js.JSConverters._

@js.native
trait OperationId extends js.Object with StringValue {
  val value: String
}

object OperationId {
  def apply(value: String): OperationId = js.Dynamic.literal(value = value).asInstanceOf[OperationId]

  implicit lazy val operationIdEncoder: Encoder[OperationId] = (a: OperationId) => Json.fromString(a.value)
  implicit lazy val operationIdDecoder: Decoder[OperationId] = Decoder.decodeString.map(OperationId(_))
}

@js.native
trait Operation extends js.Object {
  val id: String
  val status: String
  val slug: String
  val defaultLanguage: String
  val operationKind: String
  val createdAt: js.UndefOr[Date]
  val updatedAt: js.UndefOr[Date]
}

object Operation {
  def apply(operationId: OperationId,
            status: String,
            slug: String,
            defaultLanguage: String,
            createdAt: Option[ZonedDateTime],
            updatedAt: Option[ZonedDateTime]): Operation =
    js.Dynamic
      .literal(
        id = operationId.value,
        status = status,
        slug = slug,
        defaultLanguage = defaultLanguage,
        createdAt = createdAt.orUndefined.map(_.toJSDate),
        updatedAt = updatedAt.orUndefined.map(_.toJSDate)
      )
      .asInstanceOf[Operation]

  val allowedSourcesChoices: js.Array[Choice] = js.Array(Choice("core", "core"), Choice("huffpost", "Huffington Post"))

  val kindMap: Map[String, String] = Map(
    "PUBLIC_CONSULTATION" -> "Public consultation",
    "GREAT_CAUSE" -> "Great cause",
    "PRIVATE_CONSULTATION" -> "Private consultation",
    "BUSINESS_CONSULTATION" -> "Business consultation"
  )

  val kindChoices: js.Array[Choice] =
    kindMap.map {
      case (id, name) => Choice(id, name)
    }.toJSArray
}

@js.native
trait OperationsResult extends js.Object {
  val total: Int
  val results: js.Array[Operation]
}

object OperationsResult {
  def apply(total: Int, results: Seq[Operation]): OperationsResult =
    js.Dynamic.literal(total = total, results = results.toJSArray).asInstanceOf[OperationsResult]
}

@js.native
trait FeaturedOperationId extends js.Object with StringValue {
  val value: String
}

object FeaturedOperationId {
  def apply(value: String): FeaturedOperationId = js.Dynamic.literal(value = value).asInstanceOf[FeaturedOperationId]

  implicit lazy val featuredOperationIdEncoder: Encoder[FeaturedOperationId] = (a: FeaturedOperationId) =>
    Json.fromString(a.value)
  implicit lazy val featuredOperationIdDecoder: Decoder[FeaturedOperationId] =
    Decoder.decodeString.map(FeaturedOperationId(_))
}

@js.native
trait FeaturedOperationIdResult extends js.Object with StringValue {
  val featuredOperationId: FeaturedOperationId
}

object FeaturedOperationIdResult {
  def apply(featuredOperationId: String): FeaturedOperationIdResult =
    js.Dynamic
      .literal(featuredOperationId = FeaturedOperationId(featuredOperationId))
      .asInstanceOf[FeaturedOperationIdResult]
}

@js.native
trait FeaturedOperation extends js.Object {
  val id: String
  val questionId: js.UndefOr[String]
  val title: String
  val description: js.UndefOr[String]
  val landscapePicture: String
  val portraitPicture: String
  val altPicture: String
  val label: String
  val buttonLabel: String
  val internalLink: js.UndefOr[String]
  val externalLink: js.UndefOr[String]
  val slot: Int
}

object FeaturedOperation {
  def apply(id: FeaturedOperationId,
            questionId: Option[QuestionId],
            title: String,
            description: Option[String],
            landscapePicture: String,
            portraitPicture: String,
            altPicture: String,
            label: String,
            buttonLabel: String,
            internalLink: Option[String],
            externalLink: Option[String],
            slot: Int): FeaturedOperation =
    js.Dynamic
      .literal(
        id = id.value,
        questionId = questionId.map(_.value).orUndefined,
        title = title,
        description = description.orUndefined,
        landscapePicture = landscapePicture,
        portraitPicture = portraitPicture,
        altPicture = altPicture,
        label = label,
        buttonLabel = buttonLabel,
        internalLink = internalLink.orUndefined,
        externalLink = externalLink.orUndefined,
        slot = slot
      )
      .asInstanceOf[FeaturedOperation]

  val internalLinkMap: Map[String, String] = {
    Map("CONSULTATION" -> "Consultation", "SELECTION" -> "Selection", "ACTIONS" -> "Actions", "RESULTS" -> "Results")
  }
}

@js.native
trait CurrentOperationId extends js.Object with StringValue {
  val value: String
}

object CurrentOperationId {
  def apply(value: String): CurrentOperationId = js.Dynamic.literal(value = value).asInstanceOf[CurrentOperationId]

  implicit lazy val featuredOperationIdEncoder: Encoder[CurrentOperationId] = (a: CurrentOperationId) =>
    Json.fromString(a.value)
  implicit lazy val featuredOperationIdDecoder: Decoder[CurrentOperationId] =
    Decoder.decodeString.map(CurrentOperationId(_))
}

@js.native
trait CurrentOperationIdResult extends js.Object with StringValue {
  val currentOperationId: CurrentOperationId
}

object CurrentOperationIdResult {
  def apply(currentOperationId: String): CurrentOperationIdResult =
    js.Dynamic
      .literal(currentOperationId = CurrentOperationId(currentOperationId))
      .asInstanceOf[CurrentOperationIdResult]
}

@js.native
trait CurrentOperation extends js.Object {
  val id: String
  val questionId: String
  val description: String
  val label: String
  val picture: String
  val altPicture: String
  val linkLabel: String
  val internalLink: js.UndefOr[String]
  val externalLink: js.UndefOr[String]
}

object CurrentOperation {
  def apply(id: CurrentOperationId,
            questionId: QuestionId,
            description: String,
            label: String,
            picture: String,
            altPicture: String,
            linkLabel: String,
            internalLink: Option[String],
            externalLink: Option[String]): CurrentOperation =
    js.Dynamic
      .literal(
        id = id.value,
        questionId = questionId.value,
        description = description,
        label = label,
        picture = picture,
        altPicture = altPicture,
        linkLabel = linkLabel,
        internalLink = internalLink.orUndefined,
        externalLink = externalLink.orUndefined
      )
      .asInstanceOf[CurrentOperation]
}

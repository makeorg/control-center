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
import org.make.backoffice.util.JSConverters._

import scala.scalajs.js
import scala.scalajs.js.{Date, UndefOr}
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
trait OperationTranslation extends js.Object with StringValue {
  val title: String
  val language: String
}

object OperationTranslation {
  def apply(title: String, language: String): OperationTranslation =
    js.Dynamic.literal(title = title, language = language).asInstanceOf[OperationTranslation]
}

@js.native
trait OperationCountryConfiguration extends js.Object with StringValue {
  val countryCode: String
  val tagIds: js.Array[TagId]
  val startDate: UndefOr[js.Date]
  val endDate: UndefOr[js.Date]
}

object OperationCountryConfiguration {
  def apply(countryCode: String,
            tagIds: Seq[TagId],
            startDate: Option[String],
            endDate: Option[String]): OperationCountryConfiguration =
    js.Dynamic
      .literal(
        countryCode = countryCode,
        tagIds = tagIds.toJSArray,
        startDate = startDate.map(d => new js.Date(d)).orUndefined,
        endDate = endDate.map(d     => new js.Date(d)).orUndefined
      )
      .asInstanceOf[OperationCountryConfiguration]
}

@js.native
trait Operation extends js.Object {
  val id: String
  val status: String
  val slug: String
  val translations: js.Array[OperationTranslation]
  val defaultLanguage: String
  val sequenceLandingId: String
  val createdAt: js.UndefOr[Date]
  val updatedAt: js.UndefOr[Date]
  val events: js.Array[OperationAction]
  val countriesConfiguration: js.Array[OperationCountryConfiguration]
}

object Operation {
  def apply(operationId: OperationId,
            status: String,
            slug: String,
            translations: Seq[OperationTranslation] = Seq.empty,
            defaultLanguage: String,
            sequenceLandingId: String,
            createdAt: Option[ZonedDateTime],
            updatedAt: Option[ZonedDateTime],
            events: Seq[OperationAction],
            countriesConfiguration: Seq[OperationCountryConfiguration]): Operation =
    js.Dynamic
      .literal(
        id = operationId.value,
        status = status,
        slug = slug,
        translations = translations.toJSArray,
        defaultLanguage = defaultLanguage,
        sequenceLandingId = sequenceLandingId,
        createdAt = createdAt.orUndefined.map(_.toJSDate),
        updatedAt = updatedAt.orUndefined.map(_.toJSDate),
        events = events.toJSArray,
        countriesConfiguration = countriesConfiguration.toJSArray
      )
      .asInstanceOf[Operation]
}

@js.native
trait OperationAction extends js.Object {
  val date: Date
  val user: js.UndefOr[User]
  val actionType: String
  val arguments: js.Dictionary[String]
}

object OperationAction {
  def apply(date: ZonedDateTime,
            user: Option[User],
            actionType: String,
            arguments: Map[String, String]): OperationAction =
    js.Dynamic
      .literal(
        date = date.toJSDate,
        user = user.orUndefined,
        actionType = actionType,
        arguments = arguments.toJSDictionary
      )
      .asInstanceOf[OperationAction]
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

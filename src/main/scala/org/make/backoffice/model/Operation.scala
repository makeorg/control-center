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
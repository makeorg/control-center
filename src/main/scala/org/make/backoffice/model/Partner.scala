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
trait PartnerId extends js.Object with StringValue {
  val value: String
}

object PartnerId {
  def apply(value: String): PartnerId = js.Dynamic.literal(value = value).asInstanceOf[PartnerId]

  implicit lazy val PartnerIdEncoder: Encoder[PartnerId] = (a: PartnerId) => Json.fromString(a.value)
  implicit lazy val PartnerIdDecoder: Decoder[PartnerId] = Decoder.decodeString.map(PartnerId(_))
}

@js.native
trait Partner extends js.Object {
  val id: String
  val organisationId: js.UndefOr[String]
  val name: String
  val logo: js.UndefOr[String]
  val link: js.UndefOr[String]
  val kind: String
  val weight: Double
}

object Partner {

  def apply(id: PartnerId,
            organisationId: Option[UserId],
            name: String,
            logo: Option[String],
            link: Option[String],
            kind: String,
            weight: Double): Partner = {
    js.Dynamic
      .literal(
        id = id.value,
        organisationId = organisationId.map(_.value).orUndefined,
        logo = logo.orUndefined,
        link = link.orUndefined,
        kind = kind,
        weight = weight
      )
      .asInstanceOf[Partner]
  }

  val partnerKindMap: Map[String, String] =
    Map("MEDIA" -> "Media", "FOUNDER" -> "Founder", "ACTION_PARTNER" -> "Action partner")
}

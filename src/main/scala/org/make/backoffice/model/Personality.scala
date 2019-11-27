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

@js.native
trait PersonalityId extends js.Object with StringValue {
  val value: String
}

object PersonalityId {
  def apply(value: String): PersonalityId = js.Dynamic.literal(value = value).asInstanceOf[PersonalityId]

  implicit lazy val PersonalityIdEncoder: Encoder[PersonalityId] = (a: PersonalityId) => Json.fromString(a.value)
  implicit lazy val PersonalityIdDecoder: Decoder[PersonalityId] = Decoder.decodeString.map(PersonalityId(_))
}

@js.native
trait Personality extends js.Object {
  val id: String
  val userId: String
  val personalityRole: String
}

object Personality {

  def apply(id: PersonalityId, userId: UserId, personalityRole: String): Personality = {
    js.Dynamic
      .literal(id = id.value, userId = userId.value, personalityRole = personalityRole)
      .asInstanceOf[Personality]
  }

  val personalityRoleMap: Map[String, String] =
    Map("CANDIDATE" -> "Candidate")
}

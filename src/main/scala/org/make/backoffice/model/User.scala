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

import io.circe._
import org.make.backoffice.util.JSConverters._

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.scalajs.js.JSConverters._

@js.native
sealed trait Role extends js.Object {
  def shortName: String
}

object Role {
  def apply(shortName: String): Role = js.Dynamic.literal(shortName = shortName).asInstanceOf[Role]

  val roleAdmin: Role = Role("ROLE_ADMIN")
  val roleModerator: Role = Role("ROLE_MODERATOR")
  val rolePolitical: Role = Role("ROLE_POLITICAL")
  val roleCitizen: Role = Role("ROLE_CITIZEN")
  val roleActor: Role = Role("ROLE_ACTOR")

  val roles: Map[String, Role] = Map(
    roleAdmin.shortName -> roleAdmin,
    roleModerator.shortName -> roleModerator,
    rolePolitical.shortName -> rolePolitical,
    roleCitizen.shortName -> roleCitizen,
    roleActor.shortName -> roleActor
  )

  def matchRole(role: String): Option[Role] = {
    val maybeRole = roles.get(role)
    maybeRole
  }
}

@js.native
trait CurrentUser extends js.Object {
  val roles: js.Array[Role]
}

object CurrentUser {
  def apply(roles: Seq[Role]): CurrentUser =
    js.Dynamic.literal(roles = roles.toJSArray).asInstanceOf[CurrentUser]
}

@js.native
trait User extends js.Object {
  val userId: UserId
  val email: String
  val firstName: js.UndefOr[String]
  val lastName: js.UndefOr[String]
  val organisationName: js.UndefOr[String]
  val enabled: Boolean
  val emailVerified: Boolean
  val isOrganisation: Boolean
  val lastConnection: Date
  val roles: js.Array[Role]
  val profile: js.UndefOr[Profile]
}

object User {
  def apply(userId: UserId,
            email: String,
            firstName: Option[String],
            lastName: Option[String],
            organisationName: Option[String],
            enabled: Boolean,
            emailVerified: Boolean,
            isOrganisation: Boolean,
            lastConnection: ZonedDateTime,
            roles: Seq[Role],
            profile: Option[Profile]): User =
    js.Dynamic
      .literal(
        userId = userId,
        email = email,
        firstName = firstName.orUndefined,
        lastName = lastName.orUndefined,
        organisationName = organisationName.orUndefined,
        enabled = enabled,
        emailVerified = emailVerified,
        isOrganisation = isOrganisation,
        lastConnection = lastConnection.toJSDate, // Date.parse(lastConnection.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)),
        roles = roles.toJSArray,
        profile = profile.orUndefined
      )
      .asInstanceOf[User]
}

@js.native
trait SimpleUser extends js.Object {
  val id: String
  val email: String
  val firstName: js.UndefOr[String]
  val lastName: js.UndefOr[String]
  val fullName: js.UndefOr[String]
}

object SimpleUser {
  def apply(id: UserId, email: String, firstName: Option[String], lastName: Option[String]): SimpleUser = {
    js.Dynamic
      .literal(
        id = id.value,
        email = email,
        firstName = firstName.orUndefined,
        lastName = lastName.orUndefined,
        fullName = getFullName(firstName, lastName).orUndefined
      )
      .asInstanceOf[SimpleUser]
  }

  def getFullName(firstName: Option[String], lastName: Option[String]): Option[String] = {
    (firstName, lastName) match {
      case (None, None)                      => None
      case (firstName, None)                 => firstName
      case (None, lastName)                  => lastName
      case (Some(firstName), Some(lastName)) => Some(s"$firstName $lastName")
    }
  }

}

@js.native
trait UserId extends js.Object with StringValue
object UserId {
  def apply(value: String): UserId = js.Dynamic.literal(value = value).asInstanceOf[UserId]

  implicit lazy val userIdEncoder: Encoder[UserId] = (a: UserId) => Json.fromString(a.value)
  implicit lazy val userIdDecoder: Decoder[UserId] = Decoder.decodeString.map(UserId(_))

}

@js.native
trait Organisation extends js.Object {
  val id: js.UndefOr[String]
  val organisationName: String
  val profile: js.UndefOr[Profile]
}

object Organisation {
  def apply(id: Option[UserId], maybeOrganisationName: Option[String], profile: Option[Profile]): Organisation = {
    val organisationName = maybeOrganisationName.getOrElse("")
    js.Dynamic
      .literal(id = id.map(_.value).orUndefined, organisationName = organisationName, profile = profile.orUndefined)
      .asInstanceOf[Organisation]
  }
}

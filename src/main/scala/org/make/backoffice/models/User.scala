package org.make.backoffice.models

import java.time.ZonedDateTime

import io.circe._
import org.make.core.JSConverters._
import org.make.core.StringValue

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.scalajs.js.JSConverters._

@js.native
sealed trait Role extends js.Object {
  def shortName: String
}

object Role {
  def apply(shortName: String): Role = js.Dynamic.literal(shortName = shortName).asInstanceOf[Role]

  val roleAdmin = Role("ROLE_ADMIN")
  val roleModerator = Role("ROLE_MODERATOR")
  val rolePolitical = Role("ROLE_POLITICAL")
  val roleCitizen = Role("ROLE_CITIZEN")

  val roles: Map[String, Role] = Map(
    roleAdmin.shortName -> roleAdmin,
    roleModerator.shortName -> roleModerator,
    rolePolitical.shortName -> rolePolitical,
    roleCitizen.shortName -> roleCitizen
  )

  def matchRole(role: String): Option[Role] = {
    val maybeRole = roles.get(role)
    maybeRole
  }

}

@js.native
trait User extends js.Object {
  val userId: UserId
  val email: String
  val firstName: js.UndefOr[String]
  val lastName: js.UndefOr[String]
  val enabled: Boolean
  val verified: Boolean
  val lastConnection: Date
  val roles: Seq[Role]
  val profile: js.UndefOr[Profile]
}
object User {
  def apply(userId: UserId,
            email: String,
            firstName: Option[String],
            lastName: Option[String],
            enabled: Boolean,
            verified: Boolean,
            lastConnection: ZonedDateTime,
            roles: Seq[Role],
            profile: Option[Profile]): User =
    js.Dynamic
      .literal(
        userId = userId,
        email = email,
        firstName = firstName.orUndefined,
        lastName = lastName.orUndefined,
        enabled = enabled,
        verified = verified,
        lastConnection = lastConnection.toJSDate, // Date.parse(lastConnection.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)),
        roles = roles,
        profile = profile.orUndefined
      )
      .asInstanceOf[User]
}

@js.native
trait UserId extends js.Object with StringValue
object UserId {
  def apply(value: String): UserId = js.Dynamic.literal(value = value).asInstanceOf[UserId]

  implicit lazy val userIdEncoder: Encoder[UserId] = (a: UserId) => Json.fromString(a.value)
  implicit lazy val userIdDecoder: Decoder[UserId] = Decoder.decodeString.map(UserId(_))

}

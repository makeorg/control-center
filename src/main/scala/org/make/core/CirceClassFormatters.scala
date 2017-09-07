package org.make.core

import io.circe.java8.time.TimeInstances
import io.circe.{Decoder, Encoder, Json}
import org.make.backoffice.models._

trait CirceClassFormatters extends TimeInstances {
  implicit lazy val userDecoder: Decoder[User] =
    Decoder.forProduct9(
      "userId",
      "email",
      "firstName",
      "lastName",
      "enabled",
      "verified",
      "lastConnection",
      "roles",
      "profile"
    )(User.apply)

  implicit lazy val roleEncoder: Encoder[Role] = (role: Role) => Json.fromString(role.shortName)
  implicit lazy val roleDecoder: Decoder[Role] =
    Decoder.decodeString.emap(
      maybeRole =>
        Role.matchRole(maybeRole) match {
          case Some(role) => Right(role)
          case _          => Left(s"$maybeRole is not a role")
      }
    )

  implicit lazy val tokenDecoder: Decoder[Token] =
    Decoder.forProduct4("token_type", "access_token", "expires_in", "refresh_token")(Token.apply)

  implicit lazy val profileDecoder: Decoder[Profile] =
    Decoder.forProduct13(
      "dateOfBirth",
      "avatarUrl",
      "profession",
      "phoneNumber",
      "twitterId",
      "facebookId",
      "googleId",
      "gender",
      "genderName",
      "departmentNumber",
      "karmaLevel",
      "locale",
      "optInNewsletter"
    )(Profile.apply)

  implicit lazy val genderEncoder: Encoder[Gender] = (gender: Gender) => Json.fromString(gender.shortName)
  implicit lazy val genderDecoder: Decoder[Gender] =
    Decoder.decodeString.emap(
      maybeGender =>
        Gender.matchGender(maybeGender) match {
          case Some(gender) => Right(gender)
          case _            => Left(s"$maybeGender is not a gender")
      }
    )

  implicit lazy val proposalDecoder: Decoder[Proposal] =
    Decoder.forProduct18(
      "id",
      "userId",
      "content",
      "slug",
      "status",
      "createdAt",
      "updatedAt",
      "votesAgree",
      "votesDisagree",
      "votesNeutral",
      "proposalContext",
      "trending",
      "labels",
      "author",
      "country",
      "language",
      "themeId",
      "tags"
    )(Proposal.apply)

  implicit lazy val voteDecoder: Decoder[Vote] =
    Decoder.forProduct4("key", "selected", "count", "qualifications")(Vote.apply)

  implicit lazy val qualificationDecoder: Decoder[Qualification] =
    Decoder.forProduct3("key", "count", "selected")(Qualification.apply)

  implicit lazy val contextDecoder: Decoder[ProposalContext] =
    Decoder.forProduct4("operation", "source", "location", "question")(ProposalContext.apply)

  implicit lazy val authorDecoder: Decoder[Author] = Decoder.forProduct3("firstName", "postalCode", "age")(Author.apply)

  implicit lazy val tagDecoder: Decoder[Tag] = Decoder.forProduct2("tagId", "label")(Tag.apply)
}

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

package org.make.backoffice.util

import io.circe.java8.time.TimeInstances
import io.circe.{Decoder, Encoder, Json}
import org.make.backoffice.model._

trait CirceClassFormatters extends TimeInstances {
  implicit lazy val userDecoder: Decoder[User] =
    Decoder.forProduct11(
      "userId",
      "email",
      "firstName",
      "lastName",
      "organisationName",
      "enabled",
      "emailVerified",
      "isOrganisation",
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
      "postalCode",
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
    Decoder.forProduct14(
      "id",
      "userId",
      "content",
      "slug",
      "status",
      "createdAt",
      "updatedAt",
      "votes",
      "author",
      "tags",
      "ideaId",
      "questionId",
      "toEnrich",
      "initialProposal"
    )(Proposal.apply)

  implicit lazy val ideaDecoder: Decoder[Idea] =
    Decoder.forProduct5("ideaId", "name", "questionId", "createdAt", "updatedAt")(Idea.apply)

  implicit lazy val ideasResultDecoder: Decoder[IdeasResult] =
    Decoder.forProduct2("total", "results")(IdeasResult.apply)

  implicit lazy val proposalsResultDecoder: Decoder[ProposalsResult] =
    Decoder.forProduct2("total", "results")(ProposalsResult.apply)

  implicit lazy val operationsResultDecoder: Decoder[OperationsResult] =
    Decoder.forProduct2("total", "results")(OperationsResult.apply)

  implicit lazy val similarResultDecoder: Decoder[SimilarResult] =
    Decoder.forProduct2("ideaId", "ideaName")(SimilarResult.apply)

  implicit lazy val voteDecoder: Decoder[Vote] =
    Decoder.forProduct3("key", "count", "qualifications")(Vote.apply)

  implicit lazy val qualificationDecoder: Decoder[Qualification] =
    Decoder.forProduct2("key", "count")(Qualification.apply)

  implicit lazy val authorDecoder: Decoder[Author] =
    Decoder.forProduct3("firstName", "postalCode", "age")(Author.apply)

  implicit lazy val tagTypeDecoder: Decoder[TagType] =
    Decoder.forProduct4("id", "label", "display", "weight")(TagType.apply)

  implicit lazy val tagEncoder: Encoder[Tag] =
    Encoder.forProduct6("id", "label", "display", "tagTypeId", "weight", "questionId")(
      tag => (tag.id, tag.label, tag.display, tag.tagTypeId, tag.weight, tag.questionId.toOption)
    )

  implicit lazy val tagDecoder: Decoder[Tag] =
    Decoder.forProduct6("id", "label", "display", "tagTypeId", "weight", "questionId")(Tag.apply)

  implicit lazy val indexedTagDecoder: Decoder[IndexedTag] =
    Decoder.forProduct3("tagId", "label", "display")(IndexedTag.apply)

  implicit lazy val singleProposalDecoder: Decoder[SingleProposal] =
    Decoder.forProduct19(
      "proposalId",
      "slug",
      "content",
      "author",
      "status",
      "refusalReason",
      "tags",
      "votes",
      "context",
      "country",
      "language",
      "createdAt",
      "updatedAt",
      "events",
      "similarProposals",
      "idea",
      "ideaProposals",
      "operationId",
      "questionId"
    )(SingleProposal.apply)

  implicit lazy val proposalActionDecoder: Decoder[ProposalAction] =
    Decoder.forProduct4("date", "user", "actionType", "arguments")(ProposalAction.apply)

  implicit lazy val requestContextDecoder: Decoder[RequestContext] =
    Decoder.forProduct9(
      "requestId",
      "sessionId",
      "externalId",
      "country",
      "language",
      "operation",
      "source",
      "location",
      "question"
    )(RequestContext.apply)

  implicit lazy val countryConfigDecoder: Decoder[CountryConfiguration] =
    Decoder.forProduct3("countryCode", "defaultLanguage", "supportedLanguages")(CountryConfiguration.apply)

  implicit lazy val operationDecoder: Decoder[Operation] =
    Decoder.forProduct6("operationId", "status", "slug", "defaultLanguage", "createdAt", "updatedAt")(Operation.apply)

  implicit lazy val questionDecoder: Decoder[Question] =
    Decoder.forProduct10(
      "id",
      "question",
      "slug",
      "operationId",
      "startDate",
      "endDate",
      "landingSequenceId",
      "operationTitle",
      "country",
      "language"
    )(Question.apply)
}

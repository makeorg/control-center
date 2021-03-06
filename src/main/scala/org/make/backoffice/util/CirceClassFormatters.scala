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

  implicit lazy val simpleUserDecoder: Decoder[SimpleUser] =
    Decoder.forProduct4("id", "email", "firstName", "lastName")(SimpleUser.apply)

  implicit lazy val currentUserDecoder: Decoder[CurrentUser] =
    Decoder.forProduct1("roles")(CurrentUser.apply)

  implicit lazy val roleEncoder: Encoder[Role] = role => Json.fromString(role.shortName)
  implicit lazy val roleDecoder: Decoder[Role] =
    Decoder.decodeString.map(role => Role.roles.getOrElse(role, Role(role)))

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

  implicit lazy val proposalIdResultDecoder: Decoder[ProposalIdResult] =
    Decoder.forProduct1("proposalId")(ProposalIdResult.apply)

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

  implicit lazy val ideaStatusEncoder: Encoder[IdeaStatus] = (status: IdeaStatus) => Json.fromString(status.shortName)
  implicit lazy val ideaStatusDecoder: Decoder[IdeaStatus] =
    Decoder.decodeString.emap(
      ideaStatus =>
        IdeaStatus.matchIdeaStatus(ideaStatus) match {
          case Some(status) => Right(status)
          case _            => Left(s"$ideaStatus is not an idea status")
      }
    )

  implicit lazy val ideaDecoder: Decoder[Idea] =
    Decoder.forProduct4("id", "name", "questionId", "status")(Idea.apply)

  implicit lazy val ideasResultDecoder: Decoder[IdeasResult] =
    Decoder.forProduct2("total", "results")(IdeasResult.apply)

  implicit lazy val proposalsResultDecoder: Decoder[ProposalsResult] =
    Decoder.forProduct2("total", "results")(ProposalsResult.apply)

  implicit lazy val voteDecoder: Decoder[Vote] =
    Decoder.forProduct3("key", "count", "qualifications")(Vote.apply)

  implicit lazy val qualificationDecoder: Decoder[Qualification] =
    Decoder.forProduct2("key", "count")(Qualification.apply)

  implicit lazy val authorDecoder: Decoder[Author] =
    Decoder.forProduct5("firstName", "displayName", "postalCode", "age", "userType")(Author.apply)

  implicit lazy val tagTypeDecoder: Decoder[TagType] =
    Decoder.forProduct5("id", "label", "display", "weight", "requiredForEnrichment")(TagType.apply)

  implicit lazy val tagEncoder: Encoder[Tag] =
    Encoder.forProduct6("id", "label", "display", "tagTypeId", "weight", "questionId")(
      tag => (tag.id, tag.label, tag.display, tag.tagTypeId, tag.weight, tag.questionId.toOption)
    )

  implicit lazy val tagDecoder: Decoder[Tag] =
    Decoder.forProduct6("id", "label", "display", "tagTypeId", "weight", "questionId")(Tag.apply)

  implicit lazy val indexedTagDecoder: Decoder[IndexedTag] =
    Decoder.forProduct3("tagId", "label", "display")(IndexedTag.apply)

  implicit lazy val singleProposalAuthorDecoder: Decoder[SingleProposalAuthor] =
    Decoder.forProduct9(
      "userId",
      "firstName",
      "lastName",
      "displayName",
      "postalCode",
      "age",
      "avatarUrl",
      "organisationName",
      "organisationSlug"
    )(SingleProposalAuthor.apply)

  implicit lazy val singleProposalDecoder: Decoder[SingleProposal] =
    Decoder.forProduct17(
      "proposalId",
      "slug",
      "content",
      "author",
      "status",
      "refusalReason",
      "tags",
      "votes",
      "context",
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

  implicit lazy val proposalActionAuthorDecoder: Decoder[ProposalActionAuthor] =
    Decoder.forProduct2("id", "displayName")(ProposalActionAuthor.apply)

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

  implicit lazy val questionDecoder: Decoder[Question] =
    Decoder.forProduct12(
      "id",
      "question",
      "slug",
      "operationId",
      "startDate",
      "endDate",
      "operationTitle",
      "countries",
      "language",
      "consultationImage",
      "descriptionImage",
      "resultsLink"
    )(Question.apply)

  implicit lazy val resultsLinkDecoder: Decoder[ResultsLink] =
    Decoder.forProduct2("kind", "value")(ResultsLink.apply)
}

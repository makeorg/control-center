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
      "votes",
      "context",
      "trending",
      "labels",
      "author",
      "country",
      "language",
      "themeId",
      "tags",
      "ideaId",
      "operationId"
    )(Proposal.apply)

  implicit lazy val ideaDecoder: Decoder[Idea] =
    Decoder.forProduct8("ideaId", "name", "language", "country", "operationId", "question", "createdAt", "updatedAt")(
      Idea.apply
    )

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

  implicit lazy val contextDecoder: Decoder[Context] =
    Decoder.forProduct4("operation", "source", "location", "question")(Context.apply)

  implicit lazy val authorDecoder: Decoder[Author] = Decoder.forProduct3("firstName", "postalCode", "age")(Author.apply)

  implicit lazy val tagEncoder: Encoder[Tag] = Encoder.forProduct2("tagId", "label")(tag => (tag.id, tag.label))
  implicit lazy val tagDecoder: Decoder[Tag] = Decoder.forProduct2("tagId", "label")(Tag.apply)

  implicit lazy val singleProposalDecoder: Decoder[SingleProposal] =
    Decoder.forProduct20(
      "proposalId",
      "slug",
      "content",
      "author",
      "labels",
      "theme",
      "status",
      "refusalReason",
      "tags",
      "votes",
      "context",
      "language",
      "country",
      "createdAt",
      "updatedAt",
      "events",
      "similarProposals",
      "idea",
      "ideaProposals",
      "operationId"
    )(SingleProposal.apply)

  implicit lazy val proposalActionDecoder: Decoder[ProposalAction] =
    Decoder.forProduct4("date", "user", "actionType", "arguments")(ProposalAction.apply)

  implicit lazy val requestContextDecoder: Decoder[RequestContext] =
    Decoder.forProduct10(
      "currentTheme",
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

  implicit lazy val gradientColorEncoder: Encoder[GradientColor] =
    Encoder.forProduct2("from", "to")(gradientColor => (gradientColor.from, gradientColor.to))

  implicit lazy val themeTranslationEncoder: Encoder[ThemeTranslation] =
    Encoder.forProduct3("slug", "title", "language")(
      themeTranslation => (themeTranslation.slug, themeTranslation.title, themeTranslation.language)
    )

  implicit lazy val themeEncoder: Encoder[Theme] =
    Encoder.forProduct8(
      "themeId",
      "translations",
      "actionsCount",
      "proposalsCount",
      "country",
      "color",
      "gradient",
      "tags"
    )(
      theme =>
        (
          theme.themeId,
          theme.translations,
          theme.actionsCount,
          theme.proposalsCount,
          theme.country,
          theme.color,
          theme.gradient.toOption,
          theme.tags
      )
    )

  implicit lazy val businessConfigEncoder: Encoder[BusinessConfig] =
    Encoder.forProduct8(
      "proposalMinLength",
      "proposalMaxLength",
      "themes",
      "nVotesTriggerConnexion",
      "nPendingProposalsTriggerEmailModerator",
      "minProposalsPerSequence",
      "maxProposalsPerSequence",
      "reasonsForRefusal"
    )(
      businessConfig =>
        (
          businessConfig.proposalMinLength,
          businessConfig.proposalMaxLength,
          businessConfig.themes,
          businessConfig.nVotesTriggerConnexion,
          businessConfig.nPendingProposalsTriggerEmailModerator,
          businessConfig.minProposalsPerSequence,
          businessConfig.maxProposalsPerSequence,
          businessConfig.reasonsForRefusal
      )
    )

  implicit lazy val gradientColorDecoder: Decoder[GradientColor] =
    Decoder.forProduct2("from", "to")(GradientColor.apply)

  implicit lazy val themeTranslationDecoder: Decoder[ThemeTranslation] =
    Decoder.forProduct3("slug", "title", "language")(ThemeTranslation.apply)

  implicit lazy val themeDecoder: Decoder[Theme] =
    Decoder.forProduct8(
      "themeId",
      "translations",
      "actionsCount",
      "proposalsCount",
      "country",
      "color",
      "gradient",
      "tags"
    )(Theme.apply)

  implicit lazy val businessConfigDecoder: Decoder[BusinessConfig] =
    Decoder.forProduct8(
      "proposalMinLength",
      "proposalMaxLength",
      "themes",
      "nVotesTriggerConnexion",
      "nPendingProposalsTriggerEmailModerator",
      "minProposalsPerSequence",
      "maxProposalsPerSequence",
      "reasonsForRefusal"
    )(BusinessConfig.apply)

  implicit lazy val operationTranslationDecoder: Decoder[OperationTranslation] =
    Decoder.forProduct2("title", "language")(OperationTranslation.apply)

  implicit lazy val operationCountryConfigurationDecoder: Decoder[OperationCountryConfiguration] =
    Decoder.forProduct2("countryCode", "tagIds")(OperationCountryConfiguration.apply)

  implicit lazy val operationActionDecoder: Decoder[OperationAction] =
    Decoder.forProduct4("date", "user", "actionType", "arguments")(OperationAction.apply)

  implicit lazy val operationDecoder: Decoder[Operation] =
    Decoder.forProduct10(
      "operationId",
      "status",
      "slug",
      "translations",
      "defaultLanguage",
      "sequenceLandingId",
      "createdAt",
      "updatedAt",
      "events",
      "countriesConfiguration"
    )(Operation.apply)
}

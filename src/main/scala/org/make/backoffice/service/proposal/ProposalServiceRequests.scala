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

package org.make.backoffice.service.proposal

import io.circe.{Decoder, Encoder, Json}
import org.make.backoffice.model._

case class SearchFilter(theme: Option[Unit] = None, tag: Option[Unit] = None, content: Option[Unit] = None)

sealed trait Order { val shortName: String }

case object OrderAsc extends Order { override val shortName: String = "ASC" }
case object OrderDesc extends Order { override val shortName: String = "DESC" }

object Order {
  implicit lazy val orderEncoder: Encoder[Order] = (order: Order) => Json.fromString(order.shortName)
  implicit lazy val orderDecoder: Decoder[Order] = Decoder.decodeString.map(
    order => matchOrder(order).getOrElse(throw new IllegalArgumentException(s"$order is not a Order"))
  )

  val orders: Map[String, Order] = Map(OrderAsc.shortName -> OrderAsc, OrderDesc.shortName -> OrderDesc)

  def matchOrder(order: String): Option[Order] =
    orders.get(order.toUpperCase)
}

sealed trait ProposalStatus {
  def shortName: String
}

case object Pending extends ProposalStatus { override val shortName = "Pending" }
case object Postponed extends ProposalStatus { override val shortName = "Postponed" }
case object Accepted extends ProposalStatus { override val shortName = "Accepted" }
case object Refused extends ProposalStatus { override val shortName = "Refused" }
case object Archived extends ProposalStatus { override val shortName = "Archived" }

object ProposalStatus {
  def matchProposalStatus(status: String): Option[ProposalStatus] = statusMap.get(status)

  val statusMap: Map[String, ProposalStatus] =
    Map(
      Pending.shortName -> Pending,
      Postponed.shortName -> Postponed,
      Accepted.shortName -> Accepted,
      Refused.shortName -> Refused,
      Archived.shortName -> Archived
    )

  implicit lazy val proposalStatusEncoder: Encoder[ProposalStatus] = (gender: ProposalStatus) =>
    Json.fromString(gender.shortName)
  implicit lazy val proposalStatusDecoder: Decoder[ProposalStatus] =
    Decoder.decodeString.emap { value: String =>
      statusMap.get(value) match {
        case Some(status) => Right(status)
        case None         => Left(s"$value is not a proposal status")
      }
    }

}

final case class ContextFilterRequest(operation: Option[String] = None,
                                      source: Option[String] = None,
                                      location: Option[String] = None,
                                      question: Option[String] = None)

final case class SortRequest(field: Option[String], direction: Option[Order])

final case class ExhaustiveSearchRequest(proposalIds: Option[Seq[String]] = None,
                                         themesIds: Option[Seq[String]] = None,
                                         tagsIds: Option[Seq[String]] = None,
                                         @Deprecated labelsIds: Option[Seq[String]] = None,
                                         operationId: Option[String] = None,
                                         content: Option[String] = None,
                                         context: Option[ContextFilterRequest] = None,
                                         status: Option[Seq[ProposalStatus]] = None,
                                         sorts: Option[Seq[SortRequest]] = None,
                                         limit: Option[Int] = None,
                                         skip: Option[Int] = None,
                                         country: Option[String])

final case class UpdateProposalRequest(newContent: Option[String],
                                       @Deprecated labels: Seq[String],
                                       tags: Seq[TagId],
                                       similarProposals: Seq[ProposalId],
                                       idea: Option[IdeaId],
                                       questionId: Option[QuestionId],
                                       predictedTags: Option[Seq[TagId]],
                                       predictedTagsModelName: Option[String])

object UpdateProposalRequest {
  implicit lazy val encoder: Encoder[UpdateProposalRequest] = Encoder.forProduct8(
    "newContent",
    "labels",
    "tags",
    "similarProposals",
    "idea",
    "questionId",
    "predictedTags",
    "predictedTagsModelName"
  )(
    updateProposalRequest =>
      (
        updateProposalRequest.newContent,
        updateProposalRequest.labels,
        updateProposalRequest.tags,
        updateProposalRequest.similarProposals,
        updateProposalRequest.idea,
        updateProposalRequest.questionId,
        updateProposalRequest.predictedTags,
        updateProposalRequest.predictedTagsModelName
    )
  )
}

final case class RefuseProposalRequest(sendNotificationEmail: Boolean, refusalReason: Option[String])

object RefuseProposalRequest {
  implicit lazy val encoder: Encoder[RefuseProposalRequest] =
    Encoder.forProduct2("sendNotificationEmail", "refusalReason")(
      refuseProposalRequest => (refuseProposalRequest.sendNotificationEmail, refuseProposalRequest.refusalReason)
    )
}

final case class ValidateProposalRequest(newContent: Option[String],
                                         sendNotificationEmail: Boolean,
                                         @Deprecated labels: Seq[String],
                                         tags: Seq[TagId],
                                         similarProposals: Seq[ProposalId],
                                         idea: Option[IdeaId],
                                         questionId: Option[QuestionId],
                                         predictedTags: Option[Seq[TagId]],
                                         predictedTagsModelName: Option[String])

object ValidateProposalRequest {
  implicit lazy val encoder: Encoder[ValidateProposalRequest] = Encoder.forProduct9(
    "newContent",
    "sendNotificationEmail",
    "labels",
    "tags",
    "similarProposals",
    "idea",
    "questionId",
    "predictedTags",
    "predictedTagsModelName"
  )(
    validateProposalRequest =>
      (
        validateProposalRequest.newContent,
        validateProposalRequest.sendNotificationEmail,
        validateProposalRequest.labels,
        validateProposalRequest.tags,
        validateProposalRequest.similarProposals,
        validateProposalRequest.idea,
        validateProposalRequest.questionId,
        validateProposalRequest.predictedTags,
        validateProposalRequest.predictedTagsModelName
    )
  )
}

final case class PatchProposalsIdeaRequest(proposalIds: Seq[ProposalId], ideaId: IdeaId)

object PatchProposalsIdeaRequest {
  implicit lazy val encoder: Encoder[PatchProposalsIdeaRequest] = Encoder.forProduct2("proposalIds", "ideaId")(
    patchProposalsIdeaRequest => (patchProposalsIdeaRequest.proposalIds, patchProposalsIdeaRequest.ideaId)
  )
}

final case class NextProposalToModerateRequest(questionId: Option[QuestionId],
                                               toEnrich: Boolean,
                                               minVotesCount: Option[String],
                                               minScore: Option[String])

object NextProposalToModerateRequest {
  implicit lazy val encoder: Encoder[NextProposalToModerateRequest] =
    Encoder.forProduct4("questionId", "toEnrich", "minVotesCount", "minScore")(
      nextProposalToModerateRequest =>
        (
          nextProposalToModerateRequest.questionId,
          nextProposalToModerateRequest.toEnrich,
          nextProposalToModerateRequest.minVotesCount,
          nextProposalToModerateRequest.minScore
      )
    )
}

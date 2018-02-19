package org.make.services.proposal

import io.circe.{Decoder, Encoder, Json}
import org.make.backoffice.models._
import org.make.client.request.{Filter, Pagination, Sort}

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}

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
                                         labelsIds: Option[Seq[String]] = None,
                                         operationId: Option[String] = None,
                                         content: Option[String] = None,
                                         context: Option[ContextFilterRequest] = None,
                                         status: Option[Seq[ProposalStatus]] = None,
                                         sorts: Option[Seq[SortRequest]] = None,
                                         limit: Option[Int] = None,
                                         skip: Option[Int] = None,
                                         country: Option[String])

object ExhaustiveSearchRequest {

  def buildExhaustiveSearchRequest(pagination: Option[Pagination],
                                   sort: Option[Sort],
                                   filters: Option[Seq[Filter]]): ExhaustiveSearchRequest =
    ExhaustiveSearchRequest(
      proposalIds = getIdsFromFilters("proposalIds", filters),
      themesIds = getIdsFromFilters("theme", filters),
      tagsIds = getIdsFromFilters("tags", filters),
      labelsIds = getIdsFromFilters("label", filters),
      operationId = getIdFromFilters("operationId", filters),
      content = getContentFromFilters(filters),
      context = getContextFromFilters(filters),
      status = getStatusFromFilters(filters),
      sorts = getSortFromOptionalSort(sort, filters),
      limit = pagination.map(_.perPage),
      skip = pagination.map(page => page.page * page.perPage - page.perPage),
      country = getCountryFromFilters(filters)
    )

  private def getCountryFromFilters(maybeFilters: Option[Seq[Filter]]): Option[String] = {
    maybeFilters.flatMap(_.find(_.field == "country")).map(_.value.toString)
  }

  private def getIdFromFilters(field: String, maybeFilters: Option[Seq[Filter]]): Option[String] = {
    maybeFilters.flatMap(_.find(_.field == field).map(_.value.toString))
  }

  private def getIdsFromFilters(field: String, maybeFilters: Option[Seq[Filter]]): Option[Seq[String]] = {
    maybeFilters.flatMap {
      _.find(_.field == field).flatMap { filter =>
        (filter.value: Any) match {
          case filterListField: js.Array[_] =>
            Some(filterListField.map(_.toString).toSeq)
          case filterField: String => Some(Seq(filterField))
          case unknownFilterType =>
            g.console.warn(s"Unknown filter with value $unknownFilterType")
            None
        }
      }
    }
  }

  private def getContentFromFilters(maybeFilters: Option[Seq[Filter]]): Option[String] =
    maybeFilters.flatMap(_.find(_.field == "content").map(_.value.toString))

  private def getContextFromFilters(maybeFilters: Option[Seq[Filter]]): Option[ContextFilterRequest] =
    Some(
      ContextFilterRequest(
        operation = maybeFilters.flatMap(_.find(_.field == "operation").map(_.value.toString)),
        source = maybeFilters.flatMap(_.find(_.field == "source").map(_.value.toString)),
        location = maybeFilters.flatMap(_.find(_.field == "location").map(_.value.toString)),
        question = maybeFilters.flatMap(_.find(_.field == "question").map(_.value.toString))
      )
    )

  private def getStatusFromFilters(maybeFilters: Option[Seq[Filter]]): Option[Seq[ProposalStatus]] = {
    maybeFilters.flatMap(_.find(_.field == "status")).map(_.value).map {
      case statusList: js.Array[_] =>
        if (statusList.isEmpty) {
          Some(Seq(Pending, Postponed))
        } else {
          Some(
            statusList
              .map(status => ProposalStatus.matchProposalStatus(status.toString).getOrElse(Pending))
              .toSeq
          )
        }
      case unknownFilterType =>
        g.console.warn(s"Unknown filter with value $unknownFilterType")
        None
    }
  }.getOrElse(Some(Seq(Pending, Postponed)))

  def getSortFromOptionalSort(maybeSort: Option[Sort], maybeFilters: Option[Seq[Filter]]): Option[Seq[SortRequest]] = {
    maybeFilters.flatMap(_.find(_.field == "content")) match {
      case Some(_) => None
      case None =>
        maybeSort.flatMap { sort =>
          for {
            _     <- sort.field.toOption
            order <- sort.order.toOption.flatMap(Order.matchOrder)
          } yield Seq(SortRequest(sort.field.toOption, Some(order)))
        }
    }
  }
}

final case class UpdateProposalRequest(newContent: Option[String],
                                       theme: Option[ThemeId],
                                       labels: Seq[String],
                                       tags: Seq[TagId],
                                       similarProposals: Seq[ProposalId],
                                       idea: Option[IdeaId],
                                       operation: Option[OperationId])

final case class RefuseProposalRequest(sendNotificationEmail: Boolean, refusalReason: Option[String])

final case class ValidateProposalRequest(newContent: Option[String],
                                         sendNotificationEmail: Boolean,
                                         theme: Option[ThemeId],
                                         labels: Seq[String],
                                         tags: Seq[TagId],
                                         similarProposals: Seq[ProposalId],
                                         idea: Option[IdeaId],
                                         operation: Option[OperationId])

final case class PatchProposalsIdeaRequest(proposalIds: Seq[ProposalId], ideaId: IdeaId)

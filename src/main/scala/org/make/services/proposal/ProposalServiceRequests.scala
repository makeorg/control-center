package org.make.services.proposal

import io.circe.{Decoder, Encoder, Json}
import org.make.client.request.{Filter, Pagination, Sort}

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
case object Accepted extends ProposalStatus { override val shortName = "Accepted" }
case object Refused extends ProposalStatus { override val shortName = "Refused" }
case object Archived extends ProposalStatus { override val shortName = "Archived" }

object ProposalStatus {
  def matchProposalStatus(status: String): Option[ProposalStatus] = statusMap.get(status)

  val statusMap: Map[String, ProposalStatus] =
    Map(
      Pending.shortName -> Pending,
      Accepted.shortName -> Accepted,
      Refused.shortName -> Refused,
      Archived.shortName -> Archived
    )

  implicit lazy val proposalStatusEncoder: Encoder[ProposalStatus] = (gender: ProposalStatus) =>
    Json.fromString(gender.shortName)
  implicit lazy val proposalStatusDecoder: Decoder[ProposalStatus] =
    Decoder.decodeString.emap { value: String =>
      statusMap.get(value) match {
        case Some(profile) => Right(profile)
        case None          => Left(s"$value is not a proposal status")
      }
    }

}

final case class SortOptionRequest(field: String, mode: Option[Order])

final case class SearchOptionsRequest(sort: Seq[SortOptionRequest], limit: Option[Int], skip: Option[Int])

final case class ExhaustiveSearchRequest(themesIds: Option[Seq[String]] = None,
                                         tagsIds: Option[Seq[String]] = None,
                                         content: Option[String] = None,
                                         status: Option[ProposalStatus] = None,
                                         options: Option[SearchOptionsRequest] = None)

object ExhaustiveSearchRequest {

  def buildExhaustiveSearchRequest(pagination: Option[Pagination],
                                   sort: Option[Sort],
                                   filters: Option[Seq[Filter]]): ExhaustiveSearchRequest =
    ExhaustiveSearchRequest(
      themesIds = getThemesIdsFromFilters(filters),
      tagsIds = getTagsIdsFromFilters(filters),
      content = getContentFromFilters(filters),
      status = getStatusFromFilters(filters),
      options = getOptionsFromPaginationAndSorts(pagination, sort)
    )

  private def getThemesIdsFromFilters(maybeFilters: Option[Seq[Filter]]): Option[Seq[String]] = {
    maybeFilters.flatMap {
      _.find(_.field == "theme").map {
        _.value match {
          case filterListTheme: Seq[_] => Some(filterListTheme.asInstanceOf[Seq[String]])
          case filterTheme: String     => Some(Seq(filterTheme))
          case unknownFilterType =>
            g.console.warn(s"Unknown filter type: ${unknownFilterType.getClass.getName} with value $unknownFilterType")
            None
        }
      }.getOrElse(None)
    }
  }

  private def getTagsIdsFromFilters(maybeFilters: Option[Seq[Filter]]): Option[Seq[String]] = {
    maybeFilters.flatMap {
      _.find(_.field == "tag").map {
        _.value match {
          case filterListTag: Seq[_] => Some(filterListTag.asInstanceOf[Seq[String]])
          case filterTag: String     => Some(Seq(filterTag))
          case unknownFilterType =>
            g.console.warn(s"Unknown filter type: ${unknownFilterType.getClass.getName} with value $unknownFilterType")
            None
        }
      }.getOrElse(None)
    }
  }

  private def getContentFromFilters(maybeFilters: Option[Seq[Filter]]): Option[String] =
    maybeFilters.map(_.find(_.field == "content").map(_.value.asInstanceOf[String])).getOrElse(None)

  private def getStatusFromFilters(maybeFilters: Option[Seq[Filter]]): Option[ProposalStatus] =
    maybeFilters
      .flatMap(_.find(_.field == "status").map { status =>
        val maybeStatus = ProposalStatus.matchProposalStatus(status.value.asInstanceOf[String])
        if (maybeStatus.isEmpty) Some(Pending) else maybeStatus
      })
      .getOrElse(Some(Pending))

  def getOptionsFromPaginationAndSorts(maybePagination: Option[Pagination],
                                       maybeSort: Option[Sort]): Option[SearchOptionsRequest] =
    if (maybePagination.isDefined || maybeSort.isDefined) {
      val sortOptionRequest: Seq[SortOptionRequest] =
        maybeSort.flatMap { sort =>
          val order: Option[Order] = sort.order.toOption.flatMap(Order.matchOrder)
          sort.field.toOption.map(field => Seq(SortOptionRequest(field, order)))
        }.getOrElse(Seq.empty)
      Some(
        SearchOptionsRequest(
          sort = sortOptionRequest,
          limit = maybePagination.map(_.perPage),
          skip = maybePagination.map(pagination => pagination.page * pagination.perPage - pagination.perPage)
        )
      )
    } else { None }
}

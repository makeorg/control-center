package org.make.client.request

import org.make.client.{MakeServices, Resource, Response}
import org.make.services.proposal.Accepted

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

@js.native
trait GetListRequest extends js.Object with Request {
  val pagination: js.UndefOr[Pagination]
  val sort: js.UndefOr[Sort]
  val filter: js.UndefOr[js.Dictionary[js.Any]]
}

object GetListRequest extends MakeServices {
  def apply(pagination: Option[Pagination] = None,
            sort: Option[Sort] = None,
            filter: Option[Seq[Filter]] = None): GetListRequest =
    js.Dynamic
      .literal(pagination = pagination.orUndefined, sort = sort.orUndefined, filter = filter.orUndefined)
      .asInstanceOf[GetListRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case Resource.proposals =>
        val request = params.asInstanceOf[GetListRequest]
        proposalService.proposals(
          request.pagination.toOption,
          request.sort.toOption,
          request.filter.toOption.map(_.toJSArray.map(fieldValue => Filter(fieldValue._1, fieldValue._2)))
        )
      case Resource.validatedProposals =>
        val request = params.asInstanceOf[GetListRequest]
        proposalService.proposals(
          request.pagination.toOption,
          request.sort.toOption,
          request.filter.toOption.map(
            _.toJSArray
              .map(fieldValue => Filter(fieldValue._1, fieldValue._2)) ++ Seq(Filter("status", Accepted.shortName))
          )
        )
      case Resource.users =>
        throw ResourceNotImplementedException(s"Resource ${Resource.users} not implemented for request GetListRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource in GetListRequest")
    }
  }
}

package org.make.client.request

import org.make.client.{Resource, Response}
import org.make.services.idea.IdeaService
import org.make.services.operation.OperationService
import org.make.services.proposal.ProposalService

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.|

@js.native
trait GetListRequest extends js.Object with Request {
  val pagination: js.UndefOr[Pagination]
  val sort: js.UndefOr[Sort]
  val filter: js.UndefOr[js.Dictionary[String | js.Array[String]]]
}

object GetListRequest {
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
        ProposalService.proposals(
          request.pagination.toOption,
          request.sort.toOption,
          request.filter.toOption.map(_.toJSArray.map {
            case (fieldName, filterValue) => Filter(fieldName, filterValue)
          })
        )
      case Resource.ideas =>
        val request = params.asInstanceOf[GetListRequest]
        IdeaService.listIdeas(
          request.pagination.toOption,
          request.sort.toOption,
          request.filter.toOption.map(_.toJSArray.map {
            case (fieldName, filterValue) => Filter(fieldName, filterValue)
          })
        )
      case Resource.operations =>
        OperationService.operations()
      case Resource.users =>
        throw ResourceNotImplementedException(s"Resource ${Resource.users} not implemented for request GetListRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource in GetListRequest")
    }
  }
}

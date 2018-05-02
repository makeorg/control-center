package org.make.backoffice.client.request

import org.make.backoffice.client.{Resource, Response}

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

@js.native
trait GetManyReferenceRequest extends js.Object with Request {
  val target: String
  val id: String
  val pagination: js.UndefOr[Pagination]
  val sorts: js.UndefOr[Sort]
  val filter: js.UndefOr[Seq[Filter]]
}

object GetManyReferenceRequest {
  def apply(target: String,
            id: String,
            pagination: Option[Pagination] = None,
            sorts: Option[Sort] = None,
            filter: Option[Seq[Filter]] = None): GetManyReferenceRequest =
    js.Dynamic
      .literal(
        target = target,
        id = id,
        pagination = pagination.orUndefined,
        sorts = sorts.orUndefined,
        filter = filter.orUndefined
      )
      .asInstanceOf[GetManyReferenceRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case Resource.proposals =>
        throw ResourceNotImplementedException(
          s"Resource ${Resource.proposals} not implemented for request GetManyReferenceRequest"
        )
      case Resource.users =>
        throw ResourceNotImplementedException(
          s"Resource ${Resource.users} not implemented for request GetManyReferenceRequest"
        )
      case unknownResource =>
        throw UnknownResourceException(s"Unknown resource: $unknownResource in GetManyReferenceRequest")
    }
  }
}
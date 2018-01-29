package org.make.client.request

import org.make.client.{MakeServices, Resource, Response}

import scala.concurrent.Future
import scala.scalajs.js
import js.JSConverters._

@js.native
trait GetManyRequest extends js.Object with Request {
  val ids: js.Array[String]
}

object GetManyRequest extends MakeServices {
  def apply(ids: Seq[String]): GetManyRequest =
    js.Dynamic.literal(ids = ids.toJSArray).asInstanceOf[GetManyRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case Resource.operations =>
        val request = params.asInstanceOf[GetManyRequest]
        operationService.getOperationByIds(request.ids)
      case Resource.tags =>
        val request = params.asInstanceOf[GetManyRequest]
        tagService.getTagsByIds(request.ids)
      case Resource.proposals =>
        throw ResourceNotImplementedException(
          s"Resource ${Resource.proposals} not implemented for request GetManyRequest"
        )
      case Resource.users =>
        throw ResourceNotImplementedException(s"Resource ${Resource.users} not implemented for request GetManyRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource in GetManyRequest")
    }
  }
}

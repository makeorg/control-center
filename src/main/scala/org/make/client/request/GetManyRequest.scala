package org.make.client.request

import org.make.client.{MakeServices, Resource, Response}

import scala.concurrent.Future
import scala.scalajs.js

@js.native
trait GetManyRequest extends js.Object with Request {
  val id: Seq[String]
}

object GetManyRequest extends MakeServices {
  def apply(id: Seq[String]): GetManyRequest =
    js.Dynamic.literal(id = id).asInstanceOf[GetManyRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
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

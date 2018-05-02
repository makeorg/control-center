package org.make.backoffice.client.request

import org.make.backoffice.client.{Resource, Response}

import scala.concurrent.Future
import scala.scalajs.js

@js.native
trait DeleteRequest extends js.Object with Request {
  val id: String
}

object DeleteRequest {
  def apply(id: String): DeleteRequest =
    js.Dynamic.literal(id = id).asInstanceOf[DeleteRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case Resource.proposals =>
        throw ResourceNotImplementedException(
          s"Resource ${Resource.proposals} not implemented for request DeleteRequest"
        )
      case Resource.users =>
        throw ResourceNotImplementedException(s"Resource ${Resource.users} not implemented for request DeleteRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource in DeleteRequest")
    }
  }
}

package org.make.backoffice.client.request

import org.make.backoffice.client._

import scala.concurrent.Future
import scala.scalajs.js

case class InvalidRestVerbException(message: String = "Invalid REST verb") extends Exception(message)
case class UnknownResourceException(message: String = "Unknown resource") extends Exception(message)
case class ResourceNotImplementedException(message: String = "Resource not implemented") extends Exception(message)

@js.native
trait Request extends js.Object

object Request {
  def fetch(restVerb: String, resource: String, params: js.Object): Future[Response] =
    restVerb match {
      case "GET_LIST"           => GetListRequest.fetch(resource, params)
      case "GET_ONE"            => GetOneRequest.fetch(resource, params)
      case "CREATE"             => CreateRequest.fetch(resource, params)
      case "UPDATE"             => UpdateRequest.fetch(resource, params)
      case "DELETE"             => DeleteRequest.fetch(resource, params)
      case "GET_MANY"           => GetManyRequest.fetch(resource, params)
      case "GET_MANY_REFERENCE" => GetManyReferenceRequest.fetch(resource, params)
      case invalidRestVerb      => throw InvalidRestVerbException(s"Invalid REST verb: $invalidRestVerb")
    }
}

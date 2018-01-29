package org.make.client.request

import org.make.backoffice.models.Idea
import org.make.client.{MakeServices, Resource, Response}
import sun.security.provider.certpath.OCSPResponse.SingleResponse

import scala.concurrent.Future
import scala.scalajs.js

@js.native
trait CreateRequest[ENTITY <: js.Object] extends js.Object with Request {
  val data: ENTITY
}

object CreateRequest extends MakeServices {
  def apply[ENTITY <: js.Object](data: ENTITY): CreateRequest[ENTITY] =
    js.Dynamic.literal(data = data).asInstanceOf[CreateRequest[ENTITY]]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case Resource.proposals =>
        throw ResourceNotImplementedException(
          s"Resource ${Resource.proposals} not implemented for request CreateRequest"
        )
      case Resource.users =>
        throw ResourceNotImplementedException(s"Resource ${Resource.users} not implemented for request CreateRequest")
      case Resource.ideas =>
        val request: CreateRequest[Idea] = params.asInstanceOf[CreateRequest[Idea]]
        ideaService.createIdea(
          name = request.data.name,
          language = request.data.language.toOption,
          country = request.data.country.toOption,
          operation = request.data.operationId.toOption,
          question = request.data.question.toOption
        )
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource in CreateRequest")
    }
  }
}

package org.make.client.request

import org.make.backoffice.models.Idea
import org.make.client.{MakeServices, Resource, Response}

import scala.concurrent.Future
import scala.scalajs.js

@js.native
trait UpdateRequest[ENTITY <: js.Object] extends js.Object with Request {
  val id: String
  val data: ENTITY
}

object UpdateRequest extends MakeServices {
  def apply[ENTITY <: js.Object](id: String, data: ENTITY): UpdateRequest[ENTITY] =
    js.Dynamic.literal(id = id, data = data).asInstanceOf[UpdateRequest[ENTITY]]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case Resource.proposals =>
        throw ResourceNotImplementedException(
          s"Resource ${Resource.proposals} not implemented for request UpdateRequest"
        )
      case Resource.ideas =>
        val request = params.asInstanceOf[UpdateRequest[Idea]]
        ideaService.updateIdea(request.id, request.data)
      case Resource.users =>
        throw ResourceNotImplementedException(s"Resource ${Resource.users} not implemented for request UpdateRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource in UpdateRequest")
    }
  }
}

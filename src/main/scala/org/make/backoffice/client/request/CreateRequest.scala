package org.make.backoffice.client.request

import org.make.backoffice.model.Idea
import org.make.backoffice.client.{Resource, Response}
import org.make.backoffice.service.idea.IdeaService

import scala.concurrent.Future
import scala.scalajs.js

@js.native
trait CreateRequest[ENTITY <: js.Object] extends js.Object with Request {
  val data: ENTITY
}

object CreateRequest {
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
        IdeaService.createIdea(
          name = request.data.name,
          language = request.data.language.toOption,
          country = request.data.country.toOption,
          operation = request.data.operationId.toOption,
          theme = request.data.themeId.toOption,
          question = request.data.question.toOption
        )
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource in CreateRequest")
    }
  }
}

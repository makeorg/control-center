package org.make.backoffice.client.request

import org.make.backoffice.client.{Resource, Response}
import org.make.backoffice.service.idea.IdeaService
import org.make.backoffice.service.proposal.ProposalService

import scala.concurrent.Future
import scala.scalajs.js

@js.native
trait GetOneRequest extends js.Object with Request {
  val id: String
}

object GetOneRequest {
  def apply(id: String): GetOneRequest =
    js.Dynamic.literal(id = id).asInstanceOf[GetOneRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case Resource.proposals =>
        val request = params.asInstanceOf[GetOneRequest]
        ProposalService.getProposalById(request.id)
      case Resource.ideas =>
        val request = params.asInstanceOf[GetOneRequest]
        IdeaService.getIdea(request.id)
      case Resource.users =>
        throw ResourceNotImplementedException(s"Resource ${Resource.users} not implemented for request GetOneRequest")
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource in GetOneRequest")
    }
  }
}

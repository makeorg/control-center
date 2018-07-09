/*
 * Make.org Control Center
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

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

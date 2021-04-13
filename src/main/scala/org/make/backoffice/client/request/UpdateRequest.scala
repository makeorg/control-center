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

import org.make.backoffice.model.Idea
import org.make.backoffice.client.{Resource, Response}
import org.make.backoffice.service.idea.IdeaService

import scala.concurrent.Future
import scala.scalajs.js

@js.native
trait UpdateRequest[ENTITY <: js.Object] extends js.Object with Request {
  val id: String
  val data: ENTITY
}

object UpdateRequest {
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
        IdeaService.updateIdea(request.id, request.data)
      case unknownResource => throw UnknownResourceException(s"Unknown resource: $unknownResource in UpdateRequest")
    }
  }
}

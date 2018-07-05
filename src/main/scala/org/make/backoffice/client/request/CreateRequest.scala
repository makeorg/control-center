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

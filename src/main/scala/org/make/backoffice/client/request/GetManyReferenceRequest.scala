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
import org.make.backoffice.service.proposal.ProposalService

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.|

@js.native
trait GetManyReferenceRequest extends js.Object with Request {
  val target: String
  val id: String
  val pagination: js.UndefOr[Pagination]
  val sorts: js.UndefOr[Sort]
  val filter: js.UndefOr[js.Dictionary[String | js.Array[String]]]
}

object GetManyReferenceRequest {
  def apply(target: String,
            id: String,
            pagination: Option[Pagination] = None,
            sorts: Option[Sort] = None,
            filter: Option[Seq[Filter]] = None): GetManyReferenceRequest =
    js.Dynamic
      .literal(
        target = target,
        id = id,
        pagination = pagination.orUndefined,
        sorts = sorts.orUndefined,
        filter = filter.orUndefined
      )
      .asInstanceOf[GetManyReferenceRequest]

  def fetch(resource: String, params: js.Object): Future[Response] = {
    resource match {
      case Resource.proposals =>
        val request = params.asInstanceOf[GetManyReferenceRequest]
        ProposalService.proposals(
          request.pagination.toOption,
          request.sorts.toOption,
          request.filter.toOption
            .map(_.toJSArray.map {
              case (fieldName, filterValue) => Filter(fieldName, filterValue)
            }.toSeq)
            .map(_ :+ Filter(request.target, request.id))
            .orElse(Some(Seq(Filter(request.target, request.id))))
        )
      case unknownResource =>
        throw UnknownResourceException(s"Unknown resource: $unknownResource in GetManyReferenceRequest")
    }
  }
}

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

package org.make.backoffice.facade.AdminOnRest

import scala.scalajs.js
import js.JSConverters._
import scala.scalajs.js.UndefOr

object Action {
  val `AOR/FETCH_CANCEL` = AOR_Action("AOR/FETCH_CANCEL")
  def `AOR/CRUD_GET_LIST`(pagination: Option[AOR_Pagination], sort: Option[AOR_Sort], filter: Map[String, Any]) =
    AOR_Action(
      `type` = "AOR/CRUD_GET_LIST",
      payload = Some(AOR_Payload(pagination = pagination, sort = sort, filter = filter)),
      meta = Some(AOR_Meta(resource = "to_enrich_proposals", cancelPrevious = true, fetch = Some("GET_LIST")))
    )
}

@js.native
trait AOR_Action extends js.Object {
  val `type`: String
  val payload: UndefOr[AOR_Payload]
  val meta: UndefOr[AOR_Meta]
}

object AOR_Action {
  def apply(`type`: String, payload: Option[AOR_Payload] = None, meta: Option[AOR_Meta] = None): AOR_Action =
    js.Dynamic
      .literal(`type` = `type`, payload = payload.orUndefined, meta = meta.orUndefined)
      .asInstanceOf[AOR_Action]
}

@js.native
trait AOR_Meta extends js.Object {
  val resource: String
  val cancelPrevious: Boolean
  val fetch: UndefOr[String]
}

object AOR_Meta {
  def apply(resource: String, cancelPrevious: Boolean, fetch: Option[String]): AOR_Meta =
    js.Dynamic
      .literal(resource = resource, cancelPrevious = cancelPrevious, fetch = fetch.orUndefined)
      .asInstanceOf[AOR_Meta]
}

@js.native
trait AOR_Payload extends js.Object {
  val pagination: UndefOr[AOR_Pagination]
  val sort: UndefOr[AOR_Sort]
  val filter: js.Dictionary[js.Any]
}

object AOR_Payload {
  def apply(pagination: Option[AOR_Pagination], sort: Option[AOR_Sort], filter: Map[String, Any]): AOR_Payload =
    js.Dynamic
      .literal(pagination = pagination.orUndefined, sort = sort.orUndefined, filter = filter.toJSDictionary)
      .asInstanceOf[AOR_Payload]
}

@js.native
trait AOR_Pagination extends js.Object {
  val page: Int
  val perPage: Int
}

object AOR_Pagination {
  def apply(page: Int, perPage: Int): AOR_Pagination =
    js.Dynamic.literal(page = page, perPage = perPage).asInstanceOf[AOR_Pagination]

  def apply(): AOR_Pagination =
    js.Dynamic.literal(page = 1, perPage = 10).asInstanceOf[AOR_Pagination]
}

@js.native
trait AOR_Sort extends js.Object {
  val field: String
  val order: String
}

object AOR_Sort {
  def apply(field: String, order: String): AOR_Sort =
    js.Dynamic.literal(field = field, order = order).asInstanceOf[AOR_Sort]
}

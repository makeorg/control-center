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

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

@js.native
trait Pagination extends js.Object {
  val page: Int
  val perPage: Int
}
object Pagination {
  def apply(page: Int, perPage: Int): Pagination =
    js.Dynamic.literal(page = page, perPage = perPage).asInstanceOf[Pagination]
}

@js.native
sealed trait Order extends js.Object {
  val shortName: String
}
object Order {
  val asc = Order("ASC")
  val desc = Order("DESC")

  val orders: Map[String, Order] = Map(asc.shortName -> asc, desc.shortName -> desc)

  def apply(order: String): Order = js.Dynamic.literal(shortName = order).asInstanceOf[Order]
}

@js.native
trait Sort extends js.Object {
  val field: js.UndefOr[String]
  val order: js.UndefOr[String]
}
object Sort {
  def apply(field: Option[String], order: Option[String]): Sort =
    js.Dynamic.literal(field = field.orUndefined, order = order.orUndefined).asInstanceOf[Sort]
}

@js.native
trait Filter extends js.Object {
  val field: String
  val value: js.Any
}
object Filter {
  def apply(field: String, value: Any): Filter =
    js.Dynamic.literal(field = field, value = value.asInstanceOf[js.Any]).asInstanceOf[Filter]
}

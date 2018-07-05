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

package org.make.backoffice.client

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

@js.native
sealed trait Response extends js.Object

@js.native
trait ListTotalResponse[ENTITY <: js.Object] extends js.Object with Response {
  val total: Int
  val data: js.Array[ENTITY]
}
object ListTotalResponse {
  def apply[ENTITY <: js.Object](total: Int, data: js.Array[ENTITY]): ListTotalResponse[ENTITY] =
    js.Dynamic.literal(total = total, data = data).asInstanceOf[ListTotalResponse[ENTITY]]
}

@js.native
trait ListDataResponse[ENTITY <: js.Object] extends js.Object with Response {
  val data: js.Array[ENTITY]
}
object ListDataResponse {
  def apply[ENTITY <: js.Object](data: Seq[ENTITY]): ListDataResponse[ENTITY] =
    js.Dynamic.literal(data = data.toJSArray).asInstanceOf[ListDataResponse[ENTITY]]
}

@js.native
trait SingleResponse[+ENTITY <: js.Object] extends js.Object with Response {
  val data: ENTITY
}
object SingleResponse {
  def apply[ENTITY <: js.Object](data: ENTITY): SingleResponse[ENTITY] =
    js.Dynamic.literal(data = data).asInstanceOf[SingleResponse[ENTITY]]
}

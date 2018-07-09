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

package org.make.backoffice.facade

import scala.scalajs.js
import scala.scalajs.js.Promise
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("gapi-google", JSImport.Namespace)
object GApi extends js.Object {
  val gapi: GApi = js.native
}

@js.native
trait GApi extends js.Object {
  val auth2: Auth2Provider = js.native
  val signin2: Signin2 = js.native
}

@js.native
trait Auth2Provider extends js.Object {
  def init(params: Map[String, String]): Unit = js.native

  def getAuthInstance(): Auth2 = js.native
}

@js.native
trait Auth2 extends js.Object {
  def signOut(): Promise[Unit] = js.native
}

@js.native
trait Signin2 extends js.Object {
  def render(method: String, params: RenderParameters): Unit = js.native
}

@js.native
trait RenderParameters extends js.Object {
  def scope: String = js.native
  def width: Int = js.native
  def height: Int = js.native
  def longtitle: Boolean = js.native
  def theme: String = js.native
  def onsuccess: () => _ = js.native
}

object RenderParameters {
  def apply(scope: String, width: Int, height: Int, longtitle: Boolean, theme: String): RenderParameters =
    js.Dynamic
      .literal(scope = scope, width = width, height = height, longtitle = longtitle, theme = theme)
      .asInstanceOf[RenderParameters]
}

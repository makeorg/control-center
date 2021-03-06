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

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.StringAttributeSpec
import org.make.backoffice.facade.ReactClassAttributeSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "Resource")
object NativeResource extends ReactClass

object Resource {

  implicit class ResourceVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Resource: ReactClassElementSpec = elements(NativeResource)
  }

  implicit class ResourceVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val name = StringAttributeSpec("name")
    lazy val listing = ReactClassAttributeSpec("list")
    lazy val create = ReactClassAttributeSpec("create")
    lazy val edit = ReactClassAttributeSpec("edit")
    lazy val show = ReactClassAttributeSpec("show")
    lazy val remove = ReactClassAttributeSpec("remove")
  }

}

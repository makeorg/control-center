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
import io.github.shogowada.scalajs.reactjs.router.Router.RouterVirtualDOMAttributes.{
  HistoryAttributeSpec,
  LocationAttributeSpec
}
import io.github.shogowada.statictags.StringAttributeSpec
import org.make.backoffice.facade.MatchAttributeSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "Delete")
object NativeDelete extends ReactClass

object Delete {
  implicit class DeleteVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Delete: ReactClassElementSpec = elements(NativeDelete)
  }

  implicit class DeleteVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val resource = StringAttributeSpec("resource")
    lazy val location = LocationAttributeSpec("location")
    lazy val history = HistoryAttributeSpec("history")
    lazy val `match` = MatchAttributeSpec("match")
  }
}

@js.native
@JSImport("admin-on-rest", "DeleteButton")
object NativeDeleteButton extends ReactClass

object DeleteButton {
  implicit class DeleteButtonVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val DeleteButton: ReactClassElementSpec = elements(NativeDeleteButton)
  }
}

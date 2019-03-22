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
import org.make.backoffice.facade.ElementAttributeSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "MenuItemLink")
object NativeMenuItemLink extends ReactClass

@js.native
@JSImport("admin-on-rest", "DashboardMenuItem")
object NativeDashboardMenuItem extends ReactClass

object Menu {

  implicit class MenuVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val MenuItemLink: ReactClassElementSpec = elements(NativeMenuItemLink)
    lazy val DashboardMenuItem: ReactClassElementSpec = elements(NativeDashboardMenuItem)
  }

  implicit class MenuVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val to = StringAttributeSpec("to")
    lazy val leftIcon = ElementAttributeSpec("leftIcon")
  }

}

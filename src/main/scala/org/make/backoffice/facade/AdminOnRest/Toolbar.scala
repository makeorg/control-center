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

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.{BooleanAttributeSpec, StringAttributeSpec}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "Toolbar")
object NativeToolbar extends ReactClass

object Toolbar {
  implicit class ToolbarVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Toolbar: ReactClassElementSpec = elements(NativeToolbar)
  }
}

@js.native
@JSImport("admin-on-rest", "SaveButton")
object NativeSaveButton extends ReactClass

object SaveButton {
  implicit class SaveButtonVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val SaveButton: ReactClassElementSpec = elements(NativeSaveButton)
  }

  implicit class SaveButtonVirtualDOMAattributes(attributes: VirtualDOMAttributes) {
    lazy val label = StringAttributeSpec("label")
    lazy val submitOnEnter = BooleanAttributeSpec("submitOnEnter")
    lazy val raised = BooleanAttributeSpec("raised")
  }
}

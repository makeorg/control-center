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
import io.github.shogowada.statictags.{BooleanAttributeSpec, StringAttributeSpec}
import org.make.backoffice.facade.AdminOnRest.ShowButton.RecordAttributeSpec
import org.make.backoffice.facade.MaterialUi.IntAttributeSpec
import org.make.backoffice.facade.{ElementAttributeSpec, LocationAttributeSpec, MatchAttributeSpec}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "Edit")
object NativeEdit extends ReactClass

object Edit {
  implicit class EditVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Edit: ReactClassElementSpec = elements(NativeEdit)
  }

  implicit class EditVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val editTitle = ElementAttributeSpec("title")
    lazy val actions = ElementAttributeSpec("actions")
    lazy val resource = StringAttributeSpec("resource")
    lazy val location = LocationAttributeSpec("location")
    lazy val `match` = MatchAttributeSpec("match")
    lazy val hasList = BooleanAttributeSpec("hasList")
    lazy val perPage = IntAttributeSpec("perPage")
  }
}

@js.native
@JSImport("admin-on-rest", "EditButton")
object NativeEditButton extends ReactClass

object EditButton {
  implicit class EditButtonVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val EditButton: ReactClassElementSpec = elements(NativeEditButton)
  }

  implicit class EditButtonVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val basePath = StringAttributeSpec("basePath")
    lazy val record = RecordAttributeSpec("record")
  }
}

@js.native
@JSImport("admin-on-rest", "TabbedForm")
object NativeTabbedForm extends ReactClass

object TabbedForm {
  implicit class TabbedFormVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TabbedForm: ReactClassElementSpec = elements(NativeTabbedForm)
  }
}

@js.native
@JSImport("admin-on-rest", "FormTab")
object NativeFormTab extends ReactClass

object FormTab {
  implicit class FormTabVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val FormTab: ReactClassElementSpec = elements(NativeFormTab)
  }
}

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

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.{Attribute, BooleanAttributeSpec, StringAttributeSpec}
import org.make.backoffice.facade.{ElementAttributeSpec, LocationAttributeSpec, MatchAttributeSpec}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "Show")
object NativeShow extends ReactClass

object Show {

  implicit class ShowVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Show: ReactClassElementSpec = elements(NativeShow)
  }

  implicit class ShowVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val resource = StringAttributeSpec("resource")
    lazy val location = LocationAttributeSpec("location")
    lazy val `match` = MatchAttributeSpec("match")
    lazy val showTitle = ElementAttributeSpec("title")
    lazy val hasEdit = BooleanAttributeSpec("hasEdit")
    lazy val hasList = BooleanAttributeSpec("hasList")
  }
}

@js.native
@JSImport("admin-on-rest", "SimpleShowLayout")
object NativeSimpleShowLayout extends ReactClass

object SimpleShowLayout {
  implicit class SimpleShowLayoutVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val SimpleShowLayout: ReactClassElementSpec = elements(NativeSimpleShowLayout)
  }
}

@js.native
@JSImport("admin-on-rest", "TabbedShowLayout")
object NativeTabbedShowLayout extends ReactClass

object TabbedShowLayout {
  implicit class TabbedShowLayoutVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TabbedShowLayout: ReactClassElementSpec = elements(NativeTabbedShowLayout)
  }
}

@js.native
@JSImport("admin-on-rest", "Tab")
object NativeTab extends ReactClass

object Tab {
  implicit class TabVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Tab: ReactClassElementSpec = elements(NativeTab)
  }
}

@js.native
@JSImport("admin-on-rest", "ShowButton")
object NativeShowButton extends ReactClass

object ShowButton {

  case class RecordAttributeSpec(name: String) {
    def :=(element: js.Dynamic): Attribute[js.Dynamic] =
      Attribute(name = name, value = element, AS_IS)
  }

  implicit class ShowButtonVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ShowButton: ReactClassElementSpec = elements(NativeShowButton)
  }

  implicit class ShowButtonVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val basePath = StringAttributeSpec("basePath")
    lazy val record = RecordAttributeSpec("record")
  }
}

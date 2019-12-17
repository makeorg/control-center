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

package org.make.backoffice.facade.reduxForm

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.facade.FunctionAttributeSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("redux-form", "default")
object NativeField extends js.Object {
  def Field: ReactClass = js.native
}

object Field {

  implicit class FieldVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Field: ReactClassElementSpec = elements(NativeField.Field)
  }

  implicit class FieldVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val component = FunctionAttributeSpec("component")
  }
}

@js.native
trait FieldInput extends js.Any {
  def value: String = js.native
  def onChange(value: Any): Unit = js.native
}

@js.native
trait FieldHolder extends js.Any {
  def input: FieldInput = js.native
}

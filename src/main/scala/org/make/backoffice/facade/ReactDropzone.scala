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

import io.github.shogowada.scalajs.reactjs.EventVirtualDOMAttributes.OnFormEventAttribute
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.BooleanAttributeSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-dropzone", JSImport.Default)
object NativeReactDropzone extends ReactClass

object ReactDropzone {

  implicit class DropzoneVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Dropzone: ReactClassElementSpec = elements(NativeReactDropzone)
  }

  implicit class DropzoneVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val multiple = BooleanAttributeSpec("multiple")
    lazy val onDropDropzone = OnFormEventAttribute("onDrop")
  }

}

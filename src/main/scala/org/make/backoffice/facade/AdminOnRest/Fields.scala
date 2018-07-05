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
import io.github.shogowada.statictags._
import org.make.backoffice.facade.FunctionAttributeSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "TextField")
object NativeTextField extends ReactClass

@js.native
@JSImport("admin-on-rest", "BooleanField")
object NativeBooleanField extends ReactClass

@js.native
@JSImport("admin-on-rest", "ChipField")
object NativeChipField extends ReactClass

@js.native
@JSImport("admin-on-rest", "DateField")
object NativeDateField extends ReactClass

@js.native
@JSImport("admin-on-rest", "EmailField")
object NativeEmailField extends ReactClass

@js.native
@JSImport("admin-on-rest", "ImageField")
object NativeImageField extends ReactClass

@js.native
@JSImport("admin-on-rest", "FunctionField")
object NativeFunctionField extends ReactClass

@js.native
@JSImport("admin-on-rest", "NumberField")
object NativeNumberField extends ReactClass

@js.native
@JSImport("admin-on-rest", "ReferenceField")
object NativeReferenceField extends ReactClass

@js.native
@JSImport("admin-on-rest", "ReferenceManyField")
object NativeReferenceManyField extends ReactClass

@js.native
@JSImport("admin-on-rest", "ReferenceArrayField")
object NativeReferenceArrayField extends ReactClass

@js.native
@JSImport("admin-on-rest", "RichTextField")
object NativeRichTextField extends ReactClass

@js.native
@JSImport("admin-on-rest", "UrlField")
object NativeUrlField extends ReactClass

@js.native
@JSImport("admin-on-rest", "SingleFieldList")
object NativeSingleFieldList extends ReactClass

object Fields {

  case class RenderRecordAttributeSpec(name: String) extends AttributeSpec {
    def :=(renderRecord: js.Function1[js.Object, js.Any]): Attribute[js.Function1[js.Object, js.Any]] =
      Attribute(name = name, value = renderRecord, AS_IS)
  }

  implicit class FieldsVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TextField: ReactClassElementSpec = elements(NativeTextField)
    lazy val BooleanField: ReactClassElementSpec = elements(NativeBooleanField)
    lazy val ChipField: ReactClassElementSpec = elements(NativeChipField)
    lazy val DateField: ReactClassElementSpec = elements(NativeDateField)
    lazy val EmailField: ReactClassElementSpec = elements(NativeEmailField)
    lazy val ImageField: ReactClassElementSpec = elements(NativeImageField)
    lazy val FunctionField: ReactClassElementSpec = elements(NativeFunctionField)
    lazy val NumberField: ReactClassElementSpec = elements(NativeNumberField)
    lazy val ReferenceField: ReactClassElementSpec = elements(NativeReferenceField)
    lazy val ReferenceManyField: ReactClassElementSpec = elements(NativeReferenceManyField)
    lazy val ReferenceArrayField: ReactClassElementSpec = elements(NativeReferenceArrayField)
    lazy val RichTextField: ReactClassElementSpec = elements(NativeRichTextField)
    lazy val UrlField: ReactClassElementSpec = elements(NativeUrlField)
    lazy val SingleFieldList: ReactClassElementSpec = elements(NativeSingleFieldList)
  }

  implicit class FieldsVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val showTime = BooleanAttributeSpec("showTime")
    lazy val src = StringAttributeSpec("src")
    lazy val render = RenderRecordAttributeSpec("render")
    lazy val locales = StringAttributeSpec("locales")
    lazy val options = CssAttributeSpec("options")
    lazy val allowEmpty = BooleanAttributeSpec("allowEmpty")
    lazy val reference = StringAttributeSpec("reference")
    lazy val linkType = DynamicAttributeSpec("linkType")
    lazy val target = StringAttributeSpec("target")
    lazy val source = StringAttributeSpec("source")
    lazy val label = StringAttributeSpec("label")
    lazy val addLabel = BooleanAttributeSpec("addLabel")
    lazy val sortable = BooleanAttributeSpec("sortable")
    lazy val style = CssAttributeSpec("style")
    lazy val elStyle = CssAttributeSpec("elStyle")
    lazy val alwaysOn = BooleanAttributeSpec("alwaysOn")
    lazy val validate = FunctionAttributeSpec("validate")
  }
}

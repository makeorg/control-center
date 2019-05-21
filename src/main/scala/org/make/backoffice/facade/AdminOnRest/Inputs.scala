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
import org.make.backoffice.facade._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "TextInput")
object NativeTextInput extends ReactClass

@js.native
@JSImport("admin-on-rest", "AutocompleteInput")
object NativeAutocompleteInput extends ReactClass

@js.native
@JSImport("admin-on-rest", "ReferenceInput")
object NativeReferenceInput extends ReactClass

@js.native
@JSImport("admin-on-rest", "ReferenceArrayInput")
object NativeReferenceArrayInput extends ReactClass

@js.native
@JSImport("admin-on-rest", "SelectInput")
object NativeSelectInput extends ReactClass

@js.native
@JSImport("admin-on-rest", "SelectArrayInput")
object NativeSelectArrayInput extends ReactClass

@js.native
@JSImport("aor-dependent-input", "DependentInput")
object NativeDependentInput extends ReactClass

@js.native
@JSImport("admin-on-rest", "NumberInput")
object NativeNumberInput extends ReactClass

@js.native
@JSImport("admin-on-rest", "BooleanInput")
object NativeBooleanInput extends ReactClass

@js.native
@JSImport("admin-on-rest", "NullableBooleanInput")
object NativeNullableBooleanInput extends ReactClass

@js.native
@JSImport("admin-on-rest", "DateInput")
object NativeDateInput extends ReactClass

@js.native
@JSImport("admin-on-rest", "LongTextInput")
object NativeLongTextInput extends ReactClass

object Inputs {
  implicit class InputsVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TextInput: ReactClassElementSpec = elements(NativeTextInput)
    lazy val AutocompleteInput: ReactClassElementSpec = elements(NativeAutocompleteInput)
    lazy val ReferenceInput: ReactClassElementSpec = elements(NativeReferenceInput)
    lazy val ReferenceArrayInput: ReactClassElementSpec = elements(NativeReferenceArrayInput)
    lazy val SelectInput: ReactClassElementSpec = elements(NativeSelectInput)
    lazy val SelectArrayInput: ReactClassElementSpec = elements(NativeSelectArrayInput)
    lazy val DependentInput: ReactClassElementSpec = elements(NativeDependentInput)
    lazy val NumberInput: ReactClassElementSpec = elements(NativeNumberInput)
    lazy val NullableBooleanInput: ReactClassElementSpec = elements(NativeNullableBooleanInput)
    lazy val BooleanInput: ReactClassElementSpec = elements(NativeBooleanInput)
    lazy val DateInput: ReactClassElementSpec = elements(NativeDateInput)
    lazy val LongTextInput: ReactClassElementSpec = elements(NativeLongTextInput)
  }

  implicit class InputsVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    type FilterToQuery = js.Function1[String, js.Dictionary[String]]

    case class FilterToQueryAttributeSpec(name: String) extends AttributeSpec {
      def :=(element: FilterToQuery): Attribute[FilterToQuery] =
        Attribute(name = name, value = element, AS_IS)
    }

    case class DefaultValueAttributeSpec(name: String) extends AttributeSpec {
      def :=(value: String): Attribute[String] =
        Attribute(name = name, value = value, AS_IS)

      def :=(value: Boolean): Attribute[Boolean] =
        Attribute(name = name, value = value, AS_IS)
    }

    lazy val stripTags = BooleanAttributeSpec("stripTags")
    lazy val choices = ChoicesAttributeSpec("choices")
    lazy val dependsOn = StringAttributeSpec("dependsOn")
    lazy val dependsValue = DynamicAttributeSpec("value")
    lazy val defaultValue = DefaultValueAttributeSpec("defaultValue")
    lazy val resolve = StringAttributeSpec("resolve")
    lazy val optionText = StringAttributeSpec("optionText")
    lazy val optionValue = StringAttributeSpec("optionValue")
    lazy val filterToQuery = FilterToQueryAttributeSpec("filterToQuery")
    lazy val translateLabel = FunctionAttributeSpec("translate")
    lazy val parse = FunctionAttributeSpec("parse")
    lazy val sort = MapStringAttributeSpec("sort")
  }
}

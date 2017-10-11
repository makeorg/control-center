package org.make.backoffice.facades.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags._
import org.make.backoffice.facades._

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
@JSImport("admin-on-rest", "SelectInput")
object NativeSelectInput extends ReactClass

@js.native
@JSImport("admin-on-rest", "SelectArrayInput")
object NativeSelectArrayInput extends ReactClass

object Inputs {
  implicit class InputsVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TextInput: ReactClassElementSpec = elements(NativeTextInput)
    lazy val AutocompleteInput: ReactClassElementSpec = elements(NativeAutocompleteInput)
    lazy val ReferenceInput: ReactClassElementSpec = elements(NativeReferenceInput)
    lazy val SelectInput: ReactClassElementSpec = elements(NativeSelectInput)
    lazy val SelectArrayInput: ReactClassElementSpec = elements(NativeSelectArrayInput)
  }

  implicit class InputsVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val stripTags = BooleanAttributeSpec("stripTags")
    lazy val choices = ChoicesAttributeSpec("choices")
    lazy val reference = StringAttributeSpec("reference")
  }
}

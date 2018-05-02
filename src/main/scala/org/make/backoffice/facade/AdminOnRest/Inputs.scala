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

object Inputs {
  implicit class InputsVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TextInput: ReactClassElementSpec = elements(NativeTextInput)
    lazy val AutocompleteInput: ReactClassElementSpec = elements(NativeAutocompleteInput)
    lazy val ReferenceInput: ReactClassElementSpec = elements(NativeReferenceInput)
    lazy val ReferenceArrayInput: ReactClassElementSpec = elements(NativeReferenceArrayInput)
    lazy val SelectInput: ReactClassElementSpec = elements(NativeSelectInput)
    lazy val SelectArrayInput: ReactClassElementSpec = elements(NativeSelectArrayInput)
    lazy val DependentInput: ReactClassElementSpec = elements(NativeDependentInput)
  }

  implicit class InputsVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    type FilterToQuery = js.Function1[String, js.Dictionary[String]]

    case class FilterToQueryAttributeSpec(name: String) extends AttributeSpec {
      def :=(element: FilterToQuery): Attribute[FilterToQuery] =
        Attribute(name = name, value = element, AS_IS)
    }

    lazy val stripTags = BooleanAttributeSpec("stripTags")
    lazy val choices = ChoicesAttributeSpec("choices")
    lazy val dependsOn = StringAttributeSpec("dependsOn")
    lazy val dependsValue = StringAttributeSpec("value")
    lazy val resolve = StringAttributeSpec("resolve")
    lazy val optionText = StringAttributeSpec("optionText")
    lazy val filterToQuery = FilterToQueryAttributeSpec("filterToQuery")
  }
}
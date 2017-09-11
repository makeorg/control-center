package org.make.backoffice.facades.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags._
import org.make.backoffice.facades._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "TextField")
object NativeTextField extends ReactClass

@js.native
@JSImport("admine-on-rest", "BooleanField")
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
@JSImport("admin-on-rest", "RichTextField")
object NativeRichTextField extends ReactClass

@js.native
@JSImport("admin-on-rest", "UrlField")
object NativeUrlField extends ReactClass

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

object Field {

  object TextField {
    implicit class TextFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val TextField: ReactClassElementSpec = elements(NativeTextField)
    }
  }

  object BooleanField {
    implicit class BooleanFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val BooleanField: ReactClassElementSpec = elements(NativeBooleanField)
    }
  }

  object ChipField {
    implicit class ChipFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val ChipField: ReactClassElementSpec = elements(NativeChipField)
    }
  }

  object DateField {
    implicit class DateFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val DateField: ReactClassElementSpec = elements(NativeDateField)
    }

    implicit class DateFieldVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
      lazy val showTime = BooleanAttributeSpec("showTime")
    }
  }

  object EmailField {
    implicit class EmailFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val EmailField: ReactClassElementSpec = elements(NativeEmailField)
    }
  }

  object ImageField {
    implicit class ImageFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val ImageField: ReactClassElementSpec = elements(NativeImageField)
    }

    implicit class ImageFieldVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
      lazy val src = StringAttributeSpec("src")
    }
  }

  object FunctionField {
    case class RenderRecordAttributeSpec(name: String) extends AttributeSpec {
      def :=(renderRecord: js.Function1[js.Object, js.Any]): Attribute[js.Function1[js.Object, js.Any]] =
        Attribute(name = name, value = renderRecord, AS_IS)
    }

    implicit class FunctionFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val FunctionField: ReactClassElementSpec = elements(NativeFunctionField)
    }

    implicit class FunctionFieldVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
      lazy val render = RenderRecordAttributeSpec("render")
    }
  }

  object NumberField {
    implicit class NumberFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val NumberField: ReactClassElementSpec = elements(NativeNumberField)
    }

    implicit class NumberFieldVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
      lazy val locales = StringAttributeSpec("locales")
      lazy val options = MapAttributeSpec("options")
    }
  }

  object ReferenceField {
    implicit class ReferenceFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val ReferenceField: ReactClassElementSpec = elements(NativeReferenceField)
    }

    implicit class ReferenceFieldVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
      lazy val reference = StringAttributeSpec("reference")
      lazy val linkType = DynamicAttributeSpec("linkType")
    }
  }

  object ReferenceManyField {
    implicit class ReferenceManyFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val ReferenceManyField: ReactClassElementSpec = elements(NativeReferenceManyField)
    }

    implicit class ReferenceManyFieldVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
      lazy val reference = StringAttributeSpec("reference")
      lazy val target = StringAttributeSpec("target")
      lazy val perPage = IntegerAttributeSpec("perPage")
      lazy val sort = MapAttributeSpec("sort")
      lazy val filter = MapAttributeSpec("filter")
    }
  }

  object RichTextField {
    implicit class RichTextFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val RichTextField: ReactClassElementSpec = elements(NativeRichTextField)
    }

    implicit class RichTextFieldVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
      lazy val stripTags = BooleanAttributeSpec("stripTags")
    }
  }

  object UrlField {
    implicit class UrlFieldVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val UrlField: ReactClassElementSpec = elements(NativeUrlField)
    }
  }

  object TextInput {
    implicit class TextInputVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val TextInput: ReactClassElementSpec = elements(NativeTextInput)
    }
  }

  object AutocompleteInput {
    implicit class AutocompleteInputVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val AutocompleteInput: ReactClassElementSpec = elements(NativeAutocompleteInput)
    }
  }

  object ReferenceInput {
    implicit class ReferenceInputVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val ReferenceInput: ReactClassElementSpec = elements(NativeReferenceInput)
    }

    implicit class ReferenceInputVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
      lazy val reference = StringAttributeSpec("reference")
    }
  }

  object SelectInput {
    implicit class SelectInputVirtualDOMElements(elements: VirtualDOMElements) {
      lazy val SelectInput: ReactClassElementSpec = elements(NativeSelectInput)
    }

    implicit class SelectInputVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
      lazy val choices = ChoicesAttributeSpec("choices")
    }
  }

  implicit class FieldVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val source = StringAttributeSpec("source")
    lazy val label = StringAttributeSpec("label")
    lazy val addLabel = BooleanAttributeSpec("addLabel")
    lazy val sortable = BooleanAttributeSpec("sortable")
    lazy val style = CssAttributeSpec("style")
    lazy val elStyle = CssAttributeSpec("elStyle")
    lazy val alwaysOn = BooleanAttributeSpec("alwaysOn")
  }

}

@js.native
@JSImport("admin-on-rest", "SingleFieldList")
object NativeSingleFieldList extends ReactClass

object SingleFieldList {
  implicit class SingleFieldListVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val SingleFieldList: ReactClassElementSpec = elements(NativeSingleFieldList)
  }
}
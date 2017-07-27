package org.make.backoffice.libs

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.Router.RouterVirtualDOMAttributes.LocationAttributeSpec
import io.github.shogowada.statictags._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "Admin")
object NativeAdmin extends ReactClass

object Admin {

  case class RestClientAttributeSpec(name: String) extends AttributeSpec {
    def :=(restClient: js.Dynamic): Attribute[js.Dynamic] =
      Attribute(name = name, value = restClient, AS_IS)
  }

  case class CustomRoutesAttributesSpec(name : String) extends AttributeSpec {
    def := (customRoutes: js.Array[ReactElement]): Attribute[js.Array[ReactElement]] =
      Attribute(name = name, value = customRoutes, AS_IS)
  }

  implicit class AdminVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Admin: ReactClassElementSpec = elements(NativeAdmin)
  }

  implicit class AdminVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val title = StringAttributeSpec("title")
    lazy val menu = ReactClassAttributeSpec("menu")
    lazy val loginPage = StringAttributeSpec("loginPage")
    lazy val customRoutes = CustomRoutesAttributesSpec("customRoutes")
    lazy val restClient = RestClientAttributeSpec("restClient")
  }

}

@js.native
@JSImport("admin-on-rest", "default")
object JsonServerRestClient extends js.Object {
  def jsonServerRestClient(apiUrl: String): js.Dynamic = js.native
}

@js.native
@JSImport("admin-on-rest", "Resource")
object NativeResource extends ReactClass

object Resource {

  implicit class ResourceVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Resource: ReactClassElementSpec = elements(NativeResource)
  }

  implicit class ResourceVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val name = StringAttributeSpec("name")
    lazy val showList = ReactClassAttributeSpec("list")
    lazy val create = ReactClassAttributeSpec("create")
    lazy val edit = ReactClassAttributeSpec("edit")
    lazy val show = ReactClassAttributeSpec("show")
    lazy val remove = ReactClassAttributeSpec("remove")
  }

}

@js.native
@JSImport("admin-on-rest", "List")
object NativeList extends ReactClass

object List {

  implicit class ListVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val List: ReactClassElementSpec = elements(NativeList)
  }

  implicit class ListVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val title = StringAttributeSpec("title")
    lazy val actions = ElementAttributeSpec("actions")
    lazy val filters = ElementAttributeSpec("filters")
    lazy val perPage = IntegerAttributeSpec("perPage")
    lazy val sort = MapAttributeSpec("sort")
    lazy val sortable = BooleanAttributeSpec("sortable")
    lazy val filter = MapAttributeSpec("filter")
    lazy val location = LocationAttributeSpec("location")
    lazy val resource = StringAttributeSpec("resource")
    lazy val hasCreate = BooleanAttributeSpec("hasCreate")
  }
}


@js.native
@JSImport("admin-on-rest", "Filters")
object NativeFilters extends ReactClass

object Filters {

  implicit class FiltersVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Filters: ReactClassElementSpec = elements(NativeFilters)
  }

  implicit class FiltersVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val label = StringAttributeSpec("label")
    lazy val source = StringAttributeSpec("source")
    lazy val alwaysOn = TrueOrFalseAttributeSpec("alwaysOn")
  }
}

@js.native
@JSImport("admin-on-rest", "Datagrid")
object NativeDatagrid extends ReactClass

object Datagrid {

  implicit class DatagridVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Datagrid: ReactClassElementSpec = elements(NativeDatagrid)
  }

  implicit class DatagridVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val styles = CssAttributeSpec("styles")
    lazy val options = MapAttributeSpec("options")
    lazy val headerOptions = MapAttributeSpec("headerOptions")
    lazy val bodyOptions = MapAttributeSpec("bodyOptions")
    lazy val rowOptions = MapAttributeSpec("rowOptions")
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

@js.native
@JSImport("admin-on-rest", "Create")
object NativeCreate extends ReactClass

object Create {
  implicit class CreateVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Create: ReactClassElementSpec = elements(NativeCreate)
  }

  implicit class CreateVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val title = StringAttributeSpec("title")
    lazy val actions = ElementAttributeSpec("actions")
    lazy val resource = StringAttributeSpec("resource")
    lazy val location = LocationAttributeSpec("location")
  }
}

@js.native
@JSImport("admin-on-rest", "EditButton")
object NativeEditButton extends ReactClass

object EditButton{
  implicit class EditButtonVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val EditButton: ReactClassElementSpec = elements(NativeEditButton)
  }
}

@js.native
@JSImport("admin-on-rest", "Edit")
object NativeEdit extends ReactClass

object Edit {
  implicit class EditVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Edit: ReactClassElementSpec = elements(NativeEdit)
  }

  implicit class EditVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val title = StringAttributeSpec("title")
    lazy val actions = ElementAttributeSpec("actions")
    lazy val resource = StringAttributeSpec("resource")
    lazy val location = LocationAttributeSpec("location")
  }
}

@js.native
@JSImport("admin-on-rest", "SimpleForm")
object NativeSimpleForm extends ReactClass

object SimpleForm {
  implicit class SimpleFormVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val SimpleForm: ReactClassElementSpec = elements(NativeSimpleForm)
  }
}

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
@JSImport("admin-on-rest", "ShowButton")
object NativeShowButton extends ReactClass

object ShowButton {
  implicit class ShowButtonVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ShowButton: ReactClassElementSpec = elements(NativeShowButton)
  }
}

@js.native
@JSImport("admin-on-rest", "Delete")
object NativeDelete extends ReactClass

object Delete {
  implicit class DeleteVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Delete: ReactClassElementSpec = elements(NativeDelete)
  }
}

@js.native
@JSImport("admin-on-rest", "DeleteButton")
object NativeDeleteButton extends ReactClass

object DeleteButton {
  implicit class DeleteButtonVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val DeleteButton: ReactClassElementSpec = elements(NativeDeleteButton)
  }
}

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
@JSImport("admin-on-rest", "Filter")
object NativeFilter extends ReactClass

object Filter {
  implicit class FilterVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Filter: ReactClassElementSpec = elements(NativeFilter)
  }

  implicit class FilterVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val context = StringAttributeSpec("context")
  }
}

case class ReactClassAttributeSpec(name: String) extends AttributeSpec {
  def :=(element: ReactClass): Attribute[ReactClass] =
    Attribute(name = name, value = element, AS_IS)
}

case class ElementAttributeSpec(name: String) extends AttributeSpec {
  def :=(element: ReactElement): Attribute[ReactElement] =
    Attribute(name = name, value = element, AS_IS)
}

case class MapAttributeSpec(name: String) extends AttributeSpec {
  def :=(sort: Map[String, _]): Attribute[Map[String, _]] =
    Attribute(name = name, value = sort, AS_IS)
}

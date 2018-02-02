package org.make.backoffice.facades.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags._
import org.make.backoffice.facades.MaterialUi.IntAttributeSpec
import org.make.backoffice.facades.{
  ElementAttributeSpec,
  LocationAttributeSpec,
  MapArrayAttributeSpec,
  MapStringAttributeSpec
}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

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
    lazy val perPage = IntAttributeSpec("perPage")
    lazy val sort = MapStringAttributeSpec("sort")
    lazy val filter = MapArrayAttributeSpec("filter")
    lazy val location = LocationAttributeSpec("location")
    lazy val resource = StringAttributeSpec("resource")
    lazy val hasCreate = BooleanAttributeSpec("hasCreate")
  }
}

@js.native
@JSImport("admin-on-rest", "Datagrid")
object NativeDatagrid extends ReactClass

object Datagrid {

  type RowStyle = js.Function2[js.Object, Int, js.Dictionary[js.Any]]

  case class RowStyleAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: RowStyle): Attribute[RowStyle] =
      Attribute(name = name, value = element, AS_IS)
  }

  implicit class DatagridVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Datagrid: ReactClassElementSpec = elements(NativeDatagrid)
  }

  implicit class DatagridVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val styles = CssAttributeSpec("styles")
    lazy val rowStyle = RowStyleAttributeSpec("rowStyle")
    lazy val headerOptions = MapStringAttributeSpec("headerOptions")
    lazy val bodyOptions = MapStringAttributeSpec("bodyOptions")
    lazy val rowOptions = MapStringAttributeSpec("rowOptions")
  }
}

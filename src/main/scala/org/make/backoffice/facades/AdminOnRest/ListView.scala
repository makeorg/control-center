package org.make.backoffice.facades.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.Router.RouterVirtualDOMAttributes.LocationAttributeSpec
import io.github.shogowada.statictags._
import org.make.backoffice.facades.{ElementAttributeSpec, MapAttributeSpec}

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



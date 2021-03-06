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
import org.make.backoffice.facade.MaterialUi.IntAttributeSpec
import org.make.backoffice.facade.{
  ElementAttributeSpec,
  FunctionAttributeSpec,
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
    lazy val context: StringAttributeSpec = StringAttributeSpec("context")
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
    lazy val label: StringAttributeSpec = StringAttributeSpec("label")
    lazy val source: StringAttributeSpec = StringAttributeSpec("source")
    lazy val alwaysOn: TrueOrFalseAttributeSpec = TrueOrFalseAttributeSpec("alwaysOn")
  }
}

@js.native
@JSImport("admin-on-rest", "List")
object NativeList extends ReactClass

@js.native
@JSImport("admin-on-rest", "Pagination")
object NativePagination extends ReactClass

object List {

  implicit class ListVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val List: ReactClassElementSpec = elements(NativeList)
  }

  implicit class ListVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val title: StringAttributeSpec = StringAttributeSpec("title")
    lazy val actions: ElementAttributeSpec = ElementAttributeSpec("actions")
    lazy val filters: ElementAttributeSpec = ElementAttributeSpec("filters")
    lazy val perPage: IntAttributeSpec = IntAttributeSpec("perPage")
    lazy val sortList: MapStringAttributeSpec = MapStringAttributeSpec("sort")
    lazy val filter: MapArrayAttributeSpec = MapArrayAttributeSpec("filter")
    lazy val location: LocationAttributeSpec = LocationAttributeSpec("location")
    lazy val resource: StringAttributeSpec = StringAttributeSpec("resource")
    lazy val hasCreate: BooleanAttributeSpec = BooleanAttributeSpec("hasCreate")
    lazy val pagination: ElementAttributeSpec = ElementAttributeSpec("pagination")
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
    lazy val styles: CssAttributeSpec = CssAttributeSpec("styles")
    lazy val rowStyle: RowStyleAttributeSpec = RowStyleAttributeSpec("rowStyle")
    lazy val headerOptions: MapStringAttributeSpec = MapStringAttributeSpec("headerOptions")
    lazy val bodyOptions: MapStringAttributeSpec = MapStringAttributeSpec("bodyOptions")
    lazy val rowOptions: MapStringAttributeSpec = MapStringAttributeSpec("rowOptions")
  }
}

object Pagination {

  implicit class PaginationVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Pagination: ReactClassElementSpec = elements(NativePagination)
  }

  implicit class DatagridVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val page: IntAttributeSpec = IntAttributeSpec("page")
    lazy val total: IntAttributeSpec = IntAttributeSpec("total")
    lazy val setPage: FunctionAttributeSpec = FunctionAttributeSpec("setPage")
  }

}

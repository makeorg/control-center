package org.make.backoffice.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@js.native
@JSImport("material-ui", "Card")
object NativeCard extends ReactClass

@js.native
@JSImport("material-ui", "Table")
object NativeTable extends ReactClass

@js.native
@JSImport("material-ui", "TableRow")
object NativeTableRow extends ReactClass

@js.native
@JSImport("material-ui", "TableRowColumn")
object NativeTableRowColumn extends ReactClass

@js.native
@JSImport("material-ui", "TableHeader")
object NativeTableHeader extends ReactClass

@js.native
@JSImport("material-ui", "TableHeaderColumn")
object NativeTableHeaderColumn extends ReactClass

@js.native
@JSImport("material-ui", "TableBody")
object NativeTableBody extends ReactClass

@js.native
@JSImport("material-ui", "TableFooter")
object NativeTableFooter extends ReactClass

object MaterialUi {
  type OnRowColumnInteraction = js.Function2[Int, Int, Unit]
  type OnRowInteraction = js.Function1[Int, Unit]
  type OnRowSelection = js.Function2[Int, Seq[Int] | String, Unit]

  case class OnRowColumnAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnRowColumnInteraction): Attribute[OnRowColumnInteraction] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class OnRowAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnRowInteraction): Attribute[OnRowInteraction] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class OnRowSelectionAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnRowSelection): Attribute[OnRowSelection] =
      Attribute(name = name, value = element, AS_IS)
  }

  implicit class MaterialUiVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Card: ReactClassElementSpec = elements(NativeCard)
    lazy val Table: ReactClassElementSpec = elements(NativeTable)
    lazy val TableRow: ReactClassElementSpec = elements(NativeTableRow)
    lazy val TableRowColumn: ReactClassElementSpec = elements(NativeTableRowColumn)
    lazy val TableHeader: ReactClassElementSpec = elements(NativeTableHeader)
    lazy val TableHeaderColumn: ReactClassElementSpec = elements(NativeTableHeaderColumn)
    lazy val TableBody: ReactClassElementSpec = elements(NativeTableBody)
    lazy val TableFooter: ReactClassElementSpec = elements(NativeTableFooter)
  }

  implicit class MaterialUiVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val allRowsSelected = BooleanAttributeSpec("allRowsSelected")
    lazy val bodyStyle = MapAttributeSpec("bodyStyle")
    lazy val fixedFooter = BooleanAttributeSpec("fixedFooter")
    lazy val fixedHeader = BooleanAttributeSpec("fixedHeader")
    lazy val footerStyle = MapAttributeSpec("footerStyle")
    lazy val headerStyle = MapAttributeSpec("headerStyle")
    lazy val height = StringAttributeSpec("height")
    lazy val multiSelectable = BooleanAttributeSpec("multiSelectable")
    lazy val onCellClick = OnRowColumnAttributeSpec("onCellClick")
    lazy val onCellHover = OnRowColumnAttributeSpec("onCellHover")
    lazy val onCellHoverExit = OnRowColumnAttributeSpec("onCellHoverExit")
    lazy val onRowHover = OnRowAttributeSpec("onRowHover")
    lazy val onRowHoverExit = OnRowAttributeSpec("onRowHoverExit")
    lazy val onRowSelection = OnRowSelectionAttributeSpec("onRowSelection")
    lazy val wrapperStyle = MapAttributeSpec("wrapperStyle")
    lazy val children = ElementAttributeSpec("children")
    lazy val className = StringAttributeSpec("className")
    lazy val displayBorder = BooleanAttributeSpec("displayBorder")
    lazy val hoverable = BooleanAttributeSpec("hoverable")
    lazy val hovered = MapAttributeSpec("hovered")
    lazy val rowNumber = IntegerAttributeSpec("rowNumber")
    lazy val selectable = BooleanAttributeSpec("selectable")
    lazy val selected = BooleanAttributeSpec("selected")
    lazy val striped = BooleanAttributeSpec("striped")
    lazy val style = MapAttributeSpec("style")
    lazy val adjustForCheckbox = BooleanAttributeSpec("adjustForCheckbox")
    lazy val displaySelectAll = BooleanAttributeSpec("displaySelectAll")
    lazy val enableSelectAll = BooleanAttributeSpec("enableSelectAll")
    lazy val columnNumber = IntegerAttributeSpec("columnNumber")
    lazy val tooltip = StringAttributeSpec("tooltip")
    lazy val tooltipStyle = MapAttributeSpec("tooltipStyle")
    lazy val deselectOnClickaway = BooleanAttributeSpec("deselectOnClickaway")
    lazy val displayRowCheckbox = BooleanAttributeSpec("displayRowCheckbox")
    lazy val preScanRows = BooleanAttributeSpec("preScanRows")
    lazy val showRowHover = BooleanAttributeSpec("showRowHover")
    lazy val stripedRows = BooleanAttributeSpec("stripedRows")
  }
}

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

object Card {
  implicit class CardVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Card: ReactClassElementSpec = elements(NativeCard)
  }
}

@js.native
@JSImport("material-ui", "Table")
object NativeTable extends ReactClass

object Table {
  type OnRowColumnInteraction = js.Function2[Int, Int, Unit]
  type OnRowInteraction = js.Function1[Int, Unit]
  type OnRowSelection = js.Function2[Int, Seq[Int] | String, Unit]

  implicit class TableVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Table: ReactClassElementSpec = elements(NativeTable)
  }

  implicit class TableVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val allRowsSelected = BooleanAttributeSpec("allRowsSelected")
    lazy val bodyStyle = MapAttributeSpec("bodyStyle")
    lazy val children = ElementAttributeSpec("children")
    lazy val className = StringAttributeSpec("className")
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
    lazy val selectable = BooleanAttributeSpec("selectable")
    lazy val style = MapAttributeSpec("style")
    lazy val wrapperStyle = MapAttributeSpec("wrapperStyle")
  }

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
}

@js.native
@JSImport("material-ui", "TableRow")
object NativeTableRow extends ReactClass

object TableRow {
  implicit class TableRowVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TableRow: ReactClassElementSpec = elements(NativeTableRow)
  }

  implicit class TableRowVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
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
  }
}

@js.native
@JSImport("material-ui", "TableRowColumn")
object NativeTableRowColumn extends ReactClass

object TableRowColumn {
  implicit class TableRowColumnVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TableRowColumn: ReactClassElementSpec = elements(NativeTableRowColumn)
  }

  implicit class TableRowColumnVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val children = ElementAttributeSpec("children")
    lazy val className = StringAttributeSpec("className")
    lazy val style = MapAttributeSpec("style")
  }
}

@js.native
@JSImport("material-ui", "TableHeader")
object NativeTableHeader extends ReactClass

object TableHeader {
  implicit class TableHeaderVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TableHeader: ReactClassElementSpec = elements(NativeTableHeader)
  }

  implicit class TableHeaderVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val adjustForCheckbox = BooleanAttributeSpec("adjustForCheckbox")
    lazy val children = ElementAttributeSpec("children")
    lazy val className = StringAttributeSpec("className")
    lazy val displaySelectAll = BooleanAttributeSpec("displaySelectAll")
    lazy val enableSelectAll = BooleanAttributeSpec("enableSelectAll")
    lazy val style = MapAttributeSpec("style")
  }
}

@js.native
@JSImport("material-ui", "TableHeaderColumn")
object NativeTableHeaderColumn extends ReactClass

object TableHeaderColumn {
  implicit class TableHeaderColumnVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TableHeaderColumn: ReactClassElementSpec = elements(NativeTableHeaderColumn)
  }

  implicit class TableHeaderColumnVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val children = ElementAttributeSpec("children")
    lazy val className = StringAttributeSpec("className")
    lazy val columnNumber = IntegerAttributeSpec("columnNumber")
    lazy val style = MapAttributeSpec("style")
    lazy val tooltip = StringAttributeSpec("tooltip")
    lazy val tooltipStyle = MapAttributeSpec("tooltipStyle")
  }
}

@js.native
@JSImport("material-ui", "TableBody")
object NativeTableBody extends ReactClass

object TableBody {
  implicit class TableBodyVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TableBody: ReactClassElementSpec = elements(NativeTableBody)
  }

  implicit class TableBodyVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val children = ElementAttributeSpec("children")
    lazy val className = StringAttributeSpec("className")
    lazy val deselectOnClickaway = BooleanAttributeSpec("deselectOnClickaway")
    lazy val displayRowCheckbox = BooleanAttributeSpec("displayRowCheckbox")
    lazy val preScanRows = BooleanAttributeSpec("preScanRows")
    lazy val showRowHover = BooleanAttributeSpec("showRowHover")
    lazy val stripedRows = BooleanAttributeSpec("stripedRows")
    lazy val style = MapAttributeSpec("style")
  }
}

@js.native
@JSImport("material-ui", "TableFooter")
object NativeTableFooter extends ReactClass

object TableFooter {
  implicit class TableFooterVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TableFooter: ReactClassElementSpec = elements(NativeTableFooter)
  }

  implicit class TableFooterVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val children = ElementAttributeSpec("children")
    lazy val className = StringAttributeSpec("className")
    lazy val style = MapAttributeSpec("style")
  }
}

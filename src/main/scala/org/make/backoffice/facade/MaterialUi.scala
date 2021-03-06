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

package org.make.backoffice.facade

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import io.github.shogowada.statictags._
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|
import scala.scalajs.js.JSConverters._

@js.native
@JSImport("material-ui", "Card")
object NativeCard extends ReactClass

@js.native
@JSImport("material-ui", "CardHeader")
object NativeCardHeader extends ReactClass

@js.native
@JSImport("material-ui", "CardTitle")
object NativeCardTitle extends ReactClass

@js.native
@JSImport("material-ui", "CardActions")
object NativeCardActions extends ReactClass

@js.native
@JSImport("material-ui", "CardText")
object NativeCardText extends ReactClass

@js.native
@JSImport("material-ui", "Chip")
object NativeChip extends ReactClass

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

@js.native
@JSImport("material-ui", "SelectField")
object NativeSelectField extends ReactClass

@js.native
@JSImport("material-ui", "MenuItem")
object NativeMenuItem extends ReactClass

@js.native
@JSImport("material-ui", "AutoComplete")
object NativeAutoComplete extends ReactClass

@js.native
@JSImport("material-ui", "Snackbar")
object NativeSnackbar extends ReactClass

@js.native
trait DataSourceConfig extends js.Object {
  val text: String
  val value: String
}

object DataSourceConfig {
  def apply(text: String, value: String): DataSourceConfig =
    js.Dynamic.literal(text = text, value = value).asInstanceOf[DataSourceConfig]
}

@js.native
@JSImport("material-ui", "FlatButton")
object NativeFlatButton extends ReactClass

@js.native
@JSImport("material-ui", "RaisedButton")
object NativeRaisedButton extends ReactClass

@js.native
@JSImport("material-ui", "TextField")
object NativeTextField extends ReactClass

@js.native
@JSImport("material-ui", "Checkbox")
object NativeCheckbox extends ReactClass

@js.native
@JSImport("material-ui", "Dialog")
object NativeDialog extends ReactClass

@js.native
@JSImport("material-ui", "CircularProgress")
object NativeCircularProgress extends ReactClass

@js.native
@JSImport("material-ui", "Divider")
object NativeDivider extends ReactClass

@js.native
@JSImport("material-ui", "SvgIcon")
object NativeSvgIcon extends ReactClass

@js.native
@JSImport("material-ui", "Toggle")
object NativeToggle extends ReactClass

@js.native
@JSImport("material-ui", "Tabs")
object NativeTabs extends ReactClass

@js.native
@JSImport("material-ui", "Tab")
object NativeTab extends ReactClass

@js.native
@JSImport("material-ui", "Toolbar")
object NativeToolbar extends ReactClass

@js.native
@JSImport("material-ui", "ToolbarGroup")
object NativeToolbarGroup extends ReactClass

@js.native
trait Event extends js.Object {
  val target: Element = js.native
}

object MaterialUi {
  type OnRowColumnInteraction = js.Function2[Int, Int, Unit]
  type OnRowInteraction = js.Function1[Int, Unit]
  type OnRowSelection = js.Function1[js.Array[Int] | String, Unit]
  type OnChangeMultipleSelect = js.Function3[js.Object, js.UndefOr[Int], js.Array[String], Unit]
  type OnChangeSelect = js.Function3[js.Object, js.UndefOr[Int], String, Unit]
  type OnChangeReference = js.Function2[js.Object, js.UndefOr[String], Unit]
  type OnChangeTextField = js.Function2[js.Object, String, Unit]
  type FilterAutoComplete = js.Function2[String, String, Boolean]
  type BaseFunction0 = js.Function0[Unit]
  type OnNewRequest = js.Function2[js.Object, Int, Unit]
  type OnRequestClose = js.Function1[String, Unit]
  type OnUpdateInput = js.Function3[String, js.Array[js.Object], OnUpdateInputParams, Unit]
  type OnCheck = js.Function2[FormSyntheticEvent[HTMLInputElement], Boolean, Unit]

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

  case class OnChangeMultipleSelectAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnChangeMultipleSelect): Attribute[OnChangeMultipleSelect] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class OnChangeSelectAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnChangeSelect): Attribute[OnChangeSelect] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class OnChangeReferenceAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnChangeReference): Attribute[OnChangeReference] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class OnChangeTextFieldAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnChangeTextField): Attribute[OnChangeTextField] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class OnCheckAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnCheck): Attribute[OnCheck] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class ValueSelectAttributeSpec(name: String) extends AttributeSpec {
    def :=(elements: Seq[String]): Attribute[js.Array[String]] =
      Attribute(name = name, value = elements.toJSArray, AS_IS)
  }

  case class ValueFloatAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: Float): Attribute[Float] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class DataSourceAttributeSpec(name: String) extends AttributeSpec {
    def :=(elements: Seq[js.Object]): Attribute[js.Array[js.Object]] =
      Attribute(name = name, value = elements.toJSArray, AS_IS)
  }

  case class DataSourceConfigAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: DataSourceConfig): Attribute[DataSourceConfig] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class FilterAutoCompleteAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: FilterAutoComplete): Attribute[FilterAutoComplete] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class BaseFunction0AttributeSpec(name: String) extends AttributeSpec {
    def :=(element: BaseFunction0): Attribute[BaseFunction0] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class OnNewRequestAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnNewRequest): Attribute[OnNewRequest] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class OnRequestCloseAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnRequestClose): Attribute[OnRequestClose] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class OnUpdateInputAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnUpdateInput): Attribute[OnUpdateInput] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class ElementListAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: Seq[ReactElement]): Attribute[js.Array[ReactElement]] =
      Attribute(name = name, value = element.toJSArray, AS_IS)
  }

  case class IntAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: Int): Attribute[Int] =
      Attribute(name = name, value = element, AS_IS)
  }

  implicit class MaterialUiVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val AutoComplete: ReactClassElementSpec = elements(NativeAutoComplete)
    lazy val Card: ReactClassElementSpec = elements(NativeCard)
    lazy val CardActions: ReactClassElementSpec = elements(NativeCardActions)
    lazy val CardHeader: ReactClassElementSpec = elements(NativeCardHeader)
    lazy val CardText: ReactClassElementSpec = elements(NativeCardText)
    lazy val CardTitle: ReactClassElementSpec = elements(NativeCardTitle)
    lazy val Checkbox: ReactClassElementSpec = elements(NativeCheckbox)
    lazy val Chip: ReactClassElementSpec = elements(NativeChip)
    lazy val CircularProgress: ReactClassElementSpec = elements(NativeCircularProgress)
    lazy val Dialog: ReactClassElementSpec = elements(NativeDialog)
    lazy val Divider: ReactClassElementSpec = elements(NativeDivider)
    lazy val FlatButton: ReactClassElementSpec = elements(NativeFlatButton)
    lazy val MenuItem: ReactClassElementSpec = elements(NativeMenuItem)
    lazy val RaisedButton: ReactClassElementSpec = elements(NativeRaisedButton)
    lazy val SelectField: ReactClassElementSpec = elements(NativeSelectField)
    lazy val Snackbar: ReactClassElementSpec = elements(NativeSnackbar)
    lazy val SvgIcon: ReactClassElementSpec = elements(NativeSvgIcon)
    lazy val Tab: ReactClassElementSpec = elements(NativeTab)
    lazy val Table: ReactClassElementSpec = elements(NativeTable)
    lazy val TableRow: ReactClassElementSpec = elements(NativeTableRow)
    lazy val TableRowColumn: ReactClassElementSpec = elements(NativeTableRowColumn)
    lazy val TableHeader: ReactClassElementSpec = elements(NativeTableHeader)
    lazy val TableHeaderColumn: ReactClassElementSpec = elements(NativeTableHeaderColumn)
    lazy val TableBody: ReactClassElementSpec = elements(NativeTableBody)
    lazy val TableFooter: ReactClassElementSpec = elements(NativeTableFooter)
    lazy val Tabs: ReactClassElementSpec = elements(NativeTabs)
    lazy val TextFieldMaterialUi: ReactClassElementSpec = elements(NativeTextField)
    lazy val Toggle: ReactClassElementSpec = elements(NativeToggle)
    lazy val Toolbar: ReactClassElementSpec = elements(NativeToolbar)
    lazy val ToolbarGroup: ReactClassElementSpec = elements(NativeToolbarGroup)
  }

  implicit class MaterialUiVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val actAsExpander: BooleanAttributeSpec = BooleanAttributeSpec("actAsExpander")
    lazy val actionsModal: ElementListAttributeSpec = ElementListAttributeSpec("actions")
    lazy val adjustForCheckbox: BooleanAttributeSpec = BooleanAttributeSpec("adjustForCheckbox")
    lazy val allRowsSelected: BooleanAttributeSpec = BooleanAttributeSpec("allRowsSelected")
    lazy val animated: BooleanAttributeSpec = BooleanAttributeSpec("animated")
    lazy val autoHideDuration: IntAttributeSpec = IntAttributeSpec("autoHideDuration")
    lazy val autoScrollBodyContent: BooleanAttributeSpec = BooleanAttributeSpec("autoScrollBodyContent")
    lazy val bodyStyle: MapStringAttributeSpec = MapStringAttributeSpec("bodyStyle")
    lazy val buttonIcon: ElementAttributeSpec = ElementAttributeSpec("icon")
    lazy val checkedIcon: ElementAttributeSpec = ElementAttributeSpec("checkedIcon")
    lazy val children: ElementAttributeSpec = ElementAttributeSpec("children")
    lazy val className: StringAttributeSpec = StringAttributeSpec("className")
    lazy val color: StringAttributeSpec = StringAttributeSpec("color")
    lazy val columnNumber: IntegerAttributeSpec = IntegerAttributeSpec("columnNumber")
    lazy val containerElement: ElementAttributeSpec = ElementAttributeSpec("containerElement")
    lazy val dataSource: DataSourceAttributeSpec = DataSourceAttributeSpec("dataSource")
    lazy val dataSourceConfig: DataSourceConfigAttributeSpec = DataSourceConfigAttributeSpec("dataSourceConfig")
    lazy val deselectOnClickaway: BooleanAttributeSpec = BooleanAttributeSpec("deselectOnClickaway")
    lazy val disableFocusRipple: BooleanAttributeSpec = BooleanAttributeSpec("disableFocusRipple")
    lazy val displayBorder: BooleanAttributeSpec = BooleanAttributeSpec("displayBorder")
    lazy val displayRowCheckbox: BooleanAttributeSpec = BooleanAttributeSpec("displayRowCheckbox")
    lazy val displaySelectAll: BooleanAttributeSpec = BooleanAttributeSpec("displaySelectAll")
    lazy val enableSelectAll: BooleanAttributeSpec = BooleanAttributeSpec("enableSelectAll")
    lazy val errorStyle: MapStringAttributeSpec = MapStringAttributeSpec("errorStyle")
    lazy val errorText: StringAttributeSpec = StringAttributeSpec("errorText")
    lazy val expandable: BooleanAttributeSpec = BooleanAttributeSpec("expandable")
    lazy val expanded: BooleanAttributeSpec = BooleanAttributeSpec("expanded")
    lazy val filterAutoComplete: FilterAutoCompleteAttributeSpec = FilterAutoCompleteAttributeSpec("filter")
    lazy val firstChild: BooleanAttributeSpec = BooleanAttributeSpec("firstChild")
    lazy val fixedFooter: BooleanAttributeSpec = BooleanAttributeSpec("fixedFooter")
    lazy val fixedHeader: BooleanAttributeSpec = BooleanAttributeSpec("fixedHeader")
    lazy val floatingLabelFixed: BooleanAttributeSpec = BooleanAttributeSpec("floatingLabelFixed")
    lazy val floatingLabelText: StringAttributeSpec = StringAttributeSpec("floatingLabelText")
    lazy val footerStyle: MapStringAttributeSpec = MapStringAttributeSpec("footerStyle")
    lazy val fullWidth: BooleanAttributeSpec = BooleanAttributeSpec("fullWidth")
    lazy val headerStyle: MapStringAttributeSpec = MapStringAttributeSpec("headerStyle")
    lazy val height: StringAttributeSpec = StringAttributeSpec("height")
    lazy val hintText: StringAttributeSpec = StringAttributeSpec("hintText")
    lazy val hoverable: BooleanAttributeSpec = BooleanAttributeSpec("hoverable")
    lazy val hovered: MapStringAttributeSpec = MapStringAttributeSpec("hovered")
    lazy val iconStyle: MapStringAttributeSpec = MapStringAttributeSpec("iconStyle")
    lazy val inputStyle: CssAttributeSpec = CssAttributeSpec("inputStyle")
    lazy val insetChildren: BooleanAttributeSpec = BooleanAttributeSpec("insetChildren")
    lazy val listStyle: MapStringAttributeSpec = MapStringAttributeSpec("listStyle")
    lazy val labelPosition: StringAttributeSpec = StringAttributeSpec("labelPosition")
    lazy val labelStyle: MapStringAttributeSpec = MapStringAttributeSpec("labelStyle")
    lazy val maxSearchResults: IntegerAttributeSpec = IntegerAttributeSpec("maxSearchResults")
    lazy val menuCloseDelay: IntegerAttributeSpec = IntegerAttributeSpec("menuCloseDelay")
    lazy val menuProps: MapIntAttributeSpec = MapIntAttributeSpec("menuProps")
    lazy val menuStyle: MapStringAttributeSpec = MapStringAttributeSpec("menuStyle")
    lazy val message: StringAttributeSpec = StringAttributeSpec("message")
    lazy val modal: BooleanAttributeSpec = BooleanAttributeSpec("modal")
    lazy val multiSelectable: BooleanAttributeSpec = BooleanAttributeSpec("multiSelectable")
    lazy val name: StringAttributeSpec = StringAttributeSpec("name")
    lazy val onCellClick: OnRowColumnAttributeSpec = OnRowColumnAttributeSpec("onCellClick")
    lazy val onCellHover: OnRowColumnAttributeSpec = OnRowColumnAttributeSpec("onCellHover")
    lazy val onCellHoverExit: OnRowColumnAttributeSpec = OnRowColumnAttributeSpec("onCellHoverExit")
    lazy val onChangeMultipleSelect: OnChangeMultipleSelectAttributeSpec = OnChangeMultipleSelectAttributeSpec(
      "onChange"
    )
    lazy val onChangeSelect: OnChangeSelectAttributeSpec = OnChangeSelectAttributeSpec("onChange")
    lazy val onChangeReference: OnChangeReferenceAttributeSpec = OnChangeReferenceAttributeSpec("onChange")
    lazy val onChangeTextField: OnChangeTextFieldAttributeSpec = OnChangeTextFieldAttributeSpec("onChange")
    lazy val onCheck: OnCheckAttributeSpec = OnCheckAttributeSpec("onCheck")
    lazy val onClose: BaseFunction0AttributeSpec = BaseFunction0AttributeSpec("onClose")
    lazy val onExpandChange: FunctionAttributeSpec = FunctionAttributeSpec("onExpandChange")
    lazy val onNewRequest: OnNewRequestAttributeSpec = OnNewRequestAttributeSpec("onNewRequest")
    lazy val onRequestClose: OnRequestCloseAttributeSpec = OnRequestCloseAttributeSpec("onRequestClose")
    lazy val onRowHover: OnRowAttributeSpec = OnRowAttributeSpec("onRowHover")
    lazy val onRowHoverExit: OnRowAttributeSpec = OnRowAttributeSpec("onRowHoverExit")
    lazy val onRowSelection: OnRowSelectionAttributeSpec = OnRowSelectionAttributeSpec("onRowSelection")
    lazy val onToggle: OnCheckAttributeSpec = OnCheckAttributeSpec("onToggle")
    lazy val onUpdateInput: OnUpdateInputAttributeSpec = OnUpdateInputAttributeSpec("onUpdateInput")
    lazy val open: BooleanAttributeSpec = BooleanAttributeSpec("open")
    lazy val openOnFocus: BooleanAttributeSpec = BooleanAttributeSpec("openOnFocus")
    lazy val popoverProps: MapBooleanAttributeSpec = MapBooleanAttributeSpec("popoverProps")
    lazy val preScanRows: BooleanAttributeSpec = BooleanAttributeSpec("preScanRows")
    lazy val primary: BooleanAttributeSpec = BooleanAttributeSpec("primary")
    lazy val primaryText: StringAttributeSpec = StringAttributeSpec("primaryText")
    lazy val readOnly: BooleanAttributeSpec = BooleanAttributeSpec("readOnly")
    lazy val rowNumber: IntegerAttributeSpec = IntegerAttributeSpec("rowNumber")
    lazy val searchText: StringAttributeSpec = StringAttributeSpec("searchText")
    lazy val secondary: BooleanAttributeSpec = BooleanAttributeSpec("secondary")
    lazy val selectable: BooleanAttributeSpec = BooleanAttributeSpec("selectable")
    lazy val selected: BooleanAttributeSpec = BooleanAttributeSpec("selected")
    lazy val showExpandableButton: BooleanAttributeSpec = BooleanAttributeSpec("showExpandableButton")
    lazy val showRowHover: BooleanAttributeSpec = BooleanAttributeSpec("showRowHover")
    lazy val striped: BooleanAttributeSpec = BooleanAttributeSpec("striped")
    lazy val stripedRows: BooleanAttributeSpec = BooleanAttributeSpec("stripedRows")
    lazy val style: MapStringAttributeSpec = MapStringAttributeSpec("style")
    lazy val subtitle: StringAttributeSpec = StringAttributeSpec("subtitle")
    lazy val textFieldStyle: MapStringAttributeSpec = MapStringAttributeSpec("textFieldStyle")
    lazy val toggled: BooleanAttributeSpec = BooleanAttributeSpec("toggled")
    lazy val tooltip: StringAttributeSpec = StringAttributeSpec("tooltip")
    lazy val tooltipStyle: MapStringAttributeSpec = MapStringAttributeSpec("tooltipStyle")
    lazy val underlineShow: BooleanAttributeSpec = BooleanAttributeSpec("underlineShow")
    lazy val valueSelect: ValueSelectAttributeSpec = ValueSelectAttributeSpec("value")
    lazy val wrapperStyle: MapStringAttributeSpec = MapStringAttributeSpec("wrapperStyle")
  }

  @js.native
  trait OnUpdateInputParams extends js.Object {
    val source: String
  }
}

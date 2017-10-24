package org.make.backoffice.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
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
trait DataSourceConfig extends js.Object {
  val text: String
  val value: String
}

object DataSourceConfig {
  def apply(text: String, value: String): DataSourceConfig =
    js.Dynamic.literal(text = text, value = value).asInstanceOf[DataSourceConfig]
}

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
trait Event extends js.Object {
  val target: Element = js.native
}

object MaterialUi {
  type OnRowColumnInteraction = js.Function2[Int, Int, Unit]
  type OnRowInteraction = js.Function1[Int, Unit]
  type OnRowSelection = js.Function2[Int, Seq[Int] | String, Unit]
  type OnChangeMultipleSelect = js.Function3[js.Object, js.UndefOr[Int], js.Array[String], Unit]
  type OnChangeSelect = js.Function3[js.Object, js.UndefOr[Int], String, Unit]
  type FilterAutoComplete = js.Function2[String, String, Boolean]
  type BaseFunction0 = js.Function0[Unit]
  type OnNewRequest = js.Function2[js.Object, Int, Unit]
  type OnUpdateInput = js.Function3[String, js.Array[js.Object], js.Object, Unit]
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

  case class OnCheckAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnCheck): Attribute[OnCheck] =
      Attribute(name = name, value = element, AS_IS)
  }

  case class ValueSelectAttributeSpec(name: String) extends AttributeSpec {
    def :=(elements: Seq[String]): Attribute[js.Array[String]] =
      Attribute(name = name, value = elements.toJSArray, AS_IS)
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

  case class OnUpdateInputAttributeSpec(name: String) extends AttributeSpec {
    def :=(element: OnUpdateInput): Attribute[OnUpdateInput] =
      Attribute(name = name, value = element, AS_IS)
  }

  implicit class MaterialUiVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Card: ReactClassElementSpec = elements(NativeCard)
    lazy val CardActions: ReactClassElementSpec = elements(NativeCardActions)
    lazy val CardHeader: ReactClassElementSpec = elements(NativeCardHeader)
    lazy val CardText: ReactClassElementSpec = elements(NativeCardText)
    lazy val CardTitle: ReactClassElementSpec = elements(NativeCardTitle)
    lazy val Checkbox: ReactClassElementSpec = elements(NativeCheckbox)
    lazy val MenuItem: ReactClassElementSpec = elements(NativeMenuItem)
    lazy val RaisedButton: ReactClassElementSpec = elements(NativeRaisedButton)
    lazy val SelectField: ReactClassElementSpec = elements(NativeSelectField)
    lazy val Table: ReactClassElementSpec = elements(NativeTable)
    lazy val TableRow: ReactClassElementSpec = elements(NativeTableRow)
    lazy val TableRowColumn: ReactClassElementSpec = elements(NativeTableRowColumn)
    lazy val TableHeader: ReactClassElementSpec = elements(NativeTableHeader)
    lazy val TableHeaderColumn: ReactClassElementSpec = elements(NativeTableHeaderColumn)
    lazy val TableBody: ReactClassElementSpec = elements(NativeTableBody)
    lazy val TableFooter: ReactClassElementSpec = elements(NativeTableFooter)
    lazy val AutoComplete: ReactClassElementSpec = elements(NativeAutoComplete)
    lazy val TextFieldMaterialUi: ReactClassElementSpec = elements(NativeTextField)
  }

  implicit class MaterialUiVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val adjustForCheckbox = BooleanAttributeSpec("adjustForCheckbox")
    lazy val allRowsSelected = BooleanAttributeSpec("allRowsSelected")
    lazy val animated = BooleanAttributeSpec("animated")
    lazy val bodyStyle = MapAttributeSpec("bodyStyle")
    lazy val children = ElementAttributeSpec("children")
    lazy val className = StringAttributeSpec("className")
    lazy val columnNumber = IntegerAttributeSpec("columnNumber")
    lazy val dataSource = DataSourceAttributeSpec("dataSource")
    lazy val dataSourceConfig = DataSourceConfigAttributeSpec("dataSourceConfig")
    lazy val deselectOnClickaway = BooleanAttributeSpec("deselectOnClickaway")
    lazy val disableFocusRipple = BooleanAttributeSpec("disableFocusRipple")
    lazy val displayBorder = BooleanAttributeSpec("displayBorder")
    lazy val displayRowCheckbox = BooleanAttributeSpec("displayRowCheckbox")
    lazy val displaySelectAll = BooleanAttributeSpec("displaySelectAll")
    lazy val enableSelectAll = BooleanAttributeSpec("enableSelectAll")
    lazy val errorStyle = MapAttributeSpec("errorStyle")
    lazy val errorText = ElementAttributeSpec("errorText")
    lazy val filter = FilterAutoCompleteAttributeSpec("filter")
    lazy val fixedFooter = BooleanAttributeSpec("fixedFooter")
    lazy val fixedHeader = BooleanAttributeSpec("fixedHeader")
    lazy val floatingLabelFixed = BooleanAttributeSpec("floatingLabelFixed")
    lazy val floatingLabelText = StringAttributeSpec("floatingLabelText")
    lazy val footerStyle = MapAttributeSpec("footerStyle")
    lazy val fullWidth = BooleanAttributeSpec("fullWidth")
    lazy val headerStyle = MapAttributeSpec("headerStyle")
    lazy val height = StringAttributeSpec("height")
    lazy val hintText = StringAttributeSpec("hintText")
    lazy val hoverable = BooleanAttributeSpec("hoverable")
    lazy val hovered = MapAttributeSpec("hovered")
    lazy val insetChildren = BooleanAttributeSpec("insetChildren")
    lazy val listStyle = MapAttributeSpec("listStyle")
    lazy val maxSearchResults = IntegerAttributeSpec("maxSearchResults")
    lazy val menuCloseDelay = IntegerAttributeSpec("menuCloseDelay")
    lazy val menuStyle = MapAttributeSpec("menuStyle")
    lazy val multiSelectable = BooleanAttributeSpec("multiSelectable")
    lazy val onCellClick = OnRowColumnAttributeSpec("onCellClick")
    lazy val onCellHover = OnRowColumnAttributeSpec("onCellHover")
    lazy val onCellHoverExit = OnRowColumnAttributeSpec("onCellHoverExit")
    lazy val onChangeSelect = OnChangeSelectAttributeSpec("onChange")
    lazy val onChangeMultipleSelect = OnChangeMultipleSelectAttributeSpec("onChange")
    lazy val onClose = BaseFunction0AttributeSpec("onClose")
    lazy val onNewRequest = OnNewRequestAttributeSpec("onNewRequest")
    lazy val onCheck = OnCheckAttributeSpec("onCheck")
    lazy val onRowHover = OnRowAttributeSpec("onRowHover")
    lazy val onRowHoverExit = OnRowAttributeSpec("onRowHoverExit")
    lazy val onRowSelection = OnRowSelectionAttributeSpec("onRowSelection")
    lazy val onUpdateInput = OnUpdateInputAttributeSpec("onUpdateInput")
    lazy val open = BooleanAttributeSpec("open")
    lazy val openOnFocus = BooleanAttributeSpec("openOnFocus")
    lazy val preScanRows = BooleanAttributeSpec("preScanRows")
    lazy val primaryText = StringAttributeSpec("primaryText")
    lazy val rowNumber = IntegerAttributeSpec("rowNumber")
    lazy val searchText = StringAttributeSpec("searchText")
    lazy val selectable = BooleanAttributeSpec("selectable")
    lazy val selected = BooleanAttributeSpec("selected")
    lazy val showRowHover = BooleanAttributeSpec("showRowHover")
    lazy val striped = BooleanAttributeSpec("striped")
    lazy val stripedRows = BooleanAttributeSpec("stripedRows")
    lazy val style = MapAttributeSpec("style")
    lazy val textFieldStyle = MapAttributeSpec("textFieldStyle")
    lazy val tooltip = StringAttributeSpec("tooltip")
    lazy val tooltipStyle = MapAttributeSpec("tooltipStyle")
    lazy val valueSelect = ValueSelectAttributeSpec("value")
    lazy val wrapperStyle = MapAttributeSpec("wrapperStyle")
  }
}

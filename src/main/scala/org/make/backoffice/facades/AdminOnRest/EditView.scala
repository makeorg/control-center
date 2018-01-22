package org.make.backoffice.facades.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.StringAttributeSpec
import org.make.backoffice.facades.AdminOnRest.ShowButton.RecordAttributeSpec
import org.make.backoffice.facades.{ElementAttributeSpec, LocationAttributeSpec, MatchAttributeSpec}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "Edit")
object NativeEdit extends ReactClass

object Edit {
  implicit class EditVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Edit: ReactClassElementSpec = elements(NativeEdit)
  }

  implicit class EditVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val editTitle = ElementAttributeSpec("title")
    lazy val actions = ElementAttributeSpec("actions")
    lazy val resource = StringAttributeSpec("resource")
    lazy val location = LocationAttributeSpec("location")
    lazy val `match` = MatchAttributeSpec("match")
  }
}

@js.native
@JSImport("admin-on-rest", "EditButton")
object NativeEditButton extends ReactClass

object EditButton {
  implicit class EditButtonVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val EditButton: ReactClassElementSpec = elements(NativeEditButton)
  }

  implicit class EditButtonVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val basePath = StringAttributeSpec("basePath")
    lazy val record = RecordAttributeSpec("record")
  }
}

@js.native
@JSImport("admin-on-rest", "TabbedForm")
object NativeTabbedForm extends ReactClass

object TabbedForm {
  implicit class TabbedFormVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TabbedForm: ReactClassElementSpec = elements(NativeTabbedForm)
  }
}

@js.native
@JSImport("admin-on-rest", "FormTab")
object NativeFormTab extends ReactClass

object FormTab {
  implicit class FormTabVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val FormTab: ReactClassElementSpec = elements(NativeFormTab)
  }
}

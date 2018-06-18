package org.make.backoffice.facade.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.{BooleanAttributeSpec, StringAttributeSpec}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "Toolbar")
object NativeToolbar extends ReactClass

object Toolbar {
  implicit class ToolbarVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Toolbar: ReactClassElementSpec = elements(NativeToolbar)
  }
}

@js.native
@JSImport("admin-on-rest", "SaveButton")
object NativeSaveButton extends ReactClass

object SaveButton {
  implicit class SaveButtonVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val SaveButton: ReactClassElementSpec = elements(NativeSaveButton)
  }

  implicit class SaveButtonVirtualDOMAattributes(attributes: VirtualDOMAttributes) {
    lazy val label = StringAttributeSpec("label")
    lazy val submitOnEnter = BooleanAttributeSpec("submitOnEnter")
    lazy val raised = BooleanAttributeSpec("raised")
  }
}

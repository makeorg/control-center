package org.make.backoffice.facades.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.Router.RouterVirtualDOMAttributes.{HistoryAttributeSpec, LocationAttributeSpec}
import io.github.shogowada.statictags.StringAttributeSpec
import org.make.backoffice.facades.MatchAttributeSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "Delete")
object NativeDelete extends ReactClass

object Delete {
  implicit class DeleteVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Delete: ReactClassElementSpec = elements(NativeDelete)
  }

  implicit class DeleteVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val resource = StringAttributeSpec("resource")
    lazy val location = LocationAttributeSpec("location")
    lazy val history = HistoryAttributeSpec("history")
    lazy val `match` = MatchAttributeSpec("match")
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
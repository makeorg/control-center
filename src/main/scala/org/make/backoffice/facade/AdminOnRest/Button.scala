package org.make.backoffice.facade.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "RefreshButton")
object NativeRefreshButton extends ReactClass

object Button {
  implicit class ButtonVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val RefreshButton: ReactClassElementSpec = elements(NativeRefreshButton)
  }
}

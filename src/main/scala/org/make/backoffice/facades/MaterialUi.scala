package org.make.backoffice.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("material-ui", "Card")
object NativeCard extends ReactClass

object Card {
  implicit class CardVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Card: ReactClassElementSpec = elements(NativeCard)
  }
}
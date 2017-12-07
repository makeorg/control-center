package org.make.backoffice.facades.AdminOnRest

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.statictags.StringAttributeSpec
import org.make.backoffice.facades.{ElementAttributeSpec, LocationAttributeSpec, MatchAttributeSpec}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("admin-on-rest", "Show")
object NativeShow extends ReactClass

object Show {

  implicit class ShowVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Show: ReactClassElementSpec = elements(NativeShow)
  }

  implicit class ShowVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val resource = StringAttributeSpec("resource")
    lazy val location = LocationAttributeSpec("location")
    lazy val `match` = MatchAttributeSpec("match")
    lazy val showTitle = ElementAttributeSpec("title")
  }
}

@js.native
@JSImport("admin-on-rest", "SimpleShowLayout")
object NativeSimpleShowLayout extends ReactClass

object SimpleShowLayout {
  implicit class SimpleShowLayoutVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val SimpleShowLayout: ReactClassElementSpec = elements(NativeSimpleShowLayout)
  }
}

@js.native
@JSImport("admin-on-rest", "TabbedShowLayout")
object NativeTabbedShowLayout extends ReactClass

object TabbedShowLayout {
  implicit class TabbedShowLayoutVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TabbedShowLayout: ReactClassElementSpec = elements(NativeTabbedShowLayout)
  }
}

@js.native
@JSImport("admin-on-rest", "Tab")
object NativeTab extends ReactClass

object Tab {
  implicit class TabVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Tab: ReactClassElementSpec = elements(NativeTab)
  }
}

@js.native
@JSImport("admin-on-rest", "ShowButton")
object NativeShowButton extends ReactClass

object ShowButton {
  implicit class ShowButtonVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ShowButton: ReactClassElementSpec = elements(NativeShowButton)
  }
}

package org.make.backoffice.facades.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.Router.RouterVirtualDOMAttributes.LocationAttributeSpec
import io.github.shogowada.statictags.StringAttributeSpec
import org.make.backoffice.facades.ElementAttributeSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport


@js.native
@JSImport("admin-on-rest", "Create")
object NativeCreate extends ReactClass

object Create {
  implicit class CreateVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Create: ReactClassElementSpec = elements(NativeCreate)
  }

  implicit class CreateVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val title = StringAttributeSpec("title")
    lazy val actions = ElementAttributeSpec("actions")
    lazy val resource = StringAttributeSpec("resource")
    lazy val location = LocationAttributeSpec("location")
  }
}

@js.native
@JSImport("admin-on-rest", "SimpleForm")
object NativeSimpleForm extends ReactClass

object SimpleForm {
  implicit class SimpleFormVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val SimpleForm: ReactClassElementSpec = elements(NativeSimpleForm)
  }
}

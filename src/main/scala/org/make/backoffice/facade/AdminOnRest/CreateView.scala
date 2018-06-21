package org.make.backoffice.facade.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.{Attribute, AttributeSpec, BooleanAttributeSpec, StringAttributeSpec}
import org.make.backoffice.facade.{ElementAttributeSpec, LocationAttributeSpec}

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
    lazy val hasList = BooleanAttributeSpec("hasList")
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

  implicit class SimpleFormVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val redirect = RedirectAttributeSpec("redirect")
    lazy val toolbar = ElementAttributeSpec("toolbar")
  }

  case class RedirectAttributeSpec(name: String) extends AttributeSpec {
    def :=(value: String): Attribute[String] = Attribute[String](name = name, value = value)
    def :=(value: Boolean): Attribute[Boolean] = Attribute[Boolean](name = name, value = value, valueType = AS_IS)
  }

}

package org.make.backoffice.facades.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.{Attribute, AttributeSpec, StringAttributeSpec}
import org.make.backoffice.facades.{ElementAttributeSpec, LocationAttributeSpec}

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

  implicit class SimpleFormVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val redirect = new RedirectAttributeSpec
  }

  class RedirectAttributeSpec extends AttributeSpec {
    override val name: String = "redirect"
    def :=(value: RedirectType): Attribute[String] = Attribute[String](name = name, value = value.name)
    def :=(value: Boolean): Attribute[Boolean] = Attribute[Boolean](name = name, value = value, valueType = AS_IS)
  }

  sealed trait RedirectType {
    def name: String
  }

  object RedirectType {
    case object Edit extends RedirectType {
      override def name: String = "edit"
    }
    case object List extends RedirectType {
      override def name: String = "list"
    }
    case object Show extends RedirectType {
      override def name: String = "show"
    }
  }

}

package org.make.backoffice.facades.AdminOnRest

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags.{Attribute, AttributeSpec, StringAttributeSpec}
import org.make.backoffice.facades.ReactClassAttributeSpec
import org.make.client.Response

import scala.scalajs.js
import scala.scalajs.js.Promise
import scala.scalajs.js.annotation.JSImport


@js.native
@JSImport("admin-on-rest", "Admin")
object NativeAdmin extends ReactClass

object Admin {

  case class RestClientAttributeSpec(name: String) extends AttributeSpec {
    def :=(
            restClient: js.Function3[String, String, js.Object, Promise[Response]]
          ): Attribute[js.Function3[String, String, js.Object, Promise[Response]]] =
      Attribute(name = name, value = restClient, AS_IS)
  }

  case class CustomRoutesAttributesSpec(name: String) extends AttributeSpec {
    def :=(customRoutes: js.Array[ReactElement]): Attribute[js.Array[ReactElement]] =
      Attribute(name = name, value = customRoutes, AS_IS)
  }

  implicit class AdminVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Admin: ReactClassElementSpec = elements(NativeAdmin)
  }

  implicit class AdminVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val title = StringAttributeSpec("title")
    lazy val menu = ReactClassAttributeSpec("menu")
    lazy val loginPage = ReactClassAttributeSpec("loginPage")
    lazy val customRoutes = CustomRoutesAttributesSpec("customRoutes")
    lazy val restClient = RestClientAttributeSpec("restClient")
    lazy val dashboard = ReactClassAttributeSpec("dashboard")
  }

}

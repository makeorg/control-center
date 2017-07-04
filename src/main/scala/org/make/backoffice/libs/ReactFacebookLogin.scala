package org.make.backoffice.libs

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.{Attribute, AttributeSpec, StringAttributeSpec}
import org.scalajs.dom.experimental.Response

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-facebook-login", JSImport.Default)
object NativeReactFacebookLogin extends ReactClass

object ReactFacebookLogin {

  type callback = js.Function1[Response, Unit]

  @js.native
  trait Data extends js.Object {
    def url: String
  }

  @js.native
  trait Picture extends js.Object {
    def data: Data
  }

  @js.native
  trait FacebookAuthResponse extends js.Object {
    def email: String
    def first_name: String
    def last_name: String
    def name: String
    def userId: String
    def picture: Picture
  }

  case class callbackAttribute(name: String) extends AttributeSpec {
    def :=(callback: (Response) => Unit): Attribute[callback] =
      Attribute(name = name, value = callback, AS_IS)
  }

  implicit class ReactFacebookLoginVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ReactFacebookLogin: ReactClassElementSpec = elements(NativeReactFacebookLogin)
  }

  implicit class ReactFacebookLoginVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val appId = StringAttributeSpec("appId")
    lazy val scope = StringAttributeSpec("scope")
    lazy val fields = StringAttributeSpec("fields")
    lazy val callback: callbackAttribute = callbackAttribute("callback")
  }

}
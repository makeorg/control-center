/*
 * Make.org Control Center
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package org.make.backoffice.facade

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.{Attribute, AttributeSpec, StringAttributeSpec, TrueOrFalseAttributeSpec}
import org.scalajs.dom.experimental.Response

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-google-login", JSImport.Default)
object NativeReactGoogleLogin extends ReactClass

object ReactGoogleLogin {

  type OnSuccess = js.Function1[Response, Unit]
  type OnFailure = js.Function1[Response, Unit]

  @js.native
  trait Profile extends js.Object {
    def email: String
    def familyName: String
    def givenName: String
    def googleId: String
    def imageUrl: String
    def name: String
  }

  @js.native
  trait GoogleAuthResponse extends js.Object {
    def accessToken: String
    def tokenObj: String
    def googleId: String
    def profileObj: Profile
    def tokenId: String
  }

  case class OnSuccessAttribute(name: String) extends AttributeSpec {
    def :=(onSuccess: (Response) => Unit): Attribute[OnSuccess] =
      Attribute(name = name, value = onSuccess, AS_IS)
  }

  case class OnFailureAttribute(name: String) extends AttributeSpec {
    def :=(onFailure: (Response) => Unit): Attribute[OnFailure] =
      Attribute(name = name, value = onFailure, AS_IS)
  }

  implicit class ReactGoogleLoginVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ReactGoogleLogin: ReactClassElementSpec = elements(NativeReactGoogleLogin)
  }

  implicit class ReactGoogleLoginVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val clientID = StringAttributeSpec("clientId")
    lazy val scope = StringAttributeSpec("scope")
    lazy val onSuccess = OnSuccessAttribute("onSuccess")
    lazy val onFailure = OnFailureAttribute("onFailure")
    lazy val isSignIn = TrueOrFalseAttributeSpec("isSignIn")
  }

}

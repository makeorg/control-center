package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.libs.ReactFacebookLogin._
import org.scalajs.dom.experimental.Response

import scala.scalajs.js.Dynamic.{global => g}

object FacebookComponent {

  case class FacebookState(isSignIn: Boolean, profile: Option[FacebookAuthResponse])

  def apply(): ReactClass = reactClass

  def callbackResponse(self: Self[Unit, FacebookState]): (Response) => Unit = (response:Response) => {
    val auth = response.asInstanceOf[FacebookAuthResponse]
    g.console.log(auth)
    self.setState(FacebookState(
      isSignIn = true,
      profile = Some(auth)
    ))
  }

  private lazy val reactClass = React.createClass[Unit, FacebookState](
    getInitialState = (_) => FacebookState(isSignIn = false, None),
    render = (self) =>
      if (!self.state.isSignIn) {
        <.ReactFacebookLogin(
          ^.appId := "327180550991329",
          ^.scope := "public_profile, email",
          ^.fields := "first_name, last_name, email, name, picture",
          ^.callback := callbackResponse(self)
        )()
      }
      else {
        <.p()("Hello " + self.state.profile.get.name)
      }
  )
}

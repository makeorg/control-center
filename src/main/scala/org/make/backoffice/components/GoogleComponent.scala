package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.libs.ReactGoogleLogin.{GoogleAuthResponse, _}
import org.scalajs.dom.experimental.Response

import scala.scalajs.js.Dynamic.{global => g}

object GoogleComponent {

  case class GoogleState(isSignIn: Boolean, profile: Option[Profile])

  def apply(): ReactClass = reactClass

  def onSuccessResponse(self: Self[Unit, GoogleState]): (Response) => Unit = (response: Response) => {
    g.console.log(response)
    val auth = response.asInstanceOf[GoogleAuthResponse]
    g.console.log(auth.profileObj.name)
    self.setState(GoogleState(isSignIn = true, profile = Some(auth.profileObj)))
  }

  def onFailureResponse: (Response) => Unit = (_) => {}

  private lazy val reactClass = React.createClass[Unit, GoogleState](
    getInitialState = (_) => GoogleState(isSignIn = false, profile = None),
    render = (self) =>
      if (!self.state.isSignIn) {
        <.div(^.className := "GoogleAuth")(
          <.ReactGoogleLogin(
            ^.clientID := "150518969722-8bvfri6btvcqa4uoqiosktr08rnokmp8.apps.googleusercontent.com",
            ^.scope := "profile email",
            ^.onSuccess := onSuccessResponse(self),
            ^.onFailure := onFailureResponse,
            ^.isSignIn := self.state.isSignIn
          )()
        )
      }
      else {
        <.p()("Hello " + self.state.profile.get.name)
      }
  )
}

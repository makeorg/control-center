package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.facades.Configuration
import org.make.backoffice.facades.ReactGoogleLogin._
import org.make.backoffice.models.User
import org.make.client.SingleResponse
import org.make.services.user.UserServiceComponent
import org.scalajs.dom.experimental.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object GoogleComponent extends UserServiceComponent {

  val googleAppId: String = Configuration.googleAppId
  override val apiBaseUrl: String = Configuration.apiUrl

  case class GoogleState(isSignIn: Boolean, user: Option[User], error: Option[String] = None)

  def apply(): ReactClass = reactClass

  def signInGoogle(self: Self[Unit, GoogleState])(response: Response): Unit = {
    handleFutureApiResponse(userService.loginGoogle(response.asInstanceOf[GoogleAuthResponse].tokenId), self)
  }

  def handleFutureApiResponse(futureUser: Future[SingleResponse[User]], self: Self[Unit, GoogleState]): Unit = {
    futureUser.onComplete {
      case Success(singleResponseUser) =>
        self.setState(GoogleState(isSignIn = true, user = Some(singleResponseUser.data)))
      case Failure(e) =>
        self.setState(
          self.state.copy(isSignIn = false, user = None, error = Some(s"failed to connect: ${e.getMessage}"))
        )
    }
  }

  def onFailureResponse: (Response) => Unit = (_) => {}

  private lazy val reactClass = React.createClass[Unit, GoogleState](
    getInitialState = (_) => GoogleState(isSignIn = false, user = None, error = None),
    render = (self) =>
      if (!self.state.isSignIn) {
        <.div(^.className := "GoogleAuth")(
          <.ReactGoogleLogin(
            ^.clientID := googleAppId,
            ^.scope := "profile email",
            ^.onSuccess := signInGoogle(self),
            ^.onFailure := onFailureResponse,
            ^.isSignIn := self.state.isSignIn
          )()
        )
      } else {
        <.p()("Hello " + self.state.user.get.firstName)
    }
  )
}

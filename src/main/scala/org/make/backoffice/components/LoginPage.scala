package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.facades.Configuration
import org.make.backoffice.facades.ReactGoogleLogin._
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.backoffice.models.User
import org.make.client.{AuthClient, SingleResponse}
import org.make.services.user.UserServiceComponent
import org.scalajs.dom.experimental.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object LoginPage extends UserServiceComponent {

  val googleAppId: String = Configuration.googleAppId
  override val apiBaseUrl: String = Configuration.apiUrl

  type Self = React.Self[Unit, LoginPageState]

  case class LoginPageState(isSignIn: Boolean, user: Option[User], error: Option[String] = None)

  def apply(): ReactClass = reactClass

  def onFailureResponse: (Response) => Unit = (_) => {}

  lazy val reactClass = WithRouter(
    React.createClass[Unit, LoginPageState](
      getInitialState = (_) => LoginPageState(isSignIn = false, user = None, error = None),
      render = (self) => {
        def signInGoogle(response: Response): Unit = {
          handleFutureApiResponse(userService.loginGoogle(response.asInstanceOf[GoogleAuthResponse].tokenId))
          self.props.history.push("/")
        }

        def handleFutureApiResponse(futureUser: Future[SingleResponse[User]]): Unit = {
          futureUser.onComplete {
            case Success(singleResponseUser) =>
              AuthClient.auth(AuthClient.AUTH_LOGIN, js.Dictionary("user" -> Some(singleResponseUser.data)))

            //self.props.wrapped.handleLogin(self)
            case Failure(e) =>
              self.setState(
                self.state.copy(isSignIn = false, user = None, error = Some(s"failed to connect: ${e.getMessage}"))
              )
          }
        }

        <.div(^.className := "GoogleAuth")(
          <.ReactGoogleLogin(
            ^.clientID := googleAppId,
            ^.scope := "profile email",
            ^.onSuccess := signInGoogle,
            ^.onFailure := onFailureResponse,
            ^.isSignIn := self.state.isSignIn
          )()
        )
      }
    )
  )
}

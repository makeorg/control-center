package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.backoffice.facades.Configuration
import org.make.backoffice.facades.ReactGoogleLogin._
import org.make.backoffice.models.{Role, User}
import org.make.client.{AuthClient, SingleResponse}
import org.make.services.user.UserServiceComponent
import org.scalajs.dom.experimental.Response

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

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
        }

        def handleFutureApiResponse(futureUser: Future[SingleResponse[User]]): Unit = {
          futureUser.onComplete {
            case Success(singleResponseUser) =>
              val user = singleResponseUser.data
              if (user.roles.contains(Role.roleAdmin) || user.roles.contains(Role.roleModerator)) {
                AuthClient.futureAuth(AuthClient.AUTH_LOGIN, js.Dictionary("user" -> Some(singleResponseUser.data))).onComplete {
                  case Success(loginResponse) =>
                    self.props.history.push("/")
                  case Failure(e) =>
                    self.setState(
                      self.state.copy(isSignIn = false, error = Some(s"failed to connect: $e"))
                    )
                    self.props.history.push("/login")
                }
              } else {
                self.setState(
                  self.state.copy(isSignIn = false, user = None, error = Some(s"failed to connect: You dont have the right role"))
                )
              }

            case Failure(e) =>
              self.setState(
                self.state.copy(isSignIn = false, user = None, error = Some(s"failed to connect: ${e.getMessage}"))
              )
          }
        }

        <.div(^.className := LoginPageStyles.container.htmlClass)(
          <.ReactGoogleLogin(
            ^.clientID := googleAppId,
            ^.scope := "profile email",
            ^.onSuccess := signInGoogle,
            ^.onFailure := onFailureResponse,
            ^.isSignIn := self.state.isSignIn,
            ^.className := LoginPageStyles.signInButton.htmlClass
          )(
            <.i(^.className := LoginPageStyles.googlePlus.htmlClass)(),
            <.span(^.className := LoginPageStyles.signInButtonText.htmlClass)("Sign in with Google")
          ),
          <.div()(self.state.error.getOrElse("")),
          <.style()(LoginPageStyles.render[String])
        )
      }
    )
  )
}

object LoginPageStyles extends StyleSheet.Inline {

  import dsl._

  val container: StyleA = style(
    display.flex,
    flexDirection.column,
    minHeight(100.vh),
    alignItems.center,
    justifyContent.center,
    backgroundColor(rgb(0, 188, 212))
  )

  val signInButton: StyleA = style(
    addClassNames("btn", "btn-danger")
  )
  val signInButtonText: StyleA = style(
    marginLeft(10.px)
  )
  val googlePlus: StyleA = style(
    addClassNames("fa", "fa-google-plus")
  )
}

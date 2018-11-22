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

package org.make.backoffice.component

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.backoffice.facade.Configuration
import org.make.backoffice.facade.ReactGoogleLogin._
import org.make.backoffice.model.{Role, User}
import org.make.backoffice.client.{AuthClient, SingleResponse}
import org.make.backoffice.service.user.UserService
import org.scalajs.dom.experimental.Response

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object LoginPage {

  val googleAppId: String = Configuration.googleAppId

  type Self = React.Self[Unit, LoginPageState]

  case class LoginPageState(isSignIn: Boolean, user: Option[User], error: Option[String] = None)

  def apply(baseUrl: String): ReactClass = reactClass(baseUrl)

  def onFailureResponse: (Response) => Unit = (_) => {}

  def reactClass(baseUrl: String) = WithRouter(
    React.createClass[Unit, LoginPageState](
      displayName = "LoginPage",
      getInitialState = (_) => LoginPageState(isSignIn = false, user = None, error = None),
      render = (self) => {
        def signInGoogle(response: Response): Unit = {
          handleFutureApiResponse(UserService.loginGoogle(response.asInstanceOf[GoogleAuthResponse].tokenId))
        }

        def handleFutureApiResponse(futureUser: Future[SingleResponse[User]]): Unit = {
          futureUser.onComplete {
            case Success(singleResponseUser) =>
              val user = singleResponseUser.data
              if (user.roles.contains(Role.roleAdmin) || user.roles.contains(Role.roleModerator)) {
                AuthClient
                  .futureAuth(AuthClient.AUTH_LOGIN, js.Dictionary("user" -> Some(singleResponseUser.data)))
                  .onComplete {
                    case Success(_) =>
                      val resolbedHash = {
                        if (baseUrl.contains("/login")) {
                          "/"
                        } else if (baseUrl.startsWith("#")) {
                          baseUrl.substring(1)
                        } else {
                          baseUrl
                        }
                      }
                      self.props.history.push(resolbedHash)
                    case Failure(e) =>
                      self.setState(self.state.copy(isSignIn = false, error = Some(s"failed to connect: $e")))
                      self.props.history.push("/login")
                  }
              } else {
                self.setState(
                  self.state.copy(
                    isSignIn = false,
                    user = None,
                    error = Some(s"failed to connect: You don't have the right role")
                  )
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

  val signInButton: StyleA = style(addClassNames("btn", "btn-danger"))
  val signInButtonText: StyleA = style(marginLeft(10.px))
  val googlePlus: StyleA = style(addClassNames("fa", "fa-google-plus"))
}

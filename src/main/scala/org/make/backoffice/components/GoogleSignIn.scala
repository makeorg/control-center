package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.backoffice.actions.{Connect, Disconnect}
import org.make.backoffice.libs.GApi.gapi
import org.make.backoffice.libs.RenderParameters
import org.make.backoffice.models.GlobalState

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.Dynamic.{global => g}
import scala.scalajs.js.JSON

object GoogleSignInController {
  private lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val onConnect = () => dispatch(Connect)
      val onDisconnect = () => dispatch(Disconnect)
      (state: GlobalState, _: Props[Unit]) => {
        GoogleSignInPresentational.WrappedProps(
          isAuthenticated = state.user.isAuthenticated,
          onConnect = onConnect,
          onDisconnect = onDisconnect
        )
      }
    }
  )(GoogleSignInPresentational())

  def apply(): ReactClass = reactClass

}

object GoogleSignInPresentational {

  case class WrappedProps(isAuthenticated: Boolean, onConnect: () => _, onDisconnect: () => _)
  case class State(isAuthenticated: Boolean = false, errorMessage: String = "")

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[WrappedProps, State](
    getInitialState = (_) => State(),
    componentDidMount = (self) => {
      if (!self.state.isAuthenticated) {
        g.console.log("GApi: " + JSON.stringify(gapi))
        g.console.log("Signin2: " + JSON.stringify(gapi.signin2))
        gapi.signin2.render("g-signin", RenderParameters(
          scope = "profile email",
          width = 700,
          height = 100,
          longtitle = false,
          theme = "dark",
          onsuccess = this.onSignIn(self)
        ))
      }
    },
    render = (self) =>
      <.div()(
        <.p()(self.props.wrapped.isAuthenticated.toString),
        <.p()(self.state.isAuthenticated.toString),
//        if (self.state.isAuthenticated)
          <.a(^.onClick := this.signOut(self))("Sign out")
      )
  )

  def signOut(self: Self[WrappedProps, State]): (SyntheticEvent) => Future[Unit] = (_: SyntheticEvent) => {
    val auth2 = gapi.auth2.getAuthInstance()
    auth2.signOut().toFuture.map { _ =>
      g.console.log("Signing Out !")
      self.props.wrapped.onDisconnect()
    }
  }

  def onSignIn(self: Self[WrappedProps, State]): () => Unit = () => {
    g.console.log("On Sign In !")
    self.props.wrapped.onConnect()
  }
}

package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.backoffice.libs.gapi

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.Dynamic.{global => g}

object GoogleSignIn {

  case class State(isAuthenticated: Boolean = false, errorMessage: String = "")

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, State](
    getInitialState = (_) => State(),
    //    [WIP]
    //
    //    componentDidMount = (self) => {
    //      setTimeout(1000) {
    //        if (!self.state.isAuthenticated) {
    //          g.console.log("gapi" + gapi.toString)
    //          gapi.signin2.render("g-signin", js.Dynamic.literal(
    //            "scope" -> "profile email",
    //            "width" -> 20,
    //            "height" -> 500,
    //            "longtitle" -> true,
    //            "theme" -> "dark",
    //            "onsuccess" -> this.onSignIn(self)
    //          ))
    //        }
    //      }
    //    },
    render = (self) =>
      <.div()(
        <.p()("authent':"),
        <.p()(self.state.isAuthenticated.toString),
        <.div(
          ^.className := "g-signin2",
          ^("data-scope") := "profile email",
          ^("data-width") := "300",
          ^("data-height") := "25",
          ^("data-longtitle") := true,
          ^("data-theme") := "dark",
          //            ^("data-onsuccess") := s"${this.onSignIn(self)}"
          ^.onClick := this.onSignIn(self)
        )(),
        if (self.state.isAuthenticated)
          <.a(^.onClick := this.signOut(self))("Sign out")
      )
  )

  def signOut(self: Self[Unit, State]): (SyntheticEvent) => Future[Unit] = (_: SyntheticEvent) => {
    val auth2 = gapi.auth2.getAuthInstance()
    auth2.signOut().toFuture.map { _ =>
      g.console.log("Signing Out !")
      self.setState(State())
    }
  }

  def onSignIn(self: Self[Unit, State]): () => Unit = () => {
    g.console.log("On Sign In !")
    self.setState(State(isAuthenticated = true))
    self.forceUpdate()
  }


}

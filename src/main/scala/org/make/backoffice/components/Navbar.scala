package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterReduxAction.Push
import org.make.backoffice.actions.Disconnect
import org.make.backoffice.libs.GApi.gapi
import org.make.backoffice.models.GlobalState

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.Dynamic.{global => g}

object NavbarController extends RouterProps {

  private lazy val reactClass = ReactRedux.connectAdvanced { dispatch: Dispatch =>
    val routeProposition = () => dispatch(Push("/propositions"))
    val routeHomepage = () => dispatch(Push("/"))
    val onDisconnect = () => dispatch(Disconnect)
    (state: GlobalState, ownProps: Props[Unit]) => {
      NavbarPresentational.Props(
        path = ownProps.location.pathname,
        isAuthenticated = state.user.isAuthenticated,
        onPushRouteListPropositionClick = routeProposition,
        onPushRouteHomepageList = routeHomepage,
        onDisconnect = onDisconnect
      )
    }
  }(NavbarPresentational())

  def apply(): ReactClass = reactClass
}

object NavbarPresentational {

  case class Props(
                    path: String,
                    isAuthenticated: Boolean = false,
                    onPushRouteListPropositionClick: () => _,
                    onPushRouteHomepageList: () => _,
                    onDisconnect: () => _
                  )

  case class State(isAuthenticated: Boolean = false)

  def apply(): ReactClass = React.createClass[Props, State](
    getInitialState = (self) => State(self.props.wrapped.isAuthenticated),
    render = (self) => {
      <.div()(
        <.nav(^.className := "navbar navbar-inverse navbar-fixed-top")(
          <.div(^.className := "container-fluid")(
            <.div(^.className := "navbar-header")(
              <.button(^.`type` := "button", ^.className := "navbar-toggle collapsed", ^("data-toogle") := "collapse")(
                <.span(^.className := "sr-only")("Toogle navigation"),
                <.span(^.className := "icon-bar")(),
                <.span(^.className := "icon-bar")(),
                <.span(^.className := "icon-bar")()
              ),
              <.a(^.className := "navbar-brand", ^.href := "#", ^.rel := "nofollow")(
                <.img(^.src := "https://cdn.make.org/Main/_img/device/favicon-32x32.png")()
              )
            ),
            <.div(^.id := "navbar", ^.className := "navbar-collapse collapse")(
              <.ul(^.className := "nav navbar-nav navbar-left")(
                <.li()(
                  <.a(^.id := "push-route-proposition", ^.onClick := self.props.wrapped.onPushRouteListPropositionClick)("List Propositions")
                )
              ),
              <.ul(^.className := "nav navbar-nav navbar-right")(
                <.li()(
                  if (self.props.wrapped.isAuthenticated)
                    <.a(^.onClick := this.signOut(self))("Sign out")
                  else
                    <.a(^.id := "push-route-homepage", ^.onClick := self.props.wrapped.onPushRouteHomepageList)("Login")
                )
              )
            )
          )
        )
      )
    }
  )

  def signOut(self: Self[Props, State]): (SyntheticEvent) => Future[Unit] = (_: SyntheticEvent) => {
    val auth2 = gapi.auth2.getAuthInstance()
    auth2.signOut().toFuture.map { _ =>
      g.console.log("Signing Out !")
      self.props.wrapped.onDisconnect()
    }
  }

}

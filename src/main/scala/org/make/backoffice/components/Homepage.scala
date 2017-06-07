package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterReduxAction.Push
import org.make.backoffice.models.GlobalState

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}

object HomepageController {

  private lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val pushRouteProposition = () => dispatch(Push("/propositions"))
      val pushRouteHomepage = () => dispatch(Push("/"))
      (state: GlobalState, _: Props[Unit]) => {
        HomepagePresentational.WrappedProps(
          isAuthenticated = state.user.isAuthenticated,
          onPushRouteListPropositionClick = pushRouteProposition,
          onPushRouteHomepageList = pushRouteHomepage
        )
      }
    }
  )(HomepagePresentational())

  def apply(): ReactClass = reactClass

}

object HomepagePresentational {

  case class WrappedProps(
                           isAuthenticated: Boolean,
                           onPushRouteListPropositionClick: () => _,
                           onPushRouteHomepageList: () => _
                         )

  def apply(): ReactClass = React.createClass[WrappedProps, Unit](
    render = (self) =>
      <.div(^.className := "container-fluid")(
        <.div(^.className := "row")(
          <.div(^.className := "main")(
            <.h1()("Make.org - Backoffice. "),
            <(GoogleSignIn())()(),
            //  ROUTES
            <.button(^.id := "push-route-proposition", ^.onClick := self.props.wrapped.onPushRouteListPropositionClick)("List Propositions"),

            <.form(^.onSubmit := this.onSubmit(self))(
              <.input(^.`type` := "search", ^.placeholder := "Proposition id")(),
              <.button(^.`type` := "submit")("Proposition")
            )
          )
        )
      )
  )

  def onSubmit(self: Self[WrappedProps, Unit]): (SyntheticEvent) => js.Dynamic = (e: SyntheticEvent) => {
    e.preventDefault()
    g.console.log("Submitting")
  }
}

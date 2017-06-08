package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.Router._
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterReduxAction.Push
import org.make.backoffice.models.GlobalState

object RouteController extends RouterProps {

  private lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val routeProposition = () => dispatch(Push("/propositions"))
      val routeHomepage = () => dispatch(Push("/"))
      (_: GlobalState, ownProps: Props[Unit]) => {
        RoutePresentational.WrappedProps(
          path = ownProps.location.pathname,
          onPushRouteListPropositionClick = routeProposition,
          onPushRouteHomepageList = routeHomepage
        )
      }
    }
  )(RoutePresentational())

  def apply(): ReactClass = reactClass

}

object RoutePresentational {

  case class WrappedProps(path: String, onPushRouteListPropositionClick: () => _, onPushRouteHomepageList: () => _)

  private lazy val reactClass = React.createClass[WrappedProps, Unit](
    (self) =>
      <.div()(
        <.h1()(self.props.wrapped.path),
        <.button(^.id := "push-route-proposition", ^.onClick := self.props.wrapped.onPushRouteListPropositionClick)("List Propositions"),
        <.button(^.id := "push-route-homepage", ^.onClick := self.props.wrapped.onPushRouteHomepageList)("Homepage"),
        <.Switch()(
          <.Route(^.path := "/propositions", ^.component := ListPropositionsController())(),
          <.Route(^.path := "/", ^.component := HomepageController())()
        )
      )
  )

  def apply(): ReactClass = reactClass
}

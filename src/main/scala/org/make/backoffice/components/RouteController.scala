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
import org.make.backoffice.actions.DisplayListPropositions
import org.make.backoffice.models.GlobalState
import org.make.backoffice.services.PropositionsServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.Dynamic.{global => g}
import scala.util.{Failure, Success}

object RouteController extends RouterProps with PropositionsServiceComponent {

  override def propositionService: RouteController.PropositionService = new PropositionService()

  private lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val pushRouteProposition = () => {
        dispatch(Push("/propositions"))
        propositionService.listPropositions.map{ propositions =>
          DisplayListPropositions(propositions)
        }.onComplete{
          case Success(action) => dispatch(action)
          case Failure(e) => g.console.log(e.getStackTrace.map(_.toString).mkString("\n"))
        }
      }
      val pushRouteHomepage = () => dispatch(Push("/"))
      (_: GlobalState, ownProps: Props[Unit]) => {
        RoutePresentational.WrappedProps(
          path = ownProps.location.pathname,
          onPushRouteListPropositionClick = pushRouteProposition,
          onPushRouteHomepageList = pushRouteHomepage
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
        <.button(^.id := "push-route-proposition", ^.className := "btn", ^.onClick := self.props.wrapped.onPushRouteListPropositionClick)("List Propositions"),
        <.button(^.id := "push-route-homepage", ^.className := "btn", ^.onClick := self.props.wrapped.onPushRouteHomepageList)("Homepage"),
        <.Switch()(
          <.Route(^.path := "/propositions", ^.component := ListPropositionsController())(),
          <.Route(^.path := "/", ^.component := HomepageController())()
        )
      )
  )

  def apply(): ReactClass = reactClass
}

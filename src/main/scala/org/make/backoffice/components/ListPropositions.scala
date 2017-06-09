package org.make.backoffice.components

import java.util.UUID

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterReduxAction.Push
import org.make.backoffice.actions.DisplayProposition
import org.make.backoffice.models.{GlobalState, Proposition}
import org.make.backoffice.services.PropositionsServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.Dynamic.{global => g}
import scala.util.{Failure, Success}

object ListPropositionsController extends PropositionsServiceComponent {
  override val propositionService: ListPropositionsController.PropositionService =
    new PropositionService()

  def apply(): ReactClass = reactClass

  private lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {

      val onDisplayProposition = (propositionId: UUID) => {
        dispatch(Push(s"/propositions/${propositionId.toString}"))
        propositionService.getProposition(propositionId).map{ proposition =>
          DisplayProposition(proposition)
        }.onComplete{
          case Success(action) => dispatch(action)
          case Failure(e) => g.console.log(e.getStackTrace.map(_.toString).mkString("\n"))
        }
      }
      (state: GlobalState, _: Props[Unit]) => {
        ListPropositions.WrappedProps(
          propositions = state.listPropositions,
          onDisplayProposition = onDisplayProposition
        )
      }
    }
  )(ListPropositions())
}

object ListPropositions {

  case class WrappedProps(propositions: Seq[Proposition], onDisplayProposition: (UUID) => _)

  private lazy val reactClass: ReactClass = React.createClass[WrappedProps, Unit](
    render = (self) => {
      <.div()(
        <.h1()("List Propositions"),
        <.br()(),
        <.ul(^.style := Map("list-style-type" -> "none"))(
          <.hr()(),
          self.props.wrapped.propositions.map(p => displayPropositions(self.props.wrapped.onDisplayProposition, p))
        )
      )
    }
  )

  def displayPropositions(display: (UUID) => _, p: Proposition): ReactElement = {
    <.li()(
      <.p()(p.content),
      <.button(^.onClick := { (_) => display(p.propositionId) })("Display me"),
      <.hr()()

    )
  }

  def apply(): ReactClass = reactClass
}

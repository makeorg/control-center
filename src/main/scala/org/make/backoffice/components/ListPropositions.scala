package org.make.backoffice.components

import java.util.UUID

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.backoffice.actions.SearchProposition
import org.make.backoffice.models.{GlobalState, Proposition}
import org.make.backoffice.services.PropositionsServiceComponent

object ListPropositionsController extends PropositionsServiceComponent {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val searchProposition = (pId: UUID) => dispatch(SearchProposition(pId))
      (_: GlobalState, _: Props[Unit]) => {
        ListPropositions.WrappedProps(
          propositions = propositionsList,
          searchProposition = searchProposition
        )
      }
    }
  )(ListPropositions())
}

object ListPropositions {

  case class WrappedProps(propositions: Seq[Proposition], searchProposition: (UUID) => _)

  private lazy val reactClass: ReactClass = React.createClass[WrappedProps, Unit](
    render = (self) => {
      <.div()(
        <.h1()("List Propositions"),
        <.br()(),
        <.ul(^.style := Map("list-style-type" -> "none"))(
          <.hr()(),
          self.props.wrapped.propositions.map(p => displayPropositions(p))
        )
      )
    }
  )

  def displayPropositions(p: Proposition): ReactElement = {
    <.li()(
      <.a()(p.content),
      <.hr()()
    )
  }

  def apply(): ReactClass = reactClass
}

package org.make.backoffice.component.proposal.moderation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.proposal.common.ShowProposalComponents.ShowProposalComponentsProps
import org.make.backoffice.model.SingleProposal
import org.make.backoffice.service.proposal.ProposalService
import org.make.backoffice.facade.MaterialUi._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object NextProposal {

  final case class NextProposalProps() extends RouterProps
  final case class NextProposalState(proposal: Option[SingleProposal])

  lazy val reactClass: ReactClass =
    React.createClass[NextProposalProps, NextProposalState](
      displayName = "NextProposal",
      getInitialState = { _ =>
        NextProposalState(None)
      },
      componentWillReceiveProps = { (self, props) =>
        ProposalService.getProposalById(props.`match`.params("id")).onComplete {
          case Success(proposalResponse) => self.setState(_.copy(proposal = Some(proposalResponse.data)))
          case Failure(e)                => js.Dynamic.global.console.log(e.getMessage)
        }
      },
      shouldComponentUpdate = { (_, props, state) =>
        state.proposal.map(_.id).contains(props.`match`.params("id"))
      },
      componentWillUpdate = { (_, _, _) =>
        js.Dynamic.global.scrollTo(0, 0)
      },
      render = { self =>
        if (self.state.proposal.nonEmpty) {
          <.div()(
            <.Card()(<.CardTitle(^.title := self.state.proposal.map(_.content).getOrElse(""))()),
            <.ShowProposalComponents(
              ^.wrapped := ShowProposalComponentsProps(
                hash = org.scalajs.dom.window.location.hash,
                proposal = self.state.proposal
              )
            )()
          )
        } else {
          <.div()()
        }
      }
    )
}

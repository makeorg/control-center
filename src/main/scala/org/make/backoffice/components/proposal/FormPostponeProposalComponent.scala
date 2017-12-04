package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.statictags.Element
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.models.SingleProposal
import org.make.services.proposal.ProposalServiceComponent
import org.make.services.proposal.ProposalServiceComponent.ProposalService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FormPostponeProposalComponent {
  val proposalService: ProposalService = ProposalServiceComponent.proposalService

  case class FormProps(proposal: SingleProposal, isLocked: Boolean = false)
  case class FormState(errorMessage: Option[String] = None, isLocked: Boolean = false)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[FormProps, FormState](
        displayName = "FormPostponeProposalComponent",
        getInitialState = { _ =>
          FormState()
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(_.copy(errorMessage = None, isLocked = props.wrapped.isLocked))
        },
        render = { self =>
          def handleSubmitPostpone: () => Unit =
            () => {
              proposalService
                .postponeProposal(self.props.wrapped.proposal.id)
                .onComplete {
                  case Success(_) =>
                    self.props.history.goBack()
                  case Failure(_) =>
                    self.setState(_.copy(errorMessage = Some("Oooops, something went wrong")))
                }
            }

          val errorMessage: Option[Element] =
            self.state.errorMessage.map(msg => <.p()(msg))

          <.Card(^.style := Map("marginTop" -> "1em"))(
            <.CardTitle(^.title := "I want to postpone that proposal")(),
            <.CardActions()(
              <.RaisedButton(
                ^.disabled := self.state.isLocked,
                ^.label := "Confirm postpone",
                ^.onClick := handleSubmitPostpone
              )(),
              errorMessage
            )
          )
        }
      )
    )

}

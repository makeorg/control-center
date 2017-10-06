package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.statictags.Element
import org.make.backoffice.helpers.Configuration
import org.make.backoffice.models.SingleProposal
import org.make.services.proposal.ProposalServiceComponent
import org.make.services.proposal.ProposalServiceComponent.ProposalService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FormRefuseProposalComponent {
  val proposalService: ProposalService = ProposalServiceComponent.proposalService

  case class FormProps(proposal: SingleProposal)
  case class FormState(reasons: Seq[String],
                       refusalReason: String = "Other",
                       notifyUser: Boolean = true,
                       errorMessage: Option[String] = None)

  val reasons: Seq[String] = Configuration.getReasonsForRefusal

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[FormProps, FormState](
        getInitialState = { _ =>
          FormState(reasons = reasons)
        },
        render = { self =>
          def handleReasonRefusalChange: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
            val refusalReason = event.target.value
            self.setState(_.copy(refusalReason = refusalReason))
          }

          def handleNotifyUserChange: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
            val notifyUser = event.target.checked
            self.setState(_.copy(notifyUser = notifyUser))
          }

          def handleSubmitRefuse: () => Unit =
            () => {
              proposalService
                .refuseProposal(self.props.wrapped.proposal.id, Option(self.state.refusalReason), self.state.notifyUser)
                .onComplete {
                  case Success(_) =>
                    self.props.history.goBack()
                    self.setState(_.copy(errorMessage = None))
                  case Failure(e) =>
                    self.setState(_.copy(errorMessage = Some("Oooops, something went wrong")))
                }
            }

          val selectReasons = <.select(
            ^.id := "refusal-reason",
            ^.value := self.state.refusalReason,
            ^.onChange := handleReasonRefusalChange
          )(<.option(^.disabled := true, ^.value := "")("-- select a reason for refusal --"), self.state.reasons.map {
            reason =>
              <.option(^.value := reason)(reason)
          })

          val errorMessage: Option[Element] =
            self.state.errorMessage.map(msg => <.p()(msg))

          <.div()(
            <.div()("I want to refuse that proposal"),
            <.div()(
              selectReasons,
              <.label(^.`for` := "refusal-reason")("Refusal reason"),
              <.input(
                ^.`type`.checkbox,
                ^.id := "notify-user-refuse",
                ^.value := "notify-user-refuse",
                ^.checked := self.state.notifyUser,
                ^.onChange := handleNotifyUserChange
              )(),
              <.label(^.`for` := "notify-user-refuse")("Notify user"),
              <.button(^.onClick := handleSubmitRefuse)("Confirm refusal"),
              errorMessage
            )
          )
        }
      )
    )
}

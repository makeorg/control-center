package org.make.backoffice.component.proposal.common

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.statictags.Element
import org.make.backoffice.client.NotFoundHttpException
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.util.Configuration
import org.make.backoffice.model.SingleProposal
import org.make.backoffice.service.proposal.ProposalService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object FormRefuseProposalComponent {
  case class FormProps(proposal: SingleProposal, isLocked: Boolean = false)
  case class FormState(reasons: Seq[String],
                       refusalReason: String = "Other",
                       notifyUser: Boolean = true,
                       errorMessage: Option[String] = None,
                       isLocked: Boolean = false)

  val reasons: Seq[String] = Configuration.getReasonsForRefusal

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[FormProps, FormState](getInitialState = { _ =>
        FormState(reasons = reasons)
      }, componentWillReceiveProps = { (self, props) =>
        self.setState(_.copy(isLocked = props.wrapped.isLocked))
      }, render = {
        self =>
          def handleReasonRefusalChange: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
            self.setState(_.copy(refusalReason = value))
          }

          def handleNotifyUserChange: (js.Object, Boolean) => Unit = { (_, checked) =>
            self.setState(_.copy(notifyUser = checked))
          }

          def handleSubmitRefuse: () => Unit =
            () => {
              ProposalService
                .refuseProposal(self.props.wrapped.proposal.id, Option(self.state.refusalReason), self.state.notifyUser)
                .onComplete {
                  case Success(_) =>
                    self.props.history.goBack()
                    self.setState(_.copy(errorMessage = None))
                  case Failure(_) =>
                    self.setState(_.copy(errorMessage = Some("Oooops, something went wrong")))
                }
            }

          def handleNextProposal: SyntheticEvent => Unit = {
            event =>
              event.preventDefault()
              val futureNextProposal =
                for {
                  _ <- ProposalService.refuseProposal(
                    proposalId = self.props.wrapped.proposal.id,
                    refusalReason = Option(self.state.refusalReason),
                    notifyUser = self.state.notifyUser
                  )
                  nextProposal <- ProposalService
                    .nexProposalToModerate(
                      self.props.wrapped.proposal.operationId.toOption,
                      self.props.wrapped.proposal.themeId.toOption,
                      Some(self.props.wrapped.proposal.country),
                      Some(self.props.wrapped.proposal.language)
                    )
                } yield nextProposal
              futureNextProposal.onComplete {
                case Success(proposalResponse) =>
                  self.props.history.push(s"/nextProposal/${proposalResponse.data.id}")
                case Failure(NotFoundHttpException) => self.props.history.push("/proposals")
                case Failure(_)                     => self.setState(_.copy(errorMessage = Some("Oooops, something went wrong")))
              }
          }

          val selectReasons = <.SelectField(
            ^.floatingLabelText := "Refusal reason",
            ^.floatingLabelFixed := true,
            ^.value := self.state.refusalReason,
            ^.onChangeSelect := handleReasonRefusalChange,
            ^.required := true
          )(self.state.reasons.map { reason =>
            <.MenuItem(^.key := reason, ^.value := reason, ^.primaryText := reason)()
          })

          val errorMessage: Option[Element] =
            self.state.errorMessage.map(msg => <.p()(msg))

          <.Card(^.style := Map("marginTop" -> "1em"))(
            <.CardTitle(^.title := "I want to refuse that proposal")(),
            <.CardActions()(
              selectReasons,
              <.Checkbox(
                ^.label := "Notify User",
                ^.checked := self.state.notifyUser,
                ^.onCheck := handleNotifyUserChange,
                ^.style := Map("maxWidth" -> "25em")
              )(),
              <.RaisedButton(
                ^.disabled := self.state.isLocked,
                ^.label := "Confirm refusal",
                ^.onClick := handleSubmitRefuse
              )(),
              <.RaisedButton(
                ^.style := Map("float" -> "right"),
                ^.label := "Next Proposal",
                ^.onClick := handleNextProposal,
                ^.disabled := self.state.isLocked
              )(),
              errorMessage
            )
          )
      })
    )
}

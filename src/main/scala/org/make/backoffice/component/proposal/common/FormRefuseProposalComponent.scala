/*
 * Make.org Control Center
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package org.make.backoffice.component.proposal.common

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.statictags.Element
import org.make.backoffice.client.NotFoundHttpException
import org.make.backoffice.component.Main
import org.make.backoffice.component.proposal.common.ShowProposalComponents.Context
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.SingleProposal
import org.make.backoffice.service.proposal.ProposalService
import org.make.backoffice.util.Configuration

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object FormRefuseProposalComponent {
  case class FormRefuseProposalProps(proposal: SingleProposal, isLocked: Boolean = false, context: Context)
  case class FormRefuseProposalState(reasons: Seq[String],
                                     refusalReason: String = "Other",
                                     notifyUser: Boolean = true,
                                     errorMessage: Option[String] = None,
                                     isLocked: Boolean = false)

  val reasons: Seq[String] = Configuration.getReasonsForRefusal

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[FormRefuseProposalProps, FormRefuseProposalState](getInitialState = { _ =>
        FormRefuseProposalState(reasons = reasons)
      }, componentWillReceiveProps = { (self, props) =>
        self.setState(_.copy(isLocked = props.wrapped.isLocked, refusalReason = "Other", notifyUser = true))
      }, render = {
        self =>
          def handleReasonRefusalChange: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
            self.setState(_.copy(refusalReason = value))
          }

          def handleNotifyUserChange: (js.Object, Boolean) => Unit = { (_, checked) =>
            self.setState(_.copy(notifyUser = checked))
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
                    .nextProposalToModerate(
                      self.props.wrapped.proposal.operationId.toOption,
                      self.props.wrapped.proposal.themeId.toOption,
                      Some(self.props.wrapped.proposal.country),
                      Some(self.props.wrapped.proposal.language),
                      toEnrich = false,
                      minVotesCount = None,
                      minScore = None
                    )
                } yield nextProposal
              futureNextProposal.onComplete {
                case Success(proposalResponse) =>
                  self.props.history.push(s"/nextProposal/${proposalResponse.data.id}")
                case Failure(NotFoundHttpException) => self.props.history.push("/proposals")
                case Failure(_) =>
                  self.setState(_.copy(errorMessage = Some(Main.defaultErrorMessage)))
              }
          }

          def handleRefuse: SyntheticEvent => Unit = {
            event =>
              event.preventDefault()
              ProposalService
                .refuseProposal(
                  proposalId = self.props.wrapped.proposal.id,
                  refusalReason = Option(self.state.refusalReason),
                  notifyUser = self.state.notifyUser
                )
                .onComplete {
                  case Success(_) =>
                    self.props.history.goBack()
                  case Failure(NotFoundHttpException) => self.props.history.push("/proposals")
                  case Failure(_) =>
                    self.setState(_.copy(errorMessage = Some(Main.defaultErrorMessage)))
                }
          }

          def handleSubmit: SyntheticEvent => Unit = {
            if (self.props.wrapped.context == ShowProposalComponents.Context.StartModeration) {
              handleNextProposal
            } else {
              handleRefuse
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
            <.CardTitle(^.title := "I want to refuse this proposal")(),
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
                ^.onClick := handleSubmit
              )(),
              errorMessage
            )
          )
      })
    )
}

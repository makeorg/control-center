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
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.statictags.Element
import org.make.backoffice.client.{BadRequestHttpException, NotFoundHttpException}
import org.make.backoffice.component.Main
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model._
import org.make.backoffice.service.proposal.ProposalService
import org.make.backoffice.util.Configuration
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object FormValidateProposalComponent {

  case class FormValidateProposalProps(proposal: SingleProposal,
                                       action: String,
                                       isLocked: Boolean = false,
                                       context: ShowProposalComponents.Context)
  case class FormValidateProposalState(content: String,
                                       maxLength: Int,
                                       notifyUser: Boolean = true,
                                       errorMessage: Seq[String] = Seq.empty,
                                       isLocked: Boolean = false)

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[FormValidateProposalProps, FormValidateProposalState](
          displayName = "FormValidateProposalComponent",
          getInitialState = { self =>
            FormValidateProposalState(
              content = self.props.wrapped.proposal.content,
              maxLength = Configuration.proposalMaxLength,
              isLocked = self.props.wrapped.isLocked
            )
          },
          componentWillReceiveProps = { (self, props) =>
            self.setState(_.copy(isLocked = props.wrapped.isLocked))
            if (self.props.wrapped.proposal.id != props.wrapped.proposal.id) {
              self.setState(_.copy(content = props.wrapped.proposal.content))
            }
          },
          render = { self =>
            def handleContentEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
              val newContent: String = event.target.value.substring(0, self.state.maxLength)
              self.setState(_.copy(content = newContent))
            }

            def handleNotifyUserChange: (js.Object, Boolean) => Unit = { (_, checked) =>
              self.setState(_.copy(notifyUser = checked))
            }

            def handleSubmitValidate: SyntheticEvent => Unit = {
              event =>
                event.preventDefault()
                val maybeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                ProposalService
                  .validateProposal(
                    proposalId = self.props.wrapped.proposal.id,
                    newContent = maybeNewContent,
                    sendNotificationEmail = self.state.notifyUser,
                    questionId = self.props.wrapped.proposal.questionId.toOption.map(QuestionId.apply)
                  )
                  .onComplete {
                    case Success(_) =>
                      self.props.history.goBack()
                      self.setState(_.copy(errorMessage = Seq.empty))
                    case Failure(BadRequestHttpException(errors)) =>
                      self.setState(_.copy(errorMessage = errors.map(_.message.getOrElse(""))))
                    case Failure(_) =>
                      self.setState(_.copy(errorMessage = Seq(Main.defaultErrorMessage)))
                  }
            }

            def handleSubmitUpdate: SyntheticEvent => Unit = {
              event =>
                event.preventDefault()
                val maybeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                ProposalService
                  .updateProposal(
                    proposalId = self.props.wrapped.proposal.id,
                    newContent = maybeNewContent,
                    questionId = self.props.wrapped.proposal.questionId.toOption.map(QuestionId.apply)
                  )
                  .onComplete {
                    case Success(_) =>
                      self.props.history.goBack()
                      self.setState(_.copy(errorMessage = Seq.empty))
                    case Failure(BadRequestHttpException(errors)) =>
                      self.setState(_.copy(errorMessage = errors.map(_.message.getOrElse(""))))
                    case Failure(_) =>
                      self.setState(_.copy(errorMessage = Seq(Main.defaultErrorMessage)))
                  }
            }

            def handleNextProposal: SyntheticEvent => Unit = {
              event =>
                event.preventDefault()
                val maybeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                val futureNextProposal =
                  for {
                    _ <- ProposalService.validateProposal(
                      proposalId = self.props.wrapped.proposal.id,
                      newContent = maybeNewContent,
                      sendNotificationEmail = self.state.notifyUser,
                      questionId = self.props.wrapped.proposal.questionId.toOption.map(QuestionId.apply)
                    )
                    nextProposal <- ProposalService
                      .nextProposalToModerate(
                        self.props.wrapped.proposal.questionId.toOption,
                        toEnrich = false,
                        minVotesCount = None,
                        minScore = None
                      )
                  } yield nextProposal
                futureNextProposal.onComplete {
                  case Success(proposalResponse) =>
                    self.props.history.push(s"/nextProposal/${proposalResponse.data.id}")
                  case Failure(NotFoundHttpException) => self.props.history.push("/proposals")
                  case Failure(BadRequestHttpException(errors)) =>
                    self.setState(_.copy(errorMessage = errors.map(_.message.getOrElse(""))))
                  case Failure(_) =>
                    self.setState(_.copy(errorMessage = Seq(Main.defaultErrorMessage)))
                }
            }

            def handleSubmit: SyntheticEvent => Unit = {
              if (self.props.wrapped.action == "validate" &&
                  self.props.wrapped.context == ShowProposalComponents.Context.StartModeration) {
                handleNextProposal
              } else if (self.props.wrapped.action == "validate") {
                handleSubmitValidate
              } else {
                handleSubmitUpdate
              }
            }

            val errorMessage: Seq[Element] =
              self.state.errorMessage.map(msg => <.p()(msg))

            <.Card(^.style := Map("marginTop" -> "1em"))(
              <.CardTitle(^.title := s"I want to ${self.props.wrapped.action} this proposal")(),
              <.CardActions()(
                <.TextFieldMaterialUi(
                  ^.floatingLabelText := "Proposal content",
                  ^.value := self.state.content,
                  ^.onChange := handleContentEdition,
                  ^.fullWidth := true
                )(),
                <.span()(s"${self.state.content.length}/${self.state.maxLength}"),
                <.br()(),
                <.Checkbox(
                  ^.disabled := self.props.wrapped.action == "update",
                  ^.label := "Notify user",
                  ^.checked := self.state.notifyUser && self.props.wrapped.action == "validate",
                  ^.onCheck := handleNotifyUserChange,
                  ^.style := Map("maxWidth" -> "25em")
                )(),
                <.RaisedButton(
                  ^.style := Map("marginTop" -> "1em"),
                  ^.label := s"Confirm ${if (self.props.wrapped.action == "validate") "validation" else "changes"}",
                  ^.onClick := handleSubmit,
                  ^.disabled := self.state.isLocked
                )(),
                errorMessage
              )
            )
          }
        )
    )
}

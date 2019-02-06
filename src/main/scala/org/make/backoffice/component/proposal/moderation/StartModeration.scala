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

package org.make.backoffice.component.proposal.moderation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.{RouterProps, WithRouter}
import org.make.backoffice.client.request.Filter
import org.make.backoffice.client.{BadRequestHttpException, NotFoundHttpException}
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.Question
import org.make.backoffice.service.proposal.{Pending, ProposalService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object StartModeration {

  final case class StartModerationProps(questions: Seq[Question]) extends RouterProps
  final case class StartModerationState(questionId: Option[String],
                                        snackbarOpen: Boolean = false,
                                        errorMessage: String = "",
                                        proposalsAmount: Int = 0)

  val reactClass: ReactClass =
    WithRouter(
      React.createClass[StartModerationProps, StartModerationState](displayName = "StartModeration", getInitialState = {
        _ =>
          StartModerationState(None)
      }, render = {
        self =>
          def onSelectQuestion: (js.Object, js.UndefOr[Int], String) => Unit = {
            (_, _, value) =>
              self.setState(_.copy(questionId = Some(value)))
              ProposalService
                .proposals(
                  None,
                  None,
                  Some(Seq(Filter("questionId", value), Filter("status", s"${Pending.shortName}")))
                )
                .onComplete {
                  case Success(proposalsTotal) => self.setState(_.copy(proposalsAmount = proposalsTotal.total))
                  case Failure(NotFoundHttpException) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "No proposal found"))
                  case Failure(_) => self.setState(_.copy(snackbarOpen = true, errorMessage = "Internal Error"))
                }
          }

          def onClickStartModeration: SyntheticEvent => Unit = {
            event =>
              event.preventDefault()
              ProposalService
                .nextProposalToModerate(self.state.questionId, toEnrich = false, minVotesCount = None, minScore = None)
                .onComplete {
                  case Success(proposal) => self.props.history.push(s"/nextProposal/${proposal.data.id}")
                  case Failure(NotFoundHttpException) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "No proposal found"))
                  case Failure(BadRequestHttpException(_)) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "Bad request"))
                  case Failure(_) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "Internal Error"))
                }
          }

          <.Card()(
            <.CardTitle(^.title := "Proposals validation")(),
            <.CardHeader(^.title := self.state.proposalsAmount.toString + " proposals to moderate")(),
            <.SelectField(
              ^.style := Map("margin" -> "0 1em", "width" -> "80%"),
              ^.floatingLabelText := "Question",
              ^.value := self.state.questionId.getOrElse(""),
              ^.onChangeSelect := onSelectQuestion
            )(self.props.wrapped.questions.map { question =>
              <.MenuItem(
                ^.key := question.id,
                ^.value := question.id,
                ^.primaryText := s"${question.slug} : ${question.question}"
              )()
            }),
            <.CardActions()(
              <.RaisedButton(^.label := "Start Moderation", ^.primary := true, ^.onClick := onClickStartModeration)()
            ),
            <.Snackbar(
              ^.open := self.state.snackbarOpen,
              ^.message := self.state.errorMessage,
              ^.autoHideDuration := 5000,
              ^.onRequestClose := (_ => self.setState(_.copy(snackbarOpen = false)))
            )()
          )
      })
    )
}

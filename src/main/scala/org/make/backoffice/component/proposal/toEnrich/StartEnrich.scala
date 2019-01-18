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

package org.make.backoffice.component.proposal.toEnrich

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
import org.make.backoffice.service.proposal.{Accepted, ProposalService}
import org.make.backoffice.util.Configuration

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object StartEnrich {

  final case class StartEnrichProps(questions: Seq[Question]) extends RouterProps
  final case class StartEnrichState(questionId: Option[String],
                                    minVotesCount: Option[String],
                                    toEnrichMinScore: Option[String],
                                    snackbarOpen: Boolean = false,
                                    errorMessage: String = "",
                                    proposalsAmount: Int = 0)

  val reactClass: ReactClass =
    WithRouter(
      React.createClass[StartEnrichProps, StartEnrichState](displayName = "StartEnrich", getInitialState = { _ =>
        StartEnrichState(None, None, None)
      }, render = { self =>
        def onSelectQuestion: (js.Object, js.UndefOr[Int], String) => Unit = {
          (_, _, value) =>
            self.setState(_.copy(questionId = Some(value)))
            ProposalService
              .proposals(
                None,
                None,
                Some(
                  Seq(
                    Filter("questionId", self.state.questionId.getOrElse("")),
                    Filter("status", s"${Accepted.shortName}"),
                    Filter("toEnrich", true),
                    Filter("minVotesCount", self.state.minVotesCount.getOrElse(Configuration.toEnrichMinVotesCount)),
                    Filter("minScore", self.state.toEnrichMinScore.getOrElse(Configuration.toEnrichMinScore))
                  )
                )
              )
              .onComplete {
                case Success(proposalsTotal) => self.setState(_.copy(proposalsAmount = proposalsTotal.total))
                case Failure(NotFoundHttpException) =>
                  self.setState(_.copy(snackbarOpen = true, errorMessage = "No proposal found"))
                case Failure(_) => self.setState(_.copy(snackbarOpen = true, errorMessage = "Internal Error"))
              }
        }

        def onChangeMinVotesCount: (js.Object, String) => Unit = { (_, value) =>
          self.setState(_.copy(minVotesCount = Some(value)))
        }

        def onChangeMinScore: (js.Object, String) => Unit = { (_, value) =>
          self.setState(_.copy(toEnrichMinScore = Some(value)))
        }

        def onClickStartModeration: SyntheticEvent => Unit = {
          event =>
            val minVotesCount = self.state.minVotesCount.getOrElse(Configuration.toEnrichMinVotesCount)
            val minScore = self.state.toEnrichMinScore.getOrElse(Configuration.toEnrichMinScore)
            event.preventDefault()
            ProposalService
              .nextProposalToModerate(
                self.state.questionId,
                toEnrich = true,
                minVotesCount = Some(minVotesCount),
                minScore = Some(minScore)
              )
              .onComplete {
                case Success(proposal) =>
                  self.props.history
                    .push(s"/nextProposal/${proposal.data.id}?minVotesCount=$minVotesCount&minScore=$minScore")
                case Failure(NotFoundHttpException) =>
                  self.setState(_.copy(snackbarOpen = true, errorMessage = "No proposal found"))
                case Failure(BadRequestHttpException(_)) =>
                  self.setState(_.copy(snackbarOpen = true, errorMessage = "Bad request"))
                case Failure(_) =>
                  self.setState(_.copy(snackbarOpen = true, errorMessage = "Internal Error"))
              }
        }

        <.Card()(
          <.CardTitle(^.title := "Proposals enrichment")(),
          <.CardHeader(^.title := self.state.proposalsAmount.toString + " proposals to enrich")(),
          <.TextFieldMaterialUi(
            ^.style := Map("margin" -> "0 1em"),
            ^.floatingLabelText := "Minimum votes count",
            ^.value := self.state.minVotesCount.getOrElse(Configuration.toEnrichMinVotesCount),
            ^.onChangeTextField := onChangeMinVotesCount
          )(),
          <.TextFieldMaterialUi(
            ^.style := Map("margin" -> "0 1em"),
            ^.floatingLabelText := "Minimum score",
            ^.value := self.state.toEnrichMinScore.getOrElse(Configuration.toEnrichMinScore),
            ^.onChangeTextField := onChangeMinScore
          )(),
          <.br.empty,
          <.SelectField(
            ^.style := Map("margin" -> "0 1em", "width" -> "80%"),
            ^.floatingLabelText := "Question",
            ^.value := self.state.questionId.getOrElse(""),
            ^.onChangeSelect := onSelectQuestion
          )(self.props.wrapped.questions.map { question =>
            <.MenuItem(
              ^.key := question.id,
              ^.value := question.id,
              ^.primaryText := s"${question.question} (${question.slug})"
            )()
          }),
          <.CardActions()(
            <.RaisedButton(^.label := "Start Enrichment", ^.primary := true, ^.onClick := onClickStartModeration)()
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

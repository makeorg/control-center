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

package org.make.backoffice.component

import java.time.LocalDate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.request.Filter
import org.make.backoffice.component.proposal.moderation.StartModeration.StartModerationProps
import org.make.backoffice.component.proposal.moderation.StartValidationWithTags.StartValidationWithTagsProps
import org.make.backoffice.component.proposal.toEnrich.StartEnrich.StartEnrichProps
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.facade.ViewTitle._
import org.make.backoffice.model.Question
import org.make.backoffice.service.question.QuestionService
import org.make.backoffice.service.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object Dashboard {

  final case class DashboardState(questionsList: Seq[Question])

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React.createClass[RouterProps, DashboardState](
      displayName = "Dashboard",
      getInitialState = _ => {
        DashboardState(Seq.empty)
      },
      componentDidMount = self => {
        UserService.me.onComplete {
          case Success(_) =>
          case Failure(_) => self.props.history.push("/login")
        }
        QuestionService
          .questions(filters = Some(Seq(Filter("openAt", LocalDate.now()))))
          .onComplete {
            case Success(questionsResponse) =>
              self.setState(
                _.copy(questionsList = questionsResponse.data.filterNot(_.slug.contains("huffpost")).sortBy(_.slug))
              )
            case Failure(e) => js.Dynamic.global.console.log(e.getMessage)
          }
      },
      render = self => {
        <.div()(
          <.Card()(<.ViewTitle(^.title := "Dashboard")()),
          <.br.empty,
          <.StartModeration(^.wrapped := StartModerationProps(self.state.questionsList))(),
          <.br.empty,
          <.StartValidationWithTags(^.wrapped := StartValidationWithTagsProps(self.state.questionsList))(),
          <.br.empty,
          <.StartEnrich(^.wrapped := StartEnrichProps(self.state.questionsList))()
        )
      }
    )
}

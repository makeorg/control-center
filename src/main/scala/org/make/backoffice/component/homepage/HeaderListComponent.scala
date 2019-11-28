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

package org.make.backoffice.component.homepage

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.homepage.HeaderComponent.HeaderComponentProps
import org.make.backoffice.model.{FeaturedOperation, Question}
import org.make.backoffice.service.operation.FeaturedOperationService
import org.make.backoffice.service.question.QuestionService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object HeaderListComponent {

  case class HeaderListComponentState(featuredOperations: Seq[FeaturedOperation],
                                      questionsList: Seq[Question],
                                      reload: Boolean)

  lazy val reactClass: ReactClass = React.createClass[Unit, HeaderListComponentState](
    displayName = "HeaderListComponent",
    getInitialState = _ => HeaderListComponentState(Seq.empty, Seq.empty, reload = false),
    componentWillMount = self => {
      FeaturedOperationService.featuredOperations.onComplete {
        case Success(featuredOperations) => self.setState(_.copy(featuredOperations = featuredOperations))
        case Failure(_)                  =>
      }
      QuestionService
        .questions()
        .onComplete {
          case Success(questionsResponse) =>
            self.setState(
              _.copy(questionsList = questionsResponse.data.filterNot(_.slug.contains("huffpost")).sortBy(_.slug))
            )
          case Failure(e) => js.Dynamic.global.console.log(e.getMessage)
        }
    },
    componentDidUpdate = (self, _, state) => {
      if (state.reload) {
        FeaturedOperationService.featuredOperations.onComplete {
          case Success(featuredOperations) => self.setState(_.copy(featuredOperations = featuredOperations))
          case Failure(_)                  =>
        }
      }
    },
    render = self => {

      def reloadComponent: () => Unit = { () =>
        self.setState(state => state.copy(reload = true))
      }

      if (!self.state.reload) {
        <.div()(self.state.featuredOperations.sortBy(_.slot).map { featuredOperation =>
          <.HeaderComponent(
            ^.wrapped := HeaderComponentProps(
              featuredOperation = Some(featuredOperation),
              slot = featuredOperation.slot,
              questionsList = self.state.questionsList,
              reloadComponent = reloadComponent
            )
          )()
        }, for {
          i <- self.state.featuredOperations.size until 4
        } yield {
          <.HeaderComponent(
            ^.wrapped := HeaderComponentProps(
              featuredOperation = None,
              slot = i + 1,
              questionsList = self.state.questionsList,
              reloadComponent = reloadComponent
            )
          )()
        })
      } else {
        self.setState(_.copy(reload = false))
        <.div.empty
      }
    }
  )

}

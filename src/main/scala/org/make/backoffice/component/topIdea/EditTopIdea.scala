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

package org.make.backoffice.component.topIdea

import io.github.shogowada.scalajs.reactjs.{redux, React}
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.{RouterProps, WithRouter}
import org.make.backoffice.client.Resource
import org.make.backoffice.client.request.{Filter, Pagination, Sort}
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.facade.AdminOnRest.required
import org.make.backoffice.facade.Choice
import org.make.backoffice.model.{AppState, Idea}
import org.make.backoffice.service.idea.IdeaService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object EditTopIdea {

  case class EditProps(questionId: String) extends RouterProps
  case class EditState(ideaList: Seq[Idea])

  def selectorFactory: Dispatch => (AppState, Props[Unit]) => EditProps =
    (_: Dispatch) =>
      (state: AppState, _: Props[Unit]) => {
        val questionId = state.form.`record-form`.flatMap(_.values.flatMap(_.questionId))
        EditProps(questionId.getOrElse(""))
    }

  def apply(): ReactClass = WithRouter(redux.ReactRedux.connectAdvanced(selectorFactory)(reactClass))

  private lazy val reactClass: ReactClass =
    React
      .createClass[EditProps, EditState](
        displayName = "EditTopIdea",
        getInitialState = { _ =>
          EditState(ideaList = Seq.empty)
        },
        componentWillReceiveProps = { (self, props) =>
          if (self.props.wrapped.questionId != props.wrapped.questionId) {
            IdeaService
              .listIdeas(
                pagination = Some(Pagination(page = 1, perPage = 500)),
                sort = Some(Sort(field = Some("name"), order = Some("ASC"))),
                filters = Some(Seq(Filter(field = "questionId", value = props.wrapped.questionId)))
              )
              .onComplete {
                case Success(ideaList) =>
                  self.setState(_.copy(ideaList = ideaList))
                case Failure(_) =>
              }
          }
        },
        render = { self =>
          val ideaChoices: Seq[Choice] = self.state.ideaList.map { idea =>
            Choice(id = idea.id, name = idea.name)
          }

          <.Edit(^.resource := Resource.topIdeas, ^.location := self.props.location, ^.`match` := self.props.`match`)(
            <.SimpleForm()(
              <.TextInput(^.source := "name", ^.options := Map("fullWidth" -> true), ^.validate := required)(),
              <.TextInput(^.source := "label", ^.options := Map("fullWidth" -> true), ^.validate := required)(),
              <.ReferenceInput(
                ^.source := "questionId",
                ^.label := "question",
                ^.translateLabel := ((label: String) => label),
                ^.reference := Resource.questions,
                ^.perPage := 500,
                ^.sort := Map("field" -> "slug", "order" -> "ASC"),
              )(<.SelectInput(^.optionText := "slug", ^.options := Map("fullWidth" -> true), ^.validate := required)()),
              <.SelectInput(
                ^.source := "ideaId",
                ^.label := "idea",
                ^.translateLabel := ((label: String) => label),
                ^.choices := ideaChoices,
                ^.allowEmpty := false,
                ^.validate := required,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.NumberInput(
                ^.label := "Total proposal Ratio",
                ^.source := "scores.totalProposalsRatio",
                ^.options := Map("fullWidth" -> true)
              )(),
              <.NumberInput(
                ^.label := "Agreement Ratio",
                ^.source := "scores.agreementRatio",
                ^.options := Map("fullWidth" -> true)
              )(),
              <.NumberInput(
                ^.label := "Like It Ratio",
                ^.source := "scores.likeItRatio",
                ^.options := Map("fullWidth" -> true)
              )(),
              <.NumberInput(^.source := "weight", ^.options := Map("fullWidth" -> true), ^.validate := required)()
            )
          )
        }
      )

}

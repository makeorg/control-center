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
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.backoffice.client.request.Filter
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.autoComplete.AutoComplete.AutoCompleteProps
import org.make.backoffice.component.proposal.common.NewIdeaComponent.NewIdeaProps
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.DataSourceConfig
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model._
import org.make.backoffice.service.idea.IdeaService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object NewIdeaComponent {

  case class NewIdeaProps(setProposalIdea: Option[IdeaId] => Unit, setIdeas: Idea => Unit, questionId: Option[String])
  case class NewIdeaState(ideaName: String, open: Boolean = false)

  private def createIdea(self: React.Self[NewIdeaProps, NewIdeaState]): SyntheticEvent => Unit = { _ =>
    IdeaService
      .createIdea(name = self.state.ideaName, questionId = self.props.wrapped.questionId)
      .onComplete {
        case Success(ideaResponse) =>
          self.props.wrapped.setProposalIdea(Some(IdeaId(ideaResponse.data.id)))
          self.props.wrapped.setIdeas(ideaResponse.data)
          self.setState(_.copy(open = false))
        case Failure(e) => js.Dynamic.global.console.log(s"Fail to create idea: $e")
      }
  }

  private def handleOpen(self: React.Self[NewIdeaProps, NewIdeaState]): SyntheticEvent => Unit = { event =>
    event.preventDefault()
    self.setState(_.copy(open = true))
  }

  private def handleClose(self: React.Self[NewIdeaProps, NewIdeaState]): SyntheticEvent => Unit = { event =>
    event.preventDefault()
    self.setState(_.copy(open = false))
  }

  private def actionsModal(self: React.Self[NewIdeaProps, NewIdeaState]): Seq[ReactElement] = {
    Seq(
      <.FlatButton(^.label := "Cancel", ^.secondary := true, ^.onClick := handleClose(self))(),
      <.FlatButton(^.label := "Create", ^.primary := true, ^.onClick := createIdea(self))()
    )
  }

  lazy val reactClass: ReactClass =
    React
      .createClass[NewIdeaProps, NewIdeaState](
        displayName = "NewIdea",
        getInitialState = _ => NewIdeaState(ideaName = "new_idea"),
        render = { self =>
          def handleIdeaNameEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val newContent: String = event.target.value
            self.setState(_.copy(ideaName = newContent))
          }

          <.div(^.style := Map("display" -> "inline-block"))(
            <.FlatButton(^.label := "New idea", ^.primary := true, ^.onClick := handleOpen(self))(),
            <.Dialog(
              ^.title := "Choose your idea name",
              ^.open := self.state.open,
              ^.modal := true,
              ^.actionsModal := actionsModal(self)
            )(
              <.TextFieldMaterialUi(
                ^.value := self.state.ideaName,
                ^.onChange := handleIdeaNameEdition,
                ^.floatingLabelText := "idea name",
                ^.fullWidth := true
              )()
            )
          )
        }
      )

}

object ProposalIdeaComponent {
  case class ProposalIdeaProps(proposal: SingleProposal,
                               setProposalIdea: Option[IdeaId] => Unit,
                               ideaName: String,
                               handleCheckIdeaAuto: (js.Object, Boolean) => Unit,
                               ideaAuto: Boolean)
  case class ProposalIdeaState(selectedIdeaId: Option[IdeaId], ideaName: Option[String], isLoading: Boolean = true)

  lazy val reactClass: ReactClass =
    React.createClass[ProposalIdeaProps, ProposalIdeaState](
      displayName = "ProposalIdea",
      getInitialState = { _ =>
        ProposalIdeaState(selectedIdeaId = None, ideaName = None)
      },
      componentDidMount = { self =>
        self.setState(_.copy(selectedIdeaId = self.props.wrapped.proposal.ideaId.map(IdeaId(_)).toOption))
        if (self.props.wrapped.ideaName.isEmpty) {
          self.props.wrapped.proposal.ideaId.foreach { ideaId =>
            IdeaService.getIdea(ideaId).onComplete {
              case Success(ideaResponse) => self.setState(_.copy(ideaName = Some(ideaResponse.data.name)))
              case Failure(e)            => js.Dynamic.global.console.log(s"Failed with error $e")
            }
          }
        }
      },
      componentWillReceiveProps = { (self, props) =>
        if (self.props.wrapped.proposal.id != props.wrapped.proposal.id) {
          self.setState(_.copy(selectedIdeaId = None, ideaName = None, isLoading = true))
        }
      },
      render = { self =>
        def handleNewRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
          val idea = chosenRequest.asInstanceOf[Idea]
          val selectedIdea = Some(IdeaId(idea.id))
          self.setState(_.copy(selectedIdeaId = selectedIdea, ideaName = Some(idea.name)))
          self.props.wrapped.setProposalIdea(selectedIdea)
        }

        def setIdeas(newIdea: Idea): Unit = {
          self.setState(_.copy(selectedIdeaId = Some(IdeaId(newIdea.id))))
        }

        def ideaSearchRequest: Option[String] => Future[Seq[Idea]] = { name =>
          IdeaService
            .listIdeas(
              pagination = None,
              filters = Some(
                Seq(
                  Filter(field = "questionId", value = self.props.wrapped.proposal.questionId),
                  Filter(field = "name", value = name.getOrElse(""))
                )
              )
            )
        }

        val searchNew: ReactElement =
          <.AutoCompleteComponent(
            ^.wrapped :=
              AutoCompleteProps(
                searchRequest = ideaSearchRequest,
                handleNewRequest = handleNewRequest,
                dataSourceConfig = DataSourceConfig("name", "id")
              )
          )()

        <.Card(^.style := Map("marginTop" -> "1em"))(
          <.CardTitle(^.title := "Idea")(),
          <.CardText()(
            <.Checkbox(
              ^.label := "Idea auto",
              ^.checked := self.props.wrapped.ideaAuto,
              ^.onCheck := self.props.wrapped.handleCheckIdeaAuto,
              ^.style := Map("maxWidth" -> "25em")
            )()
          ),
          if (!self.props.wrapped.ideaAuto) {
            Seq(<.CardText()(if (self.state.ideaName.getOrElse(self.props.wrapped.ideaName).nonEmpty) {
              <.TextFieldMaterialUi(
                ^.value := self.state.ideaName.getOrElse(self.props.wrapped.ideaName),
                ^.name := self.state.ideaName.getOrElse(self.props.wrapped.ideaName),
                ^.readOnly := true,
                ^.underlineShow := false,
                ^.style := Map("width" -> "80%")
              )()
            }, if (self.state.selectedIdeaId.isDefined) {
              <.CardActions(^.style := Map("float" -> "left"))(
                <.EditButton(
                  ^.label := "Edit Idea",
                  ^.basePath := "/ideas",
                  ^.translateLabel := ((label: String) => label),
                  ^.record := js.Dynamic.literal("id" -> self.state.selectedIdeaId.map(_.value).getOrElse("").toString)
                )()
              )
            }), <.CardActions()(<.CardActions()(searchNew), <.br()(), <.NewIdeaComponent(^.wrapped := NewIdeaProps(self.props.wrapped.setProposalIdea, setIdeas, self.props.wrapped.proposal.questionId.toOption))()))
          }
        )
      }
    )
}

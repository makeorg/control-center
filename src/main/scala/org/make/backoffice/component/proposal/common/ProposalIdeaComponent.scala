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
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.backoffice.client.ListTotalResponse
import org.make.backoffice.client.request.{Filter, Pagination}
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.proposal.common.NewIdeaComponent.NewIdeaProps
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.DataSourceConfig
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model._
import org.make.backoffice.service.idea.IdeaService
import org.make.backoffice.service.proposal.ProposalService
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
  case class ProposalIdeaProps(proposal: SingleProposal, setProposalIdea: Option[IdeaId] => Unit, ideaName: String)
  case class ProposalIdeaState(ideas: Seq[Idea],
                               selectedIdeaId: Option[IdeaId],
                               searchIdeaContent: String,
                               foundProposalIdeas: Seq[Idea],
                               similarResult: Seq[SimilarResult],
                               ideaName: Option[String],
                               isLoading: Boolean = true)

  def loadIdeas(self: Self[ProposalIdeaProps, ProposalIdeaState],
                props: ProposalIdeaProps): Future[ListTotalResponse[Idea]] = {

    IdeaService
      .listIdeas(
        pagination = Some(Pagination(page = 1, perPage = 1000)),
        filters = Some(Seq(Filter(field = "questionId", value = props.proposal.questionId)))
      )
  }

  def loadDuplicates(props: ProposalIdeaProps): Future[Seq[SimilarResult]] = {
    ProposalService.getDuplicates(props.proposal.id)
  }

  lazy val reactClass: ReactClass =
    React.createClass[ProposalIdeaProps, ProposalIdeaState](
      displayName = "ProposalIdea",
      getInitialState = { _ =>
        ProposalIdeaState(Seq.empty, None, "", Seq.empty, Seq.empty, None)
      },
      componentDidMount = { self =>
        loadIdeas(self, self.props.wrapped).onComplete {
          case Success(listIdeas) =>
            self.setState(_.copy(ideas = listIdeas.data.toSeq, foundProposalIdeas = listIdeas.data.toSeq))
          case Failure(e) => scalajs.js.Dynamic.global.console.log(s"get ideas failed with error $e")
        }
        loadDuplicates(self.props.wrapped).onComplete {
          case Success(similarResult) => self.setState(_.copy(similarResult = similarResult, isLoading = false))
          case Failure(e) =>
            self.setState(_.copy(isLoading = false))
            scalajs.js.Dynamic.global.console.log(s"get similar failed with error $e")
        }
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
          self.setState(
            _.copy(
              selectedIdeaId = None,
              searchIdeaContent = "",
              similarResult = Seq.empty,
              ideaName = None,
              isLoading = true
            )
          )
          loadDuplicates(props.wrapped).onComplete {
            case Success(similarResult) => self.setState(_.copy(similarResult = similarResult, isLoading = false))
            case Failure(e) =>
              self.setState(_.copy(isLoading = false))
              scalajs.js.Dynamic.global.console.log(s"get similar failed with error $e")
          }
        }
      },
      render = { self =>
        def handleUpdateInput: (String, js.Array[js.Object], js.Object) => Unit = (searchText, _, _) => {
          self.setState(_.copy(searchIdeaContent = searchText))
        }

        def handleNewRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
          val idea = chosenRequest.asInstanceOf[Idea]
          val selectedIdea = Some(IdeaId(idea.id))
          self.setState(_.copy(selectedIdeaId = selectedIdea, ideaName = Some(idea.name)))
          self.props.wrapped.setProposalIdea(selectedIdea)
        }

        def setIdeas(newIdea: Idea): Unit = {
          self.setState(
            _.copy(
              foundProposalIdeas = self.state.ideas ++ Seq(newIdea),
              ideas = self.state.ideas ++ Seq(newIdea),
              selectedIdeaId = Some(IdeaId(newIdea.id)),
              searchIdeaContent = newIdea.name
            )
          )
        }

        def filterAutoComplete: (String, String) => Boolean = (searchText, key) => {
          key.indexOf(searchText) != -1
        }

        val searchNew: ReactElement =
          <.AutoComplete(
            ^.id := "search-proposal-idea",
            ^.hintText := "Search idea",
            ^.dataSource := self.state.foundProposalIdeas,
            ^.dataSourceConfig := DataSourceConfig("name", "id"),
            ^.searchText := self.state.searchIdeaContent,
            ^.hintText := "Search idea",
            ^.onUpdateInput := handleUpdateInput,
            ^.onNewRequest := handleNewRequest,
            ^.fullWidth := true,
            ^.popoverProps := Map("canAutoPosition" -> true),
            ^.openOnFocus := true,
            ^.filterAutoComplete := filterAutoComplete,
            ^.menuProps := Map("maxHeight" -> 400)
          )()

        def onCheckSimilarIdea(ideaId: String,
                               ideaName: String): (FormSyntheticEvent[HTMLInputElement], Boolean) => Unit =
          (_, isChecked) => {
            if (isChecked) {
              self.setState(_.copy(selectedIdeaId = Some(IdeaId(ideaId)), ideaName = Some(ideaName)))
              self.props.wrapped.setProposalIdea(Some(IdeaId(ideaId)))
            } else {
              self.setState(_.copy(selectedIdeaId = None, ideaName = None))
              self.props.wrapped.setProposalIdea(self.props.wrapped.proposal.ideaId.map(IdeaId(_)).toOption)
            }

            self.setState(_.copy(searchIdeaContent = ""))
          }

        val similarResultsElement = if (self.state.isLoading) {
          <.CircularProgress()()
        } else {
          self.state.similarResult.map { idea =>
            <.Checkbox(
              ^.label := idea.ideaName,
              ^.checked := self.state.selectedIdeaId.exists(_.value == idea.ideaId),
              ^.onCheck := onCheckSimilarIdea(idea.ideaId, idea.ideaName)
            )()
          }
        }

        <.Card(^.style := Map("marginTop" -> "1em"))(
          <.CardTitle(^.title := "Idea", ^.subtitle := self.state.ideaName.getOrElse(self.props.wrapped.ideaName))(),
          if (self.state.selectedIdeaId.isDefined) {
            <.CardActions()(
              <.EditButton(
                ^.label := "Edit Idea",
                ^.basePath := "/ideas",
                ^.record := js.Dynamic.literal("id" -> self.state.selectedIdeaId.map(_.value).getOrElse("").toString)
              )()
            )
          },
          <.CardActions()(
            <.h4()("Similar ideas:"),
            similarResultsElement,
            searchNew,
            <.br()(),
            <.NewIdeaComponent(
              ^.wrapped := NewIdeaProps(
                self.props.wrapped.setProposalIdea,
                setIdeas,
                self.props.wrapped.proposal.questionId.toOption
              )
            )()
          )
        )
      }
    )
}

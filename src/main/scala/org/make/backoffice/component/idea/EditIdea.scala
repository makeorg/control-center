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

package org.make.backoffice.component.idea

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.client.request.{Filter, Pagination}
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.ShowButton._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.facade.DataSourceConfig
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model._
import org.make.backoffice.service.idea.IdeaService
import org.make.backoffice.service.proposal.{Accepted, ProposalService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.|
import scala.util.{Failure, Success}

object EditIdea {

  case class TitleState(idea: Idea)

  lazy val ideaTitle: ReactClass = React.createClass[Unit, TitleState](getInitialState = self => {
    val idea = self.props.native.record.asInstanceOf[Idea]
    TitleState(idea)
  }, componentWillUpdate = (self, props, _) => {
    val idea = props.native.record.asInstanceOf[Idea]
    self.setState(_.copy(idea))
  }, render = self => {
    <.h1()(self.state.idea.name)
  })

  case class DataGridProps(ideaId: Option[String],
                           operationId: Option[String],
                           themeId: Option[String],
                           country: Option[String],
                           language: Option[String])
  case class DataGridState(ideas: Seq[Idea] = Seq.empty,
                           proposalsIdeaList: Seq[Proposal] = Seq.empty,
                           proposalsSearchList: Seq[Proposal] = Seq.empty,
                           searchIdeaContent: String = "",
                           searchProposalContent: String = "",
                           selectedIdeaId: Option[IdeaId] = None,
                           selectedProposalToAdd: Option[Proposal] = None,
                           selectedIds: Seq[ProposalId] = Seq.empty,
                           snackbarUpdateOkOpen: Boolean = false,
                           snackbarAddOkOpen: Boolean = false,
                           snackbarKoOpen: Boolean = false,
                           shouldUpdate: Boolean = true)

  lazy val dataGrid: ReactClass =
    React
      .createClass[DataGridProps, DataGridState](
        displayName = "dataGrid",
        getInitialState = _ => DataGridState(),
        componentDidUpdate = (self, _, _) => {
          if (self.props.wrapped.operationId.nonEmpty || self.props.wrapped.themeId.nonEmpty) {
            self.setState(_.copy(shouldUpdate = false))
          }
          if (self.state.shouldUpdate && (self.props.wrapped.operationId.isDefined || self.props.wrapped.themeId.isDefined)) {
            var filters: Seq[Filter] = Seq(
              Filter(field = "country", value = self.props.wrapped.country.getOrElse("")),
              Filter(field = "language", value = self.props.wrapped.language.getOrElse(""))
            )

            if (self.props.wrapped.operationId.isDefined) {
              filters :+= Filter(field = "operationId", value = self.props.wrapped.operationId.getOrElse(""))
            }

            if (self.props.wrapped.themeId.isDefined) {
              filters :+= Filter(field = "themeId", value = self.props.wrapped.themeId.getOrElse(""))
            }
            ProposalService
              .proposals(
                Some(Pagination(page = 1, perPage = 5000)), //todo asynchronous search
                None,
                Some(filters :+ Filter(field = "status", value = js.Array(Accepted.shortName)))
              )
              .onComplete {
                case Success(proposals) =>
                  self.setState(
                    _.copy(
                      proposalsIdeaList = proposals.data.toSeq
                        .filter(proposal => proposal.ideaId.contains(self.props.wrapped.ideaId.getOrElse(""))),
                      proposalsSearchList = proposals.data.toSeq
                        .filterNot(proposal => proposal.ideaId.contains(self.props.wrapped.ideaId.getOrElse("")))
                    )
                  )
                case Failure(e) => js.Dynamic.global.console.log(s"Failed with error $e")
              }
            IdeaService.listIdeas(Some(Pagination(page = 1, perPage = 1000)), None, Some(filters)).onComplete {
              case Success(ideaResponse) =>
                self.setState(
                  _.copy(ideas = ideaResponse.data.filterNot(_.id == self.props.wrapped.ideaId.getOrElse("")))
                )
              case Failure(e) => js.Dynamic.global.console.log(s"Failed with error $e")
            }
          }
        },
        render = self => {
          def onRowSelection(ids: Seq[String]): js.Function1[js.Array[Int] | String, Unit] = rowNumber => {
            var selectedIds: Seq[String] = Seq.empty
            (rowNumber: Any) match {
              case rows: js.Array[_] if rows.nonEmpty => selectedIds = rows.map(n => ids(n.asInstanceOf[Int])).toSeq
              case all: String if all == "all"        => selectedIds = ids
              case _                                  => selectedIds = Seq.empty
            }
            self.setState(_.copy(selectedIds = selectedIds.map(ProposalId(_))))
          }

          def handleUpdateIdeaInput: (String, js.Array[js.Object], js.Object) => Unit = (searchText, _, _) => {
            self.setState(_.copy(searchIdeaContent = searchText))
          }

          def handleUpdateProposalInput: (String, js.Array[js.Object], js.Object) => Unit = (searchText, _, _) => {
            self.setState(_.copy(searchProposalContent = searchText))
          }

          def handleNewIdeaRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
            val idea = chosenRequest.asInstanceOf[Idea]
            self.setState(_.copy(selectedIdeaId = Some(IdeaId(idea.id))))
          }

          def handleNewProposalRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
            val proposal = chosenRequest.asInstanceOf[Proposal]
            self.setState(_.copy(selectedProposalToAdd = Some(proposal)))
          }

          def filterAutoComplete: (String, String) => Boolean = (searchText, key) => {
            key.indexOf(searchText) != -1
          }

          def onSnackbarClose: String => Unit = _ => {
            self.setState(_.copy(snackbarUpdateOkOpen = false, snackbarKoOpen = false, snackbarAddOkOpen = false))
          }

          def onClickChangeIdea: SyntheticEvent => Unit = {
            event =>
              event.preventDefault()
              self.state.selectedIdeaId match {
                case Some(ideaId) =>
                  if (self.state.selectedIds.nonEmpty) {
                    ProposalService.changeProposalsIdea(ideaId, self.state.selectedIds).onComplete {
                      case Success(_) =>
                        val newProposalsList = self.state.proposalsIdeaList
                          .filterNot(proposal => self.state.selectedIds.exists(_.value == proposal.id))
                        self.setState(
                          _.copy(
                            proposalsIdeaList = newProposalsList,
                            selectedIds = Seq.empty,
                            searchIdeaContent = "",
                            snackbarUpdateOkOpen = true
                          )
                        )
                      case Failure(_) => self.setState(_.copy(snackbarKoOpen = true))
                    }
                  }
                case None =>
              }
          }

          def onClickAddProposal: SyntheticEvent => Unit = event => {
            event.preventDefault()
            self.state.selectedProposalToAdd match {
              case Some(proposal) =>
                self.props.wrapped.ideaId.foreach {
                  ideaId =>
                    ProposalService
                      .changeProposalsIdea(IdeaId(ideaId), Seq(ProposalId(proposal.id)))
                      .onComplete {
                        case Success(_) =>
                          val newProposalsList = self.state.proposalsIdeaList :+ proposal
                          val newSearchList = self.state.proposalsSearchList.filterNot(_.id == proposal.id)
                          self.setState(
                            _.copy(
                              proposalsIdeaList = newProposalsList,
                              proposalsSearchList = newSearchList,
                              searchProposalContent = "",
                              selectedProposalToAdd = None,
                              snackbarAddOkOpen = true
                            )
                          )
                        case Failure(_) => self.setState(_.copy(snackbarKoOpen = true))
                      }
                }
              case None =>
            }
          }

          <.Card(^.style := Map("marginTop" -> "3em", "padding" -> "0px 1em 3em"))(
            <.CardHeader(^.title := "Add proposals")(),
            <.AutoComplete(
              ^.id := "search-proposal",
              ^.hintText := "Search proposals to add",
              ^.dataSource := self.state.proposalsSearchList,
              ^.dataSourceConfig := DataSourceConfig("content", "id"),
              ^.searchText := self.state.searchProposalContent,
              ^.onUpdateInput := handleUpdateProposalInput,
              ^.onNewRequest := handleNewProposalRequest,
              ^.fullWidth := true,
              ^.popoverProps := Map("canAutoPosition" -> false),
              ^.openOnFocus := true,
              ^.filterAutoComplete := filterAutoComplete,
              ^.menuProps := Map("maxHeight" -> 400)
            )(),
            <.FlatButton(
              ^.fullWidth := true,
              ^.label := "add proposal",
              ^.secondary := true,
              ^.onClick := onClickAddProposal
            )(),
            if (self.state.proposalsIdeaList.nonEmpty) {
              <.div()(
                <.CardTitle(
                  ^.title := "Proposals list",
                  ^.subtitle := s"${self.state.proposalsIdeaList.length} proposals"
                )(),
                <.Table(
                  ^.multiSelectable := true,
                  ^.onRowSelection := onRowSelection(self.state.proposalsIdeaList.map(_.id)),
                  ^.style := Map("tableLayout" -> "auto"),
                  ^.fixedHeader := false
                )(
                  <.TableHeader()(<.TableRow()(<.TableHeaderColumn()("Content"), <.TableHeaderColumn()())),
                  <.TableBody(^.deselectOnClickaway := false)(self.state.proposalsIdeaList.map { proposal =>
                    <.TableRow(
                      ^.key := proposal.id,
                      ^.selected := self.state.selectedIds.exists(_.value == proposal.id)
                    )(
                      <.TableRowColumn(^.style := Map("whiteSpace" -> "normal", "wordWrap" -> "break-word"))(
                        proposal.content
                      ),
                      <.TableRowColumn()(
                        <.ShowButton(
                          ^.basePath := "/validated_proposals",
                          ^.record := js.Dynamic.literal("id" -> proposal.id)
                        )()
                      )
                    )
                  })
                ),
                <.br()(),
                <.AutoComplete(
                  ^.id := "search-proposal-idea",
                  ^.hintText := "Search idea",
                  ^.dataSource := self.state.ideas,
                  ^.dataSourceConfig := DataSourceConfig("name", "id"),
                  ^.searchText := self.state.searchIdeaContent,
                  ^.onUpdateInput := handleUpdateIdeaInput,
                  ^.onNewRequest := handleNewIdeaRequest,
                  ^.fullWidth := true,
                  ^.popoverProps := Map("canAutoPosition" -> true),
                  ^.openOnFocus := true,
                  ^.filterAutoComplete := filterAutoComplete,
                  ^.menuProps := Map("maxHeight" -> 400)
                )(),
                <.FlatButton(
                  ^.fullWidth := true,
                  ^.label := "Change idea",
                  ^.secondary := true,
                  ^.onClick := onClickChangeIdea
                )(),
                <.Snackbar(
                  ^.open := self.state.snackbarUpdateOkOpen,
                  ^.message := "Proposal(s) updated successfully",
                  ^.autoHideDuration := 3000,
                  ^.onRequestClose := onSnackbarClose
                )(),
                <.Snackbar(
                  ^.open := self.state.snackbarAddOkOpen,
                  ^.message := "Proposal(s) added successfully",
                  ^.autoHideDuration := 3000,
                  ^.onRequestClose := onSnackbarClose
                )(),
                <.Snackbar(
                  ^.open := self.state.snackbarKoOpen,
                  ^.message := "Proposal(s) update failed",
                  ^.autoHideDuration := 3000,
                  ^.onRequestClose := onSnackbarClose,
                  ^.bodyStyle := Map("backgroundColor" -> "red")
                )()
              )
            } else {
              <.CardTitle(^.title := "No proposals")()
            }
          )
        }
      )

  case class EditProps() extends RouterProps
  case class EditState(idea: Option[Idea])

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[EditProps, EditState](
        displayName = "EditIdea",
        getInitialState = _ => EditState(None),
        componentDidMount = self => {
          IdeaService.getIdea(self.props.`match`.params.getOrElse("id", "")).onComplete {
            case Success(ideaResponse) => self.setState(_.copy(Some(ideaResponse.data)))
            case Failure(e)            => js.Dynamic.global.console.log(s"Failed with error: $e")
          }
        },
        render = self => {
          <.div()(
            <.Edit(
              ^.resource := Resource.ideas,
              ^.location := self.props.location,
              ^.`match` := self.props.`match`,
              ^.editTitle := <.IdeaTitle()()
            )(
              <.SimpleForm(^.redirect := false)(
                <.TextField(^.source := "id")(),
                <.TextInput(^.source := "name", ^.options := Map("fullWidth" -> true))(),
                <.ReferenceField(
                  ^.source := "questionId",
                  ^.label := "question",
                  ^.reference := Resource.questions,
                  ^.linkType := false,
                  ^.allowEmpty := true
                )(<.TextField(^.source := "slug")()),
              )
            )
          )
        }
      )

}

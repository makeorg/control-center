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
import org.make.backoffice.component.autoComplete.AutoComplete.AutoCompleteProps
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.ShowButton._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.facade.{Choice, DataSourceConfig}
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model._
import org.make.backoffice.service.idea.IdeaService
import org.make.backoffice.service.proposal.{Accepted, ProposalService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
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

  case class DataGridProps(ideaId: Option[String], questionId: Option[String])
  case class DataGridState(proposalsIdeaList: Seq[Proposal] = Seq.empty,
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
          if (self.props.wrapped.questionId.nonEmpty) {
            self.setState(_.copy(shouldUpdate = false))
          }
          if (self.state.shouldUpdate && self.props.wrapped.questionId.isDefined) {
            ProposalService
              .proposals(
                Some(Pagination(page = 1, perPage = 500)), //todo: paginate the custom datagrid
                None,
                Some(
                  Seq(
                    Filter(field = "questionId", value = self.props.wrapped.questionId.getOrElse("")),
                    Filter(field = "status", value = js.Array(Accepted.shortName)),
                    Filter(field = "ideaId", value = self.props.wrapped.ideaId.getOrElse(""))
                  )
                )
              )
              .onComplete {
                case Success(proposals) =>
                  self.setState(_.copy(proposalsIdeaList = proposals.data.toSeq))
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

          def handleNewIdeaRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
            val idea = chosenRequest.asInstanceOf[Idea]
            self.setState(_.copy(selectedIdeaId = Some(IdeaId(idea.id))))
          }

          def handleNewProposalRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
            val proposal = chosenRequest.asInstanceOf[Proposal]
            self.setState(_.copy(selectedProposalToAdd = Some(proposal)))
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
              case Some(proposal) if !self.state.proposalsIdeaList.contains(proposal) =>
                self.props.wrapped.ideaId.foreach { ideaId =>
                  ProposalService
                    .changeProposalsIdea(IdeaId(ideaId), Seq(ProposalId(proposal.id)))
                    .onComplete {
                      case Success(_) =>
                        val newProposalsList = self.state.proposalsIdeaList :+ proposal
                        self.setState(
                          _.copy(
                            proposalsIdeaList = newProposalsList,
                            selectedProposalToAdd = None,
                            snackbarAddOkOpen = true
                          )
                        )
                      case Failure(_) => self.setState(_.copy(snackbarKoOpen = true))
                    }
                }
              case _ =>
            }
          }

          def proposalsSearchRequest: Option[String] => Future[Seq[Proposal]] = { content =>
            ProposalService
              .proposals(
                None,
                None,
                Some(
                  Seq(
                    Filter(field = "questionId", value = self.props.wrapped.questionId.getOrElse("")),
                    Filter(field = "content", value = content.getOrElse("")),
                    Filter(field = "status", value = js.Array(Accepted.shortName))
                  )
                )
              )
              .map(_.data)
          }

          def ideaSearchRequest: Option[String] => Future[Seq[Idea]] = { name =>
            IdeaService
              .listIdeas(
                pagination = None,
                filters = Some(
                  Seq(
                    Filter(field = "questionId", value = self.props.wrapped.questionId.getOrElse("")),
                    Filter(field = "name", value = name.getOrElse(""))
                  )
                )
              )
          }

          <.Card(^.style := Map("marginTop" -> "3em", "padding" -> "0px 1em 3em"))(
            <.CardHeader(^.title := "Add proposals")(),
            <.AutoCompleteComponent(
              ^.wrapped := AutoCompleteProps(
                searchRequest = proposalsSearchRequest,
                handleNewRequest = handleNewProposalRequest,
                dataSourceConfig = DataSourceConfig("content", "id")
              )
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
                <.AutoCompleteComponent(
                  ^.wrapped :=
                    AutoCompleteProps(
                      searchRequest = ideaSearchRequest,
                      handleNewRequest = handleNewIdeaRequest,
                      dataSourceConfig = DataSourceConfig("name", "id")
                    )
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

          val statusChoices =
            Seq(
              Choice(id = IdeaStatus.ideaActivated.shortName, name = IdeaStatus.ideaActivated.shortName),
              Choice(id = IdeaStatus.ideaArchived.shortName, name = IdeaStatus.ideaArchived.shortName)
            )

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
                <.SelectInput(
                  ^.source := "status",
                  ^.choices := statusChoices,
                  ^.allowEmpty := false,
                  ^.options := Map("fullWidth" -> true)
                )(),
                <.ReferenceField(
                  ^.source := "questionId",
                  ^.label := "question",
                  ^.reference := Resource.questions,
                  ^.linkType := false,
                  ^.allowEmpty := true
                )(<.TextField(^.source := "slug")()),
              )
            ),
            <.CustomIdeaDatagrid(
              ^.wrapped := DataGridProps(
                ideaId = self.state.idea.map(_.id),
                questionId = self.state.idea.flatMap(_.questionId.toOption)
              )
            )()
          )
        }
      )

}

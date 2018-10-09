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

package org.make.backoffice.component.tag

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
import org.make.backoffice.facade.AdminOnRest.required
import org.make.backoffice.facade.DataSourceConfig
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model._
import org.make.backoffice.service.proposal.{Accepted, ProposalService}
import org.make.backoffice.service.tag.TagService
import org.make.backoffice.util.Configuration

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.|
import scala.util.{Failure, Success}

object EditTag {

  case class TitleState(tag: Tag)

  lazy val tagTitle: ReactClass = React.createClass[Unit, TitleState](getInitialState = self => {
    val tag = self.props.native.record.asInstanceOf[Tag]
    TitleState(tag)
  }, componentWillUpdate = (self, props, _) => {
    val tag = props.native.record.asInstanceOf[Tag]
    self.setState(_.copy(tag))
  }, render = self => {
    <.h1()(self.state.tag.label)
  })

  case class DataGridProps(tagId: Option[String],
                           operationId: Option[String],
                           themeId: Option[String],
                           country: Option[String],
                           language: Option[String])
  case class DataGridState(tags: Seq[Tag] = Seq.empty,
                           proposalsTagList: Seq[Proposal] = Seq.empty,
                           proposalsSearchList: Seq[Proposal] = Seq.empty,
                           searchTagContent: String = "",
                           searchProposalContent: String = "",
                           selectedTagId: Option[TagId] = None,
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
                      proposalsTagList = proposals.data.toSeq
                        .filter(proposal => proposal.tagIds.contains(self.props.wrapped.tagId.getOrElse(""))),
                      proposalsSearchList = proposals.data.toSeq
                        .filterNot(proposal => proposal.tagIds.contains(self.props.wrapped.tagId.getOrElse("")))
                    )
                  )
                case Failure(e) => js.Dynamic.global.console.log(s"Failed with error $e")
              }
            TagService
              .tags(
                self.props.wrapped.operationId,
                self.props.wrapped.themeId,
                self.props.wrapped.country.getOrElse("FR"),
                self.props.wrapped.language.getOrElse("fr")
              )
              .onComplete {
                case Success(tagsList) =>
                  self.setState(_.copy(tags = tagsList.filterNot(_.id == self.props.wrapped.tagId.getOrElse(""))))
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

          def handleUpdateTagInput: (String, js.Array[js.Object], js.Object) => Unit = (searchText, _, _) => {
            self.setState(_.copy(searchTagContent = searchText))
          }

          def handleUpdateProposalInput: (String, js.Array[js.Object], js.Object) => Unit = (searchText, _, _) => {
            self.setState(_.copy(searchProposalContent = searchText))
          }

          def handleNewTagRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
            val tag = chosenRequest.asInstanceOf[Tag]
            self.setState(_.copy(selectedTagId = Some(TagId(tag.id))))
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

          sealed trait UpdateTag
          case object AddTag extends UpdateTag
          case object ChangeTag extends UpdateTag
          case object RemoveTag extends UpdateTag

          def updateTag(updateMode: UpdateTag, tagId: TagId): Unit = {
            self.state.selectedIds.foreach {
              proposalId =>
                ProposalService
                  .getProposalById(proposalId.value)
                  .flatMap {
                    proposal =>
                      val tags = updateMode match {
                        case AddTag => proposal.data.tagIds.toSeq.map(TagId(_)) :+ tagId
                        case RemoveTag =>
                          proposal.data.tagIds.toSeq
                            .filterNot(_ == self.props.wrapped.tagId.getOrElse(""))
                            .map(TagId(_))
                        case ChangeTag =>
                          (proposal.data.tagIds.toSeq
                            .filterNot(_ == self.props.wrapped.tagId.getOrElse("")) :+ tagId.value).distinct
                            .map(TagId(_))
                      }
                      ProposalService
                        .updateProposal(
                          proposalId.value,
                          newContent = None,
                          tags = tags,
                          labels = proposal.data.labels.toSeq,
                          theme = self.props.wrapped.themeId.map(ThemeId(_)),
                          operationId = self.props.wrapped.operationId.map(OperationId(_)),
                          ideaId = proposal.data.ideaId.toOption.map(IdeaId(_))
                        )
                  }
                  .onComplete {
                    case Success(_) =>
                      val newProposalsList =
                        updateMode match {
                          case RemoveTag | ChangeTag =>
                            self.state.proposalsTagList
                              .filterNot(proposal => self.state.selectedIds.exists(_.value == proposal.id))
                          case AddTag => self.state.proposalsTagList
                        }
                      self.setState(
                        _.copy(
                          proposalsTagList = newProposalsList,
                          selectedIds = Seq.empty,
                          searchTagContent = "",
                          snackbarUpdateOkOpen = true
                        )
                      )
                    case Failure(_) => self.setState(_.copy(snackbarKoOpen = true))
                  }
            }
          }

          def removeTag(): Unit = {
            self.state.selectedIds.foreach {
              proposalId =>
                ProposalService
                  .getProposalById(proposalId.value)
                  .flatMap {
                    proposal =>
                      ProposalService
                        .updateProposal(
                          proposalId.value,
                          newContent = None,
                          tags = proposal.data.tagIds.toSeq
                            .filterNot(_ == self.props.wrapped.tagId.getOrElse(""))
                            .map(TagId(_)),
                          labels = proposal.data.labels.toSeq,
                          theme = self.props.wrapped.themeId.map(ThemeId(_)),
                          operationId = self.props.wrapped.operationId.map(OperationId(_)),
                          ideaId = proposal.data.ideaId.toOption.map(IdeaId(_))
                        )
                  }
                  .onComplete {
                    case Success(_) =>
                      val newProposalsList =
                        self.state.proposalsTagList
                          .filterNot(proposal => self.state.selectedIds.exists(_.value == proposal.id))
                      self.setState(
                        _.copy(
                          proposalsTagList = newProposalsList,
                          selectedIds = Seq.empty,
                          searchTagContent = "",
                          snackbarUpdateOkOpen = true
                        )
                      )
                    case Failure(_) => self.setState(_.copy(snackbarKoOpen = true))
                  }
            }
          }

          def onClickUpdateTag(updateMode: UpdateTag): SyntheticEvent => Unit = { event =>
            event.preventDefault()
            self.state.selectedTagId match {
              case Some(tagId) => updateTag(updateMode, tagId)
              case None =>
                updateMode match {
                  case RemoveTag => removeTag()
                  case _         =>
                }
            }
          }

          def onClickAddProposal: SyntheticEvent => Unit = event => {
            event.preventDefault()
            self.state.selectedProposalToAdd match {
              case Some(proposal) =>
                self.props.wrapped.tagId.foreach {
                  tagId =>
                    ProposalService
                      .updateProposal(
                        proposal.id,
                        newContent = None,
                        tags = (proposal.tagIds.toSeq :+ tagId).map(TagId(_)),
                        labels = proposal.labels.toSeq,
                        theme = self.props.wrapped.themeId.map(ThemeId(_)),
                        operationId = self.props.wrapped.operationId.map(OperationId(_)),
                        ideaId = proposal.ideaId.toOption.map(IdeaId(_))
                      )
                      .onComplete {
                        case Success(_) =>
                          val newProposalsList = self.state.proposalsTagList :+ proposal
                          val newSearchList = self.state.proposalsSearchList.filterNot(_.id == proposal.id)
                          self.setState(
                            _.copy(
                              proposalsTagList = newProposalsList,
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
            if (self.state.proposalsTagList.nonEmpty) {
              <.div()(
                <.CardTitle(
                  ^.title := "Proposals list",
                  ^.subtitle := s"${self.state.proposalsTagList.length} proposals"
                )(),
                <.Table(
                  ^.multiSelectable := true,
                  ^.onRowSelection := onRowSelection(self.state.proposalsTagList.map(_.id)),
                  ^.style := Map("tableLayout" -> "auto"),
                  ^.fixedHeader := false
                )(
                  <.TableHeader()(<.TableRow()(<.TableHeaderColumn()("Content"), <.TableHeaderColumn()())),
                  <.TableBody(^.deselectOnClickaway := false)(self.state.proposalsTagList.map { proposal =>
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
                <.FlatButton(
                  ^.fullWidth := true,
                  ^.label := "Remove tag",
                  ^.secondary := true,
                  ^.onClick := onClickUpdateTag(RemoveTag)
                )(),
                <.br()(),
                <.AutoComplete(
                  ^.id := "search-proposal-tag",
                  ^.hintText := "Search tag",
                  ^.dataSource := self.state.tags,
                  ^.dataSourceConfig := DataSourceConfig("label", "id"),
                  ^.searchText := self.state.searchTagContent,
                  ^.onUpdateInput := handleUpdateTagInput,
                  ^.onNewRequest := handleNewTagRequest,
                  ^.fullWidth := true,
                  ^.popoverProps := Map("canAutoPosition" -> true),
                  ^.openOnFocus := true,
                  ^.filterAutoComplete := filterAutoComplete,
                  ^.menuProps := Map("maxHeight" -> 400)
                )(),
                <.div(
                  ^.style := Map(
                    "display" -> "flex",
                    "align-items" -> "flex-start",
                    "justify-content" -> "space-around"
                  )
                )(
                  <.FlatButton(
                    ^.style := Map("min-width" -> "50%"),
                    ^.fullWidth := true,
                    ^.label := "Replace tag",
                    ^.secondary := true,
                    ^.onClick := onClickUpdateTag(ChangeTag)
                  )(),
                  <.FlatButton(
                    ^.style := Map("min-width" -> "50%"),
                    ^.fullWidth := true,
                    ^.label := "Add tag",
                    ^.secondary := true,
                    ^.onClick := onClickUpdateTag(AddTag)
                  )()
                ),
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

  case class EditTagProps() extends RouterProps
  case class EditTagState(tag: Option[Tag])

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[EditTagProps, EditTagState](
        displayName = "EditTag",
        getInitialState = _ => EditTagState(None),
        componentDidMount = self => {
          TagService.getTag(self.props.`match`.params.getOrElse("id", "")).onComplete {
            case Success(tag) => self.setState(_.copy(Some(tag)))
            case Failure(e)   => js.Dynamic.global.console.log(s"Failed with error: $e")
          }
        },
        render = self => {
          <.div()(
            <.Edit(
              ^.resource := Resource.tags,
              ^.location := self.props.location,
              ^.`match` := self.props.`match`,
              ^.editTitle := <.TagTitle()(),
              ^.hasList := true
            )(
              <.SimpleForm()(
                <.TextInput(
                  ^.source := "label",
                  ^.validate := required,
                  ^.allowEmpty := false,
                  ^.options := Map("fullWidth" -> true)
                )(),
                <.SelectInput(
                  ^.source := "country",
                  ^.choices := Configuration.choicesCountryFilter,
                  ^.allowEmpty := false,
                  ^.validate := required
                )(),
                Configuration.choiceLanguageFilter.map {
                  case (country, languages) =>
                    <.DependentInput(^.dependsOn := "country", ^.dependsValue := country)(
                      <.SelectInput(
                        ^.source := "language",
                        ^.choices := languages,
                        ^.allowEmpty := false,
                        ^.validate := required
                      )()
                    )
                },
                <.ReferenceInput(
                  ^.label := "Tag Type",
                  ^.source := "tagTypeId",
                  ^.reference := Resource.tagType,
                  ^.allowEmpty := false
                )(<.SelectInput(^.optionText := "label")()),
                <.SelectInput(
                  ^.label := "Theme",
                  ^.source := "themeId",
                  ^.allowEmpty := true,
                  ^.choices := Configuration.choicesThemeFilter
                )(),
                <.ReferenceInput(
                  ^.label := "Operation",
                  ^.source := "operationId",
                  ^.reference := Resource.operations,
                  ^.allowEmpty := true
                )(<.SelectInput(^.optionText := "slug")()),
                <.NumberInput(^.source := "weight", ^.validate := required, ^.allowEmpty := false)()
              )
            ),
            <.CustomTagDatagrid(
              ^.wrapped := DataGridProps(
                tagId = self.state.tag.map(_.id),
                operationId = self.state.tag.flatMap(_.operationId.toOption),
                themeId = self.state.tag.flatMap(_.themeId.toOption),
                country = self.state.tag.map(_.country),
                language = self.state.tag.map(_.language)
              )
            )()
          )
        }
      )
}

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

package org.make.backoffice.component.proposal.validated

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.{redux, React}
import org.make.backoffice.client.Resource
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.proposal.validated.ValidatedProposalList.ExportComponent.ExportProps
import org.make.backoffice.facade.AdminOnRest.Button._
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Filter._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.List._
import org.make.backoffice.facade.AdminOnRest.ShowButton._
import org.make.backoffice.facade.Choice
import org.make.backoffice.facade.Configuration.apiUrl
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.facade.React._
import org.make.backoffice.model.{AppState, Proposal}
import org.make.backoffice.service.proposal.Accepted
import org.make.backoffice.service.tag.TagService
import org.make.backoffice.util.Configuration
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

@js.native
trait FilterValues extends js.Object {
  def theme: js.UndefOr[String]
  def tags: js.UndefOr[js.Array[String]]
  def content: js.UndefOr[String]
  def operationId: js.UndefOr[String]
  def source: js.UndefOr[String]
  def question: js.UndefOr[String]
  def country: js.UndefOr[String]
}

object ValidatedProposalList {

  case class ValidatedProposalListProps(filters: Map[String, String]) extends RouterProps

  lazy val ProposalListContainer: ReactClass =
    redux.ReactRedux.connectAdvanced(selectorFactory)(reactClass)

  def selectorFactory: Dispatch => (AppState, Props[Unit]) => ValidatedProposalListProps =
    (_: Dispatch) =>
      (state: AppState, _: Props[Unit]) => {
        ValidatedProposalListProps(filters = state.admin.resources.proposals.list.params.filter.toMap)
    }

  case class ValidatedProposalListState(tagChoices: Seq[Choice])

  private lazy val reactClass: ReactClass =
    React
      .createClass[ValidatedProposalListProps, ValidatedProposalListState](
        displayName = "ValidatedProposalList",
        getInitialState = { _ =>
          ValidatedProposalListState(Seq.empty)
        },
        componentDidMount = { self =>
          val operationIdFilter: Option[String] = self.props.wrapped.filters.get("operationId")
          val themeIdFilter: Option[String] = self.props.wrapped.filters.get("themeId")
          val countryFilter: Option[String] = self.props.wrapped.filters.get("country")
          val languageFilter: Option[String] = self.props.wrapped.filters.get("language")
          TagService.tags(operationIdFilter, themeIdFilter, countryFilter, languageFilter).onComplete {
            case Success(tags) => self.setState(_.copy(tags.map(tag => Choice(tag.id, tag.label))))
            case Failure(_)    => self.setState(_.copy(Seq.empty))
          }
        },
        componentWillReceiveProps = { (self, props) =>
          if (self.props.wrapped.filters != props.wrapped.filters) {
            val operationIdFilter: Option[String] = props.wrapped.filters.get("operationId")
            val themeIdFilter: Option[String] = props.wrapped.filters.get("themeId")
            val countryFilter: Option[String] = props.wrapped.filters.get("country")
            val languageFilter: Option[String] = props.wrapped.filters.get("language")
            TagService.tags(operationIdFilter, themeIdFilter, countryFilter, languageFilter).onComplete {
              case Success(tags) => self.setState(_.copy(tags.map(tag => Choice(tag.id, tag.label))))
              case Failure(_)    => self.setState(_.copy(Seq.empty))
            }
          }
        },
        render = { self =>
          <.List(
            ^.title := "Validated proposals",
            ^.location := self.props.location,
            ^.resource := Resource.proposals,
            ^.hasCreate := false,
            ^.filters := filterList(self.state.tagChoices),
            ^.filter := Map("status" -> Seq(Accepted.shortName)),
            ^.actions := <.ActionComponent()(),
            ^.sort := Map("field" -> "createdAt", "order" -> "DESC")
          )(
            <.Datagrid()(
              <.ShowButton()(),
              <.TextField(^.source := "content")(),
              <.FunctionField(^.label := "theme", ^.render := { record =>
                val proposal = record.asInstanceOf[Proposal]
                proposal.themeId.map { id =>
                  Configuration.getThemeFromThemeId(id)
                }
              })(),
              <.ReferenceField(
                ^.source := "operationId",
                ^.label := "operation",
                ^.reference := Resource.operations,
                ^.linkType := false,
                ^.allowEmpty := true,
                ^.sortable := false
              )(<.TextField(^.source := "slug")()),
              <.TextField(^.source := "context.source", ^.label := "source", ^.sortable := false)(),
              <.RichTextField(^.source := "context.question", ^.label := "question", ^.sortable := false)(),
              <.DateField(^.source := "createdAt", ^.label := "Date", ^.showTime := true)(),
              <.TextField(^.source := "status", ^.sortable := false)(),
              <.ReferenceArrayField(^.label := "Tags", ^.reference := Resource.tags, ^.source := "tagIds")(
                <.SingleFieldList()(<.ChipField(^.source := "label")())
              ),
              <.FunctionField(^.label := "labels", ^.sortable := false, ^.render := { record =>
                val proposal = record.asInstanceOf[Proposal]
                proposal.labels.mkString(", ")
              })(),
              <.FunctionField(^.label := "Votes", ^.sortable := false, ^.render := { record =>
                Proposal.totalVotes(record.asInstanceOf[Proposal].votes)
              })(),
              <.FunctionField(^.label := "Agreement rate", ^.sortable := false, ^.render := { record =>
                s"${Proposal.voteRate(record.asInstanceOf[Proposal].votes, "agree")}%"
              })(),
              <.FunctionField(^.label := "Emergence rate", ^.sortable := false, ^.render := { record =>
                Proposal.totalVotes(record.asInstanceOf[Proposal].votes)
              })()
            )
          )
        }
      )


  object ExportComponent {

    case class ValidatedProposalListExportState(fileName: String, open: Boolean = false)
    case class ExportProps(filters: FilterValues)

    def buildParams(filters: FilterValues, fileName: String): String = {
      var filterParams: String = s"?format=csv&filename=${fileName.split(".csv").head}"
      filterParams = filters.theme.map(theme     => filterParams + s"&theme=$theme").getOrElse(filterParams)
      filterParams = filters.tags.map(tags       => filterParams + s"&tags=${tags.mkString(",")}").getOrElse(filterParams)
      filterParams = filters.content.map(content => filterParams + s"&content=$content").getOrElse(filterParams)
      filterParams =
        filters.operationId.map(operationId        => filterParams + s"&operation=$operationId").getOrElse(filterParams)
      filterParams = filters.source.map(source     => filterParams + s"&source=$source").getOrElse(filterParams)
      filterParams = filters.question.map(question => filterParams + s"&question=$question").getOrElse(filterParams)
      filterParams = filters.country.map(country   => filterParams + s"&country=$country").getOrElse(filterParams)
      filterParams
    }

    private def export(self: React.Self[ExportProps, ValidatedProposalListExportState]): SyntheticEvent => Unit = {
      event =>
        event.preventDefault()
        val params: String = buildParams(self.props.wrapped.filters, self.state.fileName)
        self.setState(_.copy(open = false))
        org.scalajs.dom.window.open(s"$apiUrl/moderation/proposals/export$params")
    }

    private def handleOpen(self: React.Self[ExportProps, ValidatedProposalListExportState]): SyntheticEvent => Unit = {
      event =>
        event.preventDefault()
        self.setState(_.copy(open = true))
    }

    private def handleClose(self: React.Self[ExportProps, ValidatedProposalListExportState]): SyntheticEvent => Unit = {
      event =>
        event.preventDefault()
        self.setState(_.copy(open = false))
    }

    private def actionsModal(self: React.Self[ExportProps, ValidatedProposalListExportState]): Seq[ReactElement] = {
      Seq(
        <.FlatButton(^.label := "Cancel", ^.secondary := true, ^.onClick := handleClose(self))(),
        <.FlatButton(^.label := "Export", ^.primary := true, ^.onClick := export(self))()
      )
    }

    lazy val reactClass: ReactClass = React.createClass[ExportProps, ValidatedProposalListExportState](
      displayName = "ValidatedProposalListExport",
      getInitialState = _ => ValidatedProposalListExportState(fileName = "proposals.csv"),
      render = { self =>
        def handleFileNameEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
          val newContent: String = event.target.value
          self.setState(_.copy(fileName = newContent))
        }

        <.div(^.style := Map("display" -> "inline-block"))(
          <.FlatButton(^.label := "Export", ^.primary := true, ^.onClick := handleOpen(self))(),
          <.Dialog(
            ^.title := "Choose your filename",
            ^.open := self.state.open,
            ^.modal := true,
            ^.actionsModal := actionsModal(self)
          )(
            <.TextFieldMaterialUi(
              ^.value := self.state.fileName,
              ^.onChange := handleFileNameEdition,
              ^.floatingLabelText := "Filename"
            )()
          )
        )
      }
    )

  }

  object Action {

    lazy val reactClass: ReactClass =
      React
        .createClass[React.Props[Unit], Unit](
        displayName = "ValidatedProposalListAction",
        render = { self =>
          <.CardActions()(
            <.ExportComponent(^.wrapped := ExportProps(self.props.native.filterValues.asInstanceOf[FilterValues]))(),
            cloneElement(
              self.props.native.filters.asInstanceOf[ReactClass],
              js.Dynamic.literal(
                "showFilter" -> self.props.native.showFilter,
                "displayedFilters" -> self.props.native.displayedFilters,
                "filterValues" -> self.props.native.filterValues,
                "context" -> "button"
              )
            ),
            <.RefreshButton()()
          )
        }
      )
  }

  def filterAutoComplete: (String, String) => Boolean = (searchText, key) => {
    key.indexOf(searchText) != -1
  }

  def filterList(tagChoices: Seq[Choice]): ReactElement = {
    <.Filter(^.resource := Resource.proposals)(
      <.TextInput(^.label := "Search", ^.source := "content", ^.alwaysOn := true)(),
      <.SelectInput(
        ^.label := "Theme",
        ^.source := "themeId",
        ^.alwaysOn := false,
        ^.choices := Configuration.choicesThemeFilter
      )(),
      <.SelectInput(
        ^.label := "Country",
        ^.source := "country",
        ^.alwaysOn := false,
        ^.choices := Configuration.choicesCountryFilter
      )(),
      <.TextInput(^.label := "Source", ^.source := "source", ^.alwaysOn := false)(),
      <.ReferenceInput(
        ^.label := "Operation",
        ^.source := "operationId",
        ^.reference := Resource.operations,
        ^.alwaysOn := true
      )(<.SelectInput(^.optionText := "slug")()),
      <.TextInput(^.label := "Question", ^.source := "question", ^.alwaysOn := false)(),
      <.SelectInput(
        ^.label := "Tags",
        ^.source := "tagsIds",
        ^.choices := tagChoices,
        ^.alwaysOn := true,
        ^.allowEmpty := true
      )()
    )
  }
}

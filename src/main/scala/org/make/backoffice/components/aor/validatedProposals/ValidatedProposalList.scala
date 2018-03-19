package org.make.backoffice.components.aor.validatedProposals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.components.aor.validatedProposals.ValidatedProposalList.ExportComponent.ExportProps
import org.make.backoffice.facades.AdminOnRest.Button._
import org.make.backoffice.facades.AdminOnRest.Datagrid._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Filter._
import org.make.backoffice.facades.AdminOnRest.Inputs._
import org.make.backoffice.facades.AdminOnRest.List._
import org.make.backoffice.facades.AdminOnRest.ShowButton._
import org.make.backoffice.facades.Configuration.apiUrl
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.facades.React._
import org.make.backoffice.helpers.Configuration
import org.make.backoffice.models.Proposal
import org.make.client.Resource
import org.make.services.proposal.Accepted
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenMap

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

  case class ListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  object ExportComponent {

    case class ExportState(fileName: String, open: Boolean = false)
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

    private def export(self: React.Self[ExportProps, ExportState]): (SyntheticEvent) => Unit = { event =>
      event.preventDefault()
      val params: String = buildParams(self.props.wrapped.filters, self.state.fileName)
      self.setState(_.copy(open = false))
      org.scalajs.dom.window.open(s"$apiUrl/moderation/proposals/export$params")
    }

    private def handleOpen(self: React.Self[ExportProps, ExportState]): (SyntheticEvent) => Unit = { event =>
      event.preventDefault()
      self.setState(_.copy(open = true))
    }

    private def handleClose(self: React.Self[ExportProps, ExportState]): (SyntheticEvent) => Unit = { event =>
      event.preventDefault()
      self.setState(_.copy(open = false))
    }

    private def actionsModal(self: React.Self[ExportProps, ExportState]): Seq[ReactElement] = {
      Seq(
        <.FlatButton(^.label := "Cancel", ^.secondary := true, ^.onClick := handleClose(self))(),
        <.FlatButton(^.label := "Export", ^.primary := true, ^.onClick := export(self))()
      )
    }

    lazy val reactClass: ReactClass = React.createClass[ExportProps, ExportState](
      getInitialState = (_) => ExportState(fileName = "proposals.csv"),
      render = { self =>
        def handleFileNameEdition: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
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
      React.createClass[React.Props[Unit], Unit](render = { self =>
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
      })
  }

  private lazy val reactClass: ReactClass =
    React
      .createClass[ListProps, Unit](
        displayName = "ValidatedProposalList",
        render = { (self) =>
          <.List(
            ^.title := "Validated proposals",
            ^.location := self.props.location,
            ^.resource := Resource.proposals,
            ^.hasCreate := false,
            ^.filters := filterList(),
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

  def filterList(): ReactElement = {
    <.Filter(^.resource := Resource.proposals)(
        //TODO: add the possibility to search by userId or proposalId
        <.TextInput(^.label := "Search", ^.source := "content", ^.alwaysOn := true)(),
        <.SelectInput(
          ^.label := "Theme",
          ^.source := "theme",
          ^.alwaysOn := false,
          ^.choices := Configuration.choicesThemeFilter
        )(),
        <.SelectInput(
          ^.label := "Country",
          ^.source := "country",
          ^.alwaysOn := true,
          ^.choices := Configuration.choicesCountryFilter
        )(),
        <.TextInput(^.label := "Source", ^.source := "source", ^.alwaysOn := false)(),
        <.ReferenceInput(^.label := "Operation", ^.source := "operationId", ^.reference := Resource.operations)(
          <.SelectInput(^.optionText := "slug", ^.alwaysOn := false)()
        ),
        <.TextInput(^.label := "Question", ^.source := "question", ^.alwaysOn := false)(),
        <.ReferenceArrayInput(^.label := "Tags", ^.source := "tagsIds",
          ^.filterToQuery := {(searchText) => Map("label" -> searchText).toJSDictionary},
          ^.reference := Resource.tags)(
          <.SelectArrayInput(^.optionText := "label", ^.alwaysOn := false)()
        )
        //TODO: add filter on: "moderator"
    )
  }
}

package org.make.backoffice.component.proposal.moderation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Filter._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.List._
import org.make.backoffice.facade.AdminOnRest.ShowButton._
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.facade.Choice
import org.make.backoffice.util.Configuration
import org.make.backoffice.model.Proposal
import org.make.backoffice.client.Resource
import org.make.backoffice.service.proposal._

import scala.scalajs.js

object ProposalList {

  case class ProposalListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass = React.createClass[ProposalListProps, Unit](
    displayName = "ProposalList",
    render = (self) =>
      <.div()(
        <.StartModeration.empty,
        <.List(
          ^.title := "Proposals",
          ^.location := self.props.location,
          ^.resource := Resource.proposals,
          ^.hasCreate := false,
          ^.filters := filterList(),
          ^.sort := Map("field" -> "createdAt", "order" -> "DESC")
        )(
          <.Datagrid(^.rowStyle := rowStyle)(
            <.ShowButton()(),
            <.TextField(^.source := "content", ^.sortable := false)(),
            <.TextField(^.source := "status", ^.sortable := false)(),
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
            <.DateField(^.source := "createdAt", ^.label := "Date", ^.showTime := true)()
          )
        )
    )
  )

  def rowStyle: RowStyle = { (record, _) =>
    val proposal = record.asInstanceOf[Proposal]

    proposal.status match {
      case Postponed.shortName => js.Dictionary("backgroundColor" -> "#fda")
      case Refused.shortName   => js.Dictionary("backgroundColor" -> "#fdd")
      case _                   => js.Dictionary.empty
    }
  }

  def filterList(): ReactElement = {
    val statusChoices = Seq(
      Choice(Pending.shortName, "Pending"),
      Choice(Postponed.shortName, "Postponed"),
      Choice(Refused.shortName, "Refused"),
      Choice(Archived.shortName, "Archived")
    )

    <.Filter(^.resource := Resource.proposals)(
      Seq(
        //TODO: add the possibility to search by userId or proposalId
        <.TextInput(^.label := "Search", ^.source := "content", ^.alwaysOn := true)(),
        <.SelectArrayInput(^.label := "Status", ^.source := "status", ^.alwaysOn := true, ^.choices := statusChoices)(),
        <.SelectInput(
          ^.label := "Theme",
          ^.source := "themeId",
          ^.alwaysOn := false,
          ^.choices := Configuration.choicesThemeFilter
        )(),
        <.SelectInput(
          ^.label := "Country",
          ^.source := "country",
          ^.alwaysOn := true,
          ^.allowEmpty := true,
          ^.choices := Configuration.choicesCountryFilter
        )(),
        <.TextInput(^.label := "Source", ^.source := "source", ^.alwaysOn := false)(),
        <.ReferenceInput(^.label := "Operation", ^.source := "operationId", ^.reference := Resource.operations)(
          <.SelectInput(^.optionText := "slug", ^.alwaysOn := false)()
        )
        //TODO: add filter on: "reason for refusal" and "moderator"
      )
    )
  }
}

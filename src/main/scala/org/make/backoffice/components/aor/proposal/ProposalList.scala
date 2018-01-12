package org.make.backoffice.components.aor.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.AdminOnRest.Datagrid._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Filter._
import org.make.backoffice.facades.AdminOnRest.Inputs._
import org.make.backoffice.facades.AdminOnRest.List._
import org.make.backoffice.facades.AdminOnRest.ShowButton._
import org.make.backoffice.facades.Choice
import org.make.backoffice.helpers.Configuration
import org.make.backoffice.models.Proposal
import org.make.client.Resource
import org.make.services.proposal.{Archived, Pending, Postponed, Refused}

import scala.scalajs.js

object ProposalList {

  case class ListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass = React.createClass[ListProps, Unit](
    render = (self) =>
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
          <.TextField(^.source := "context.operation", ^.label := "operation", ^.sortable := false)(),
          <.TextField(^.source := "context.source", ^.label := "source", ^.sortable := false)(),
          <.TextField(^.source := "context.question", ^.label := "question", ^.sortable := false)(),
          <.DateField(^.source := "createdAt", ^.label := "Date", ^.showTime := true)()
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
    val choices = Seq(
      Choice(Pending.shortName, "Pending"),
      Choice(Postponed.shortName, "Postponed"),
      Choice(Refused.shortName, "Refused"),
      Choice(Archived.shortName, "Archived")
    )
    <.Filter(^.resource := Resource.proposals)(
      Seq(
        //TODO: add the possibility to search by userId or proposalId
        <.TextInput(^.label := "Search", ^.source := "content", ^.alwaysOn := true)(),
        <.SelectArrayInput(^.label := "Status", ^.source := "status", ^.alwaysOn := true, ^.choices := choices)(),
        <.SelectInput(
          ^.label := "Theme",
          ^.source := "theme",
          ^.alwaysOn := false,
          ^.choices := Configuration.choicesThemeFilter
        )(),
        <.TextInput(^.label := "Source", ^.source := "source", ^.alwaysOn := false)(),
        <.TextInput(^.label := "Operation", ^.source := "operation", ^.alwaysOn := false)()
        //TODO: add filter on: "reason for refusal" and "moderator"
      )
    )
  }
}

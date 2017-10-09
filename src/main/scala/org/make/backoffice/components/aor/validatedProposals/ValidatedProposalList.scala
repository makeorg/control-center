package org.make.backoffice.components.aor.validatedProposals

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
import org.make.backoffice.helpers.Configuration
import org.make.backoffice.models.{Proposal, ThemeId}
import org.make.client.Resource
import org.make.services.proposal.Accepted

object ValidatedProposalList {

  case class ListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass = React.createClass[ListProps, Unit](
    render = (self) =>
      <.List(
        ^.title := "Validated proposals",
        ^.location := self.props.location,
        ^.resource := Resource.validatedProposals,
        ^.hasCreate := false,
        ^.filters := filterList(),
        ^.filter := Map("status" -> Accepted.shortName),
        ^.sort := Map("field" -> "createdAt", "order" -> "DESC")
      )(
        <.Datagrid()(
          <.ShowButton()(),
          <.TextField(^.source := "content")(),
          <.FunctionField(^.label := "theme", ^.render := { record =>
            val proposal = record.asInstanceOf[Proposal]
            proposal.themeId.map { id =>
              Configuration.getThemeFromThemeId(ThemeId(id))
            }
          })(),
          <.TextField(^.source := "proposalContext.operation", ^.label := "operation", ^.sortable := false)(),
          <.TextField(^.source := "proposalContext.source", ^.label := "source", ^.sortable := false)(),
          <.TextField(^.source := "proposalContext.question", ^.label := "question", ^.sortable := false)(),
          <.DateField(^.source := "createdAt", ^.label := "Date", ^.showTime := true)(),
          <.TextField(^.source := "status", ^.sortable := false)(),
          <.FunctionField(^.label := "tags", ^.sortable := false, ^.render := { record =>
            val proposal = record.asInstanceOf[Proposal]
            proposal.tags.map(_.tagId.value).mkString(", ")
          })(<.ChipField(^.source := "tags.label")()),
          <.FunctionField(^.label := "labels", ^.sortable := false, ^.render := { record =>
            val proposal = record.asInstanceOf[Proposal]
            proposal.labels.mkString(", ")
          })(<.ChipField(^.source := "labels.labelId.value")()),
          <.FunctionField(^.label := "Votes", ^.sortable := false, ^.render := { record =>
            Proposal.totalVotes(record.asInstanceOf[Proposal])
          })(),
          <.TextField(^.source := "votesAgree.count", ^.label := "Agreement rate", ^.sortable := false)(),
          <.FunctionField(^.label := "Emergence rate", ^.sortable := false, ^.render := { record =>
            Proposal.totalVotes(record.asInstanceOf[Proposal])
          })()
        )
    )
  )

  def filterList(): ReactElement = {
    <.Filter(^.resource := Resource.validatedProposals)(
      Seq(
        //TODO: add the possibility to search by userId or proposalId
        <.TextInput(^.label := "Search", ^.source := "content", ^.alwaysOn := true)(),
        <.SelectInput(
          ^.label := "Theme",
          ^.source := "theme",
          ^.alwaysOn := false,
          ^.choices := Configuration.choicesThemeFilter
        )(),
        <.TextInput(^.label := "Source", ^.source := "source", ^.alwaysOn := false)(),
        <.TextInput(^.label := "Support", ^.source := "support", ^.alwaysOn := false)()
        //TODO: add filter on: "reason for refusal" and "moderator"
      )
    )
  }
}

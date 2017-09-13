package org.make.backoffice.components.validated_proposals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.AdminOnRest.Datagrid._
import org.make.backoffice.facades.AdminOnRest.DeleteButton._
import org.make.backoffice.facades.AdminOnRest.EditButton._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Inputs._
import org.make.backoffice.facades.AdminOnRest.Filter._
import org.make.backoffice.facades.AdminOnRest.List._
import org.make.backoffice.facades.AdminOnRest.ShowButton._
import org.make.backoffice.models.Proposal
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
          <.TextField(^.source := "id")(),
          <.TextField(^.source := "content")(),
          <.TextField(^.source := "themeId", ^.label := "Theme")(),
          <.TextField(^.source := "proposalContext.operation", ^.label := "support", ^("sortable") := false)(),
          <.TextField(^.source := "proposalContext.source", ^.label := "context", ^("sortable") := false)(),
          <.TextField(^.source := "proposalContext.question", ^.label := "question", ^("sortable") := false)(),
          <.DateField(^.source := "createdAt", ^.label := "Date", ^.showTime := true)(),
          <.TextField(^.source := "status")(),
          <.TextField(^.source := "tags")(),
          <.TextField(^.source := "labels")(),
          <.TextField(^.source := "userId", ^.label := "User id")(),
          <.FunctionField(^.label := "Votes", ^.render := { record =>
            Proposal.totalVotes(record.asInstanceOf[Proposal])
          })(),
          <.TextField(^.source := "votesAgree.count", ^.label := "Agreement rate")(),
          <.FunctionField(^.label := "Emergence rate", ^.render := { record =>
            Proposal.totalVotes(record.asInstanceOf[Proposal])
          })(),
          <.EditButton()(),
          <.ShowButton()(),
          <.DeleteButton()()
        )
    )
  )

  def filterList(): ReactElement = {
    <.Filter(^.resource := Resource.validatedProposals)(
      Seq(
        //TODO: add the possibility to search by userId or proposalId
        <.TextInput(^.label := "Search", ^.source := "content", ^.alwaysOn := true)(),
        <.TextInput(^.label := "Theme", ^.source := "theme", ^.alwaysOn := false)(),
        <.TextInput(^.label := "Source", ^.source := "source", ^.alwaysOn := false)(),
        <.TextInput(^.label := "Support", ^.source := "support", ^.alwaysOn := false)()
        //TODO: add filter on: "reason for refusal" and "moderator"
      )
    )
  }
}

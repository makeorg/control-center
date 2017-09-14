package org.make.backoffice.components.aor_proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.AdminOnRest.Datagrid._
import org.make.backoffice.facades.AdminOnRest.DeleteButton._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Filter._
import org.make.backoffice.facades.AdminOnRest.Inputs._
import org.make.backoffice.facades.AdminOnRest.List._
import org.make.backoffice.facades.AdminOnRest.ShowButton._
import org.make.backoffice.facades.Choice
import org.make.client.Resource
import org.make.services.proposal.{Archived, Pending, Refused}

import scala.scalajs.js.JSConverters._

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
        <.Datagrid()(
          <.TextField(^.source := "id")(),
          <.TextField(^.source := "content")(),
          <.TextField(^.source := "status")(),
          <.TextField(^.source := "themeId", ^.label := "Theme")(),
          <.TextField(^.source := "proposalContext.operation", ^.label := "support", ^("sortable") := false)(),
          <.TextField(^.source := "proposalContext.source", ^.label := "context", ^("sortable") := false)(),
          <.TextField(^.source := "proposalContext.question", ^.label := "question", ^("sortable") := false)(),
          <.DateField(^.source := "createdAt", ^.label := "Date", ^.showTime := true)(),
          <.TextField(^.source := "userId", ^.label := "User id")(),
          <.ShowButton()(),
          <.DeleteButton()()
        )
    )
  )

  def filterList(): ReactElement = {
    val choices = Seq(
      Choice(Pending.shortName, "Pending"),
      Choice(Refused.shortName, "Refused"),
      Choice(Archived.shortName, "Archived")
    )
    <.Filter(^.resource := Resource.proposals)(
      Seq(
        //TODO: add the possibility to search by userId or proposalId
        <.TextInput(^.label := "Search", ^.source := "content", ^.alwaysOn := true)(),
        <.SelectInput(^.label := "Status", ^.source := "status", ^.alwaysOn := true, ^.choices := choices.toJSArray)(),
        <.TextInput(^.label := "Theme", ^.source := "theme", ^.alwaysOn := false)(),
        <.TextInput(^.label := "Source", ^.source := "source", ^.alwaysOn := false)(),
        <.TextInput(^.label := "Support", ^.source := "support", ^.alwaysOn := false)()
        //TODO: add filter on: "reason for refusal" and "moderator"
      )
    )
  }
}

package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.Choice
import org.make.backoffice.facades.Datagrid._
import org.make.backoffice.facades.DeleteButton._
import org.make.backoffice.facades.EditButton._
import org.make.backoffice.facades.Field.SelectInput._
import org.make.backoffice.facades.Field.TextInput._
import org.make.backoffice.facades.Field.TextField._
import org.make.backoffice.facades.Field._
import org.make.backoffice.facades.Filter._
import org.make.backoffice.facades.List._
import org.make.backoffice.facades.ShowButton._
import org.make.services.proposal.{Accepted, Archived, Pending, Refused}

import scala.scalajs.js.JSConverters._

object ProposalList {

  case class ListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass = React.createClass[ListProps, Unit](
    render = (self) =>
      <.List(
        ^.title := "Proposals",
        ^.location := self.props.location,
        ^.resource := "proposals",
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
          <.TextField(^.source := "createdAt", ^.label := "Date")(),
          <.TextField(^.source := "userId", ^.label := "User id")(),
          <.EditButton()(),
          <.ShowButton()(),
          <.DeleteButton()()
        )
    )
  )

  def filterList(): ReactElement = {
    val choices = Seq(
      Choice(Pending.shortName, "Pending"),
      Choice(Accepted.shortName, "Accepted"),
      Choice(Refused.shortName, "Refused"),
      Choice(Archived.shortName, "Archived")
    )
    <.Filter(^.resource := "proposals")(
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

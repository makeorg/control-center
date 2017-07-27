package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.libs.Datagrid._
import org.make.backoffice.libs.List._
import org.make.backoffice.libs.Filter._
import org.make.backoffice.libs.Field._
import org.make.backoffice.libs.Field.TextField._
import org.make.backoffice.libs.Field.EmailField._
import org.make.backoffice.libs.Field.TextInput._
import org.make.backoffice.libs.EditButton._
import org.make.backoffice.libs.ShowButton._

object ProposalList {

  case class ListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass = React.createClass[ListProps, Unit](
    render = (self) =>
      <.List(
        ^.title := "Propositions",
        ^.location := self.props.location,
        ^.resource := "propositions",
        ^.hasCreate := true,
        ^.filters := filterList()
      )(
        <.Datagrid()(
          <.TextField(^.source := "id")(),
          <.TextField(^.source := "content")(),
          <.TextField(^.source := "author")(),
          <.EmailField(^.source := "email")(),
          <.TextField(^.source := "status")(),
          <.EditButton()(),
          <.ShowButton()()
        )
      )
  )

  def filterList(): ReactElement = {
    <.Filter(^.resource := "propositions")(
      <.TextInput(^.label := "Search", ^.source := "q", ^.alwaysOn := true)()
    )
  }
}

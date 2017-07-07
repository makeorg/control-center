package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.libs.Datagrid._
import org.make.backoffice.libs.List._
import org.make.backoffice.libs.Field._
import org.make.backoffice.libs.Field.TextField._

object ProposalList {

  case class ListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ListProps, Unit](
    render = (self) =>
      <.List(
        ^.title := "Propositions",
        ^.location := self.props.location,
        ^.resource := "propositions",
        ^.hasCreate := false
      )(
        <.Datagrid()(
          <.TextField(^.source := "id")(),
          <.TextField(^.source := "content")(),
          <.TextField(^.source := "author")(),
          <.TextField(^.source := "status")()
        )
      )
  )
}

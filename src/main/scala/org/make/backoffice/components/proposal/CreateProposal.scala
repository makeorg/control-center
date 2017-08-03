package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.libs.Create._
import org.make.backoffice.libs.Field.TextInput._
import org.make.backoffice.libs.Field._
import org.make.backoffice.libs.SimpleForm._

object CreateProposal {

  case class CreateProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[CreateProps, Unit](
    render = (self) =>
      <.Create(
        ^.resource := "propositions",
        ^.location := self.props.location
      )(
        <.SimpleForm()(
          <.TextInput(^.source := "content")(),
          <.TextInput(^.source := "author")(),
          <.TextInput(^.source := "status")(),
          <.TextInput(^.source := "email")()
        )
      )

    )
}

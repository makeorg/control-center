package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.AdminOnRest.Edit._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Inputs._
import org.make.backoffice.facades.AdminOnRest.SimpleForm._
import org.make.backoffice.facades.{Match, Params}
import org.make.client.Resource

object EditProposal {

  case class EditProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[EditProps, Unit](
    render = (self) =>
      <.Edit(
        ^.resource := Resource.proposals,
        ^.location := self.props.location,
        ^.`match` := Match(params = Params(id = self.props.location.pathname.split('/')(2)))
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

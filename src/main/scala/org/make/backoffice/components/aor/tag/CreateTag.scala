package org.make.backoffice.components.aor.tag

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.AdminOnRest.Create._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Inputs._
import org.make.backoffice.facades.AdminOnRest.SimpleForm._
import org.make.client.Resource

object CreateTag {
  case class CreateProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[CreateProps, Unit](displayName = "CreateTag", render = (self) => {

      <.Create(^.resource := Resource.tags, ^.location := self.props.location)(
        <.SimpleForm()(
          <.TextInput(^.source := "label", ^.allowEmpty := false)()
        )
      )
    })
}

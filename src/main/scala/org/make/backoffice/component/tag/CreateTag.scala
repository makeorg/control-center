package org.make.backoffice.component.tag

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facade.AdminOnRest.Create._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.client.Resource

object CreateTag {
  case class CreateTagProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[CreateTagProps, Unit](
        displayName = "CreateTag",
        render = (self) => {

          <.Create(^.resource := Resource.tags, ^.location := self.props.location)(
            <.SimpleForm()(<.TextInput(^.source := "label", ^.allowEmpty := false)())
          )
        }
      )
}

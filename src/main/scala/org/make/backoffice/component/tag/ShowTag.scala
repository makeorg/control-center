package org.make.backoffice.component.tag

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Show._
import org.make.backoffice.facade.AdminOnRest.SimpleShowLayout._
import org.make.backoffice.client.Resource

object ShowTag {
  case class ShowTagProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ShowTagProps, Unit](
    displayName = "ShowTag",
    render = self =>
      <.Show(
        ^.location := self.props.location,
        ^.resource := Resource.tags,
        ^.`match` := self.props.`match`,
        ^.hasEdit := true
      )(<.SimpleShowLayout()(<.TextField(^.source := "label")()))
  )
}

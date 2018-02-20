package org.make.backoffice.components.aor.tag

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Show._
import org.make.backoffice.facades.AdminOnRest.SimpleShowLayout._
import org.make.client.Resource

object ShowTag {
  case class ShowProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ShowProps, Unit](
    displayName = "ShowTag",
    render = (self) =>
      <.Show(
        ^.location := self.props.location,
        ^.resource := Resource.tags,
        ^.`match` := self.props.`match`
      )(
        <.SimpleShowLayout()(
          <.TextField(^.source := "label")()
      )
    )
  )
}

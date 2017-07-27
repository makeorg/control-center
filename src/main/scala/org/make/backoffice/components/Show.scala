package org.make.backoffice.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.libs.Show._
import org.make.backoffice.libs.SimpleShowLayout._
import org.make.backoffice.libs.Field._
import org.make.backoffice.libs.Field.TextField._
import org.make.backoffice.libs.Field.EmailField._

object Show {

  case class ShowProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ShowProps, Unit](
    render = (self) =>
      <.Show(
        ^.location := self.props.location,
        ^.resource := "propositions"
      )(
        <.SimpleShowLayout()(
          <.TextField(^.source := "id")(),
          <.TextField(^.source := "content")(),
          <.TextField(^.source := "author")(),
          <.EmailField(^.source := "email")(),
          <.TextField(^.source := "status")()
        )
      )
  )

}

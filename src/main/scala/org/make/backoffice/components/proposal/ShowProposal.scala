package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.libs.Field.EmailField._
import org.make.backoffice.libs.Field.TextField._
import org.make.backoffice.libs.Field._
import org.make.backoffice.libs.Show._
import org.make.backoffice.libs.SimpleShowLayout._
import org.make.backoffice.libs.{Match, Params}

object ShowProposal {

  case class ShowProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ShowProps, Unit](
    render = (self) =>
      <.Show(
        ^.location := self.props.location,
        ^.resource := "propositions",
        ^.`match` := Match(
          params = Params(id = self.props.location.pathname.split('/')(2))
        )
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

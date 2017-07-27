package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.libs.Delete._
import org.make.backoffice.libs.{Match, Params}

object DeleteProposal {

  case class DeleteProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[DeleteProps, Unit](
    render = (self) =>
      <.Delete(
        ^.location := self.props.location,
        ^.resource := "propositions",
        ^.history := self.props.history,
        ^.`match` := Match(
          params = Params(id = self.props.location.pathname.split('/')(2))
        )
      )()
  )
}

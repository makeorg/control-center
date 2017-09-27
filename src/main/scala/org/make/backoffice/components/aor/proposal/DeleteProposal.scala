package org.make.backoffice.components.aor.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facades.AdminOnRest.Delete._
import org.make.backoffice.facades.{Match, Params}
import org.make.client.Resource

object DeleteProposal {

  case class DeleteProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[DeleteProps, Unit](
    render = (self) =>
      <.Delete(
        ^.location := self.props.location,
        ^.resource := Resource.proposals,
        ^.history := self.props.history,
        ^.`match` := Match(params = Params(id = self.props.location.pathname.split('/')(2)))
      )()
  )
}

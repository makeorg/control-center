package org.make.backoffice.components.aor_proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Show._
import org.make.backoffice.facades.AdminOnRest.SimpleShowLayout._
import org.make.backoffice.facades.{Match, Params}
import org.make.client.Resource

object ShowProposal {

  case class ShowProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ShowProps, Unit](
    render = (self) =>
      <.Show(
        ^.location := self.props.location,
        ^.resource := Resource.proposals,
        ^.`match` := Match(params = Params(id = self.props.location.pathname.split('/')(2)))
      )(
        <.SimpleShowLayout()(
          <.TextField(^.source := "id")(),
          <.TextField(^.source := "content")(),
          <.TextField(^.source := "status")(),
          <.TextField(^.source := "theme", ^.label := "Theme")(),
          <.TextField(^.source := "creationContext.operation", ^.label := "support")(),
          <.TextField(^.source := "creationContext.source", ^.label := "context")(),
          <.TextField(^.source := "creationContext.question", ^.label := "question")(),
          <.TextField(^.source := "creationContext.createdAt", ^.label := "date")(),
          <.TextField(^.source := "author.userId.value", ^.label := "User id")(),
          <.ShowProposalComponents()()
        )
    )
  )
}

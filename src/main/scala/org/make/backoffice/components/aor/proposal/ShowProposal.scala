package org.make.backoffice.components.aor.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.components.proposal.ShowProposalComponents.ShowComponentsProps
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.Show._
import org.make.backoffice.facades.AdminOnRest.SimpleShowLayout._
import org.make.backoffice.facades.{Match, Params}
import org.make.backoffice.helpers.Configuration
import org.make.backoffice.models.SingleProposal
import org.make.client.Resource

object ShowProposal {

  case class ShowProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ShowProps, Unit](
    render = (self) =>
      <.Show(
        ^.location := self.props.location,
        ^.resource := Resource.proposals,
        ^.`match` := Match(params = Params(id = self.props.location.pathname.split('/')(2))),
        ^.showTitle := <.ShowProposalTitle()()
      )(
        <.SimpleShowLayout()(
          <.TextField(^.source := "id")(),
          <.TextField(^.source := "content")(),
          <.TextField(^.source := "status")(),
          <.FunctionField(^.label := "theme", ^.render := { record =>
            val proposal = record.asInstanceOf[SingleProposal]
            proposal.theme.map { id =>
              Configuration.getThemeFromThemeId(id)
            }
          })(),
          <.TextField(^.source := "context.operation", ^.label := "operation")(),
          <.TextField(^.source := "context.source", ^.label := "source")(),
          <.TextField(^.source := "context.question", ^.label := "question")(),
          <.DateField(
            ^.source := "createdAt",
            ^.label := "date",
            ^.options := Map("weekday" -> "long", "year" -> "numeric", "month" -> "long", "day" -> "numeric"),
            ^.locales := "en-EN"
          )(),
          <.TextField(^.source := "author.firstName", ^.label := "User name")(),
          <.ShowProposalComponents(^.wrapped := ShowComponentsProps(org.scalajs.dom.window.location.hash))()
        )
    )
  )
}

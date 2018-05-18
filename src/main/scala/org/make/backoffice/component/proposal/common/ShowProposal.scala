package org.make.backoffice.component.proposal.common

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.proposal.common.ShowProposalComponents.{Context, ShowProposalComponentsProps}
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Show._
import org.make.backoffice.facade.AdminOnRest.Tab._
import org.make.backoffice.facade.AdminOnRest.TabbedShowLayout._
import org.make.backoffice.util.Configuration
import org.make.backoffice.model.SingleProposal
import org.make.backoffice.client.Resource

object ShowProposal {

  case class ShowProposalProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ShowProposalProps, Unit](
    displayName = "ShowProposal",
    render = self =>
      <.Show(
        ^.location := self.props.location,
        ^.resource := Resource.proposals,
        ^.`match` := self.props.`match`,
        ^.showTitle := <.ShowProposalTitle()()
      )(
        <.TabbedShowLayout()(
          <.Tab(^.label := "Actions")(
            <.ShowProposalComponents(
              ^.wrapped := ShowProposalComponentsProps(
                hash = org.scalajs.dom.window.location.hash,
                proposal = None,
                context = Context.List
              )
            )()
          ),
          <.Tab(^.label := "Proposal infos", ^.disabled := false)(
            <.TextField(^.source := "id")(),
            <.TextField(^.source := "content")(),
            <.TextField(^.source := "status")(),
            <.TextField(^.source := "language")(),
            <.TextField(^.source := "country")(),
            <.FunctionField(^.label := "theme", ^.render := { record =>
              val proposal = record.asInstanceOf[SingleProposal]
              proposal.themeId.map { id =>
                Configuration.getThemeFromThemeId(id)
              }
            })(),
            <.ReferenceField(
              ^.source := "operationId",
              ^.label := "operation",
              ^.reference := Resource.operations,
              ^.linkType := false,
              ^.allowEmpty := true
            )(<.TextField(^.source := "slug")()),
            <.TextField(^.source := "context.source", ^.label := "source")(),
            <.TextField(^.source := "context.question", ^.label := "question")(),
            <.DateField(
              ^.source := "createdAt",
              ^.label := "date",
              ^.options := Map("weekday" -> "long", "year" -> "numeric", "month" -> "long", "day" -> "numeric"),
              ^.locales := "en-EN"
            )(),
            <.TextField(^.source := "author.firstName", ^.label := "User name")(),
            <.TextField(^.source := "author.profile.age", ^.label := "User age")(),
            <.TextField(^.source := "author.profile.postalCode", ^.label := "User location")()
          )
        )
    )
  )
}

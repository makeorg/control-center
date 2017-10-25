package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.models.SingleProposal
import org.make.services.proposal.{Accepted, Refused}

object ShowProposalComponents {

  case class ShowComponentsState(proposal: SingleProposal)

  lazy val reactClass: ReactClass = React.createClass[Unit, ShowComponentsState](
    displayName = "ShowProposalComponent",
    getInitialState = { self =>
      val proposal = self.props.native.record.asInstanceOf[SingleProposal]
      ShowComponentsState(proposal)
    },
    componentWillUpdate = { (self, props, _) =>
      val proposal = props.native.record.asInstanceOf[SingleProposal]
      self.setState(ShowComponentsState(proposal))
    },
    render = self =>
      <.div()(
        if (self.state.proposal.status == Refused.shortName)
          <.ModerationHistoryComponent(^.wrapped := ModerationHistoryComponent.HistoryProps(self.state.proposal))(),
        if (self.state.proposal.status == Accepted.shortName)
          <.StatsValidatedProposal(^.wrapped := StatsValidatedProposal.StatsProps(self.state.proposal))(),
        if (self.state.proposal.status == Accepted.shortName)
          <.FormValidateProposalComponent(
            ^.wrapped := FormValidateProposalComponent.FormProps(self.state.proposal, "update")
          )(),
        if (self.state.proposal.status != Refused.shortName)
          <.FormRefuseProposalComponent(^.wrapped := FormRefuseProposalComponent.FormProps(self.state.proposal))(),
        if (self.state.proposal.status != Accepted.shortName)
          <.FormValidateProposalComponent(
            ^.wrapped := FormValidateProposalComponent.FormProps(self.state.proposal, "validate")
          )(),
        if (self.state.proposal.status == Accepted.shortName)
          <.ModerationHistoryComponent(^.wrapped := ModerationHistoryComponent.HistoryProps(self.state.proposal))()
    )
  )

}

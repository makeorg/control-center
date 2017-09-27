package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.models.SingleProposal
import org.make.services.proposal.{Accepted, Pending, Refused}

import scala.scalajs.js

object ShowProposalComponents {

  @js.native
  trait Native extends js.Object {
    val props: RecordProposal
  }
  object Native {
    def apply(props: RecordProposal): Native =
      js.Dynamic.literal(props = props).asInstanceOf[Native]
  }

  @js.native
  trait RecordProposal extends js.Object {
    val record: SingleProposal
  }
  object RecordProposal {
    def apply(record: SingleProposal): RecordProposal =
      js.Dynamic.literal(record = record).asInstanceOf[RecordProposal]
  }

  case class ShowComponentsProps(basePath: String, record: SingleProposal, resource: String)
  case class ShowComponentsState(proposal: SingleProposal)

  lazy val reactClass: ReactClass = React.createClass[ShowComponentsProps, ShowComponentsState](
    getInitialState = { self =>
      val native = self.native.asInstanceOf[Native]
      val proposal = native.props.record
      ShowComponentsState(proposal)
    },
    componentWillUpdate = { (self, props, state) =>
      val proposal = props.native.record.asInstanceOf[SingleProposal]
      self.setState(ShowComponentsState(proposal))
    },
    render = self =>
      <.div()(
        if (self.state.proposal.status == Refused.shortName)
          <.ModerationHistoryComponent(^.wrapped := ModerationHistoryComponent.HistoryProps(self.state.proposal))(),
        if (self.state.proposal.status == Pending.shortName)
          <.FormRefuseProposalComponent(^.wrapped := FormRefuseProposalComponent.FormProps(self.state.proposal))(),
        if (self.state.proposal.status != Accepted.shortName)
          <.FormValidateProposalComponent(^.wrapped := FormValidateProposalComponent.FormProps(self.state.proposal))(),
        if (self.state.proposal.status == Accepted.shortName)
          <.ModerationHistoryComponent(^.wrapped := ModerationHistoryComponent.HistoryProps(self.state.proposal))()
    )
  )

}

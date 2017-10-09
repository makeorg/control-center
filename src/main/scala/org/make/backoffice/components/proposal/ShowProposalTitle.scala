package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.models.SingleProposal

object ShowProposalTitle {

  case class ShowTitleState(proposal: SingleProposal)

  lazy val reactClass: ReactClass = React.createClass[Unit, ShowTitleState](getInitialState = { self =>
    val proposal = self.props.native.record.asInstanceOf[SingleProposal]
    ShowTitleState(proposal)
  }, componentWillUpdate = { (self, props, state) =>
    val proposal = props.native.record.asInstanceOf[SingleProposal]
    self.setState(ShowTitleState(proposal))
  }, render = self => <.h1()(self.state.proposal.content))

}

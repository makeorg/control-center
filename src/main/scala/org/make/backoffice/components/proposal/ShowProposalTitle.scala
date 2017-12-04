package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.models.SingleProposal
import org.make.services.proposal.{Postponed, Refused}

object ShowProposalTitle {

  case class ShowTitleState(proposal: SingleProposal)

  lazy val reactClass: ReactClass = React.createClass[Unit, ShowTitleState](getInitialState = { self =>
    val proposal = self.props.native.record.asInstanceOf[SingleProposal]
    ShowTitleState(proposal)
  }, componentWillUpdate = { (self, props, _) =>
    val proposal = props.native.record.asInstanceOf[SingleProposal]
    self.setState(ShowTitleState(proposal))
  }, render = { self =>
    val style: Map[String, _] = self.state.proposal.status match {
      case Postponed.shortName => Map("color" -> "#ffa500")
      case Refused.shortName   => Map("color" -> "#ff3232")
      case _                   => Map.empty
    }
    <.h1(^.style := style)(self.state.proposal.content)
  })

}

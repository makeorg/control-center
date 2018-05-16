package org.make.backoffice.component.proposal.common

import java.util.{Timer, TimerTask}

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.SingleProposal
import org.make.backoffice.client.BadRequestHttpException
import org.make.backoffice.service.proposal.{ProposalService, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object ShowProposalComponents {

  sealed trait Context { val name: String }

  object Context {
    case object StartModeration extends Context { override val name: String = "startModeration" }
    case object List extends Context { override val name: String = "list" }
  }

  val timer = new Timer()
  def task(self: React.Self[ShowProposalComponentsProps, ShowProposalComponentsState]): TimerTask = new TimerTask {
    override def run(): Unit = {
      if (org.scalajs.dom.window.location.hash == self.props.wrapped.hash) {
        ProposalService.lock(self.state.proposal.id).onComplete {
          case Success(_) => self.setState(_.copy(isLocked = false))
          case Failure(badRequest: BadRequestHttpException) =>
            val moderatorName: Option[String] = badRequest.errors.find(_.field == "moderatorName").flatMap(_.message)
            self.setState(_.copy(isLocked = true, moderatorName = moderatorName))
          case Failure(_) => self.setState(_.copy(isLocked = true))
        }
      } else {
        this.cancel()
      }
    }
  }

  case class ShowProposalComponentsProps(hash: String,
                                         eventRefresh: Boolean = false,
                                         proposal: Option[SingleProposal],
                                         context: Context)
  case class ShowProposalComponentsState(proposal: SingleProposal,
                                         isLocked: Boolean = false,
                                         moderatorName: Option[String] = None)

  lazy val reactClass: ReactClass = React.createClass[ShowProposalComponentsProps, ShowProposalComponentsState](
    displayName = "ShowProposalComponent",
    getInitialState = { self =>
      val proposal = self.props.wrapped.proposal.getOrElse(self.props.native.record.asInstanceOf[SingleProposal])
      ShowProposalComponentsState(proposal)
    },
    componentDidMount = { self =>
      timer.scheduleAtFixedRate(task(self), 0L, 10000L)
    },
    componentWillReceiveProps = { (self, props) =>
      val proposal = props.wrapped.proposal.getOrElse(props.native.record.asInstanceOf[SingleProposal])
      self.setState(_.copy(proposal = proposal))
    },
    render = self =>
      <.div()(
        if (self.state.proposal.status == Refused.shortName)
          <.ModerationHistoryComponent(^.wrapped := ModerationHistoryComponent.HistoryProps(self.state.proposal))(),
        if (self.state.proposal.status == Accepted.shortName)
          <.StatsValidatedProposal(
            ^.wrapped := ValidatedProposalStats.ValidatedProposalStatsProps(self.state.proposal)
          )(),
        if (self.state.proposal.status == Accepted.shortName)
          <.FormValidateProposalComponent(
            ^.wrapped := FormValidateProposalComponent
              .FormProps(self.state.proposal, "update", self.state.isLocked, self.props.wrapped.context)
          )(),
        if (self.state.proposal.status == Pending.shortName)
          <.FormPostponeProposalComponent(
            ^.wrapped := FormPostponeProposalComponent
              .FormProps(self.state.proposal, self.state.isLocked, self.props.wrapped.context)
          )(),
        if (self.state.proposal.status != Refused.shortName)
          <.FormRefuseProposalComponent(
            ^.wrapped := FormRefuseProposalComponent
              .FormProps(self.state.proposal, self.state.isLocked, self.props.wrapped.context)
          )(),
        if (self.state.proposal.status != Accepted.shortName)
          <.FormValidateProposalComponent(
            ^.wrapped := FormValidateProposalComponent
              .FormProps(self.state.proposal, "validate", self.state.isLocked, self.props.wrapped.context)
          )(),
        if (self.state.proposal.status == Accepted.shortName)
          <.ModerationHistoryComponent(^.wrapped := ModerationHistoryComponent.HistoryProps(self.state.proposal))(),
        if (self.state.isLocked)
          <.Snackbar(^.open := self.state.isLocked, ^.message := {
            self.state.moderatorName match {
              case Some(moderatorName) => s"proposal locked by: $moderatorName"
              case None                => "proposal locked"
            }
          }, ^.onRequestClose := (_ => ()))()
    )
  )

}

package org.make.backoffice.components.proposal

import java.util.{Timer, TimerTask}

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.models.SingleProposal
import org.make.client.BadRequestHttpException
import org.make.services.proposal.ProposalService
import org.make.services.proposal._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object ShowProposalComponents {

  val timer = new Timer()
  def task(self: React.Self[ShowComponentsProps, ShowComponentsState]): TimerTask = new TimerTask {
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

  case class ShowComponentsProps(hash: String, eventRefresh: Boolean = false)
  case class ShowComponentsState(proposal: SingleProposal,
                                 isLocked: Boolean = false,
                                 moderatorName: Option[String] = None)

  lazy val reactClass: ReactClass = React.createClass[ShowComponentsProps, ShowComponentsState](
    displayName = "ShowProposalComponent",
    getInitialState = { self =>
      val proposal = self.props.native.record.asInstanceOf[SingleProposal]
      ShowComponentsState(proposal)
    },
    componentDidMount = { self =>
      timer.scheduleAtFixedRate(task(self), 0L, 10000L)
    },
    componentWillUpdate = { (self, props, _) =>
      val proposal = props.native.record.asInstanceOf[SingleProposal]
      self.setState(_.copy(proposal = proposal))
    },
    render = self =>
      <.div()(
        if (self.state.proposal.status == Refused.shortName)
          <.ModerationHistoryComponent(^.wrapped := ModerationHistoryComponent.HistoryProps(self.state.proposal))(),
        if (self.state.proposal.status == Accepted.shortName)
          <.StatsValidatedProposal(^.wrapped := StatsValidatedProposal.StatsProps(self.state.proposal))(),
        if (self.state.proposal.status == Accepted.shortName)
          <.FormValidateProposalComponent(
            ^.wrapped := FormValidateProposalComponent.FormProps(self.state.proposal, "update", self.state.isLocked)
          )(),
        if (self.state.proposal.status == Pending.shortName)
          <.FormPostponeProposalComponent(
            ^.wrapped := FormPostponeProposalComponent.FormProps(self.state.proposal, self.state.isLocked)
          )(),
        if (self.state.proposal.status != Refused.shortName)
          <.FormRefuseProposalComponent(
            ^.wrapped := FormRefuseProposalComponent.FormProps(self.state.proposal, self.state.isLocked)
          )(),
        if (self.state.proposal.status != Accepted.shortName)
          <.FormValidateProposalComponent(
            ^.wrapped := FormValidateProposalComponent.FormProps(self.state.proposal, "validate", self.state.isLocked)
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

/*
 * Make.org Control Center
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package org.make.backoffice.component.proposal.common

import java.util.{Timer, TimerTask}

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.SingleProposal
import org.make.backoffice.client.BadRequestHttpException
import org.make.backoffice.component.proposal.common.FormEnrichProposalComponent.FormEnrichProposalProps
import org.make.backoffice.component.proposal.common.FormPostponeProposalComponent.FormPostponeProposalProps
import org.make.backoffice.component.proposal.common.FormRefuseProposalComponent.FormRefuseProposalProps
import org.make.backoffice.component.proposal.common.FormValidateProposalComponent.FormValidateProposalProps
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
                                         minVotesCount: Option[String],
                                         toEnrichMinScore: Option[String],
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
        if (self.state.proposal.status == Accepted.shortName)
          <.FormEnrichProposalComponent(
            ^.wrapped := FormEnrichProposalProps(
              self.state.proposal,
              self.props.wrapped.minVotesCount,
              self.props.wrapped.toEnrichMinScore,
              "enrich",
              self.state.isLocked,
              self.props.wrapped.context
            )
          )(),
        if (self.state.proposal.status != Accepted.shortName)
          <.FormValidateProposalComponent(
            ^.wrapped := FormValidateProposalProps(
              self.state.proposal,
              "validate",
              self.state.isLocked,
              self.props.wrapped.context
            )
          )(),
        if (self.state.proposal.status == Accepted.shortName)
          <.FormValidateProposalComponent(
            ^.wrapped := FormValidateProposalProps(
              self.state.proposal,
              "update",
              self.state.isLocked,
              self.props.wrapped.context
            )
          )(),
        if (self.state.proposal.status != Refused.shortName)
          <.FormRefuseProposalComponent(
            ^.wrapped := FormRefuseProposalProps(self.state.proposal, self.state.isLocked, self.props.wrapped.context)
          )(),
        if (self.state.proposal.status == Pending.shortName)
          <.FormPostponeProposalComponent(
            ^.wrapped := FormPostponeProposalProps(self.state.proposal, self.state.isLocked, self.props.wrapped.context)
          )(),
        if (self.state.proposal.status != Accepted.shortName)
          <.FormEnrichProposalComponent(
            ^.wrapped := FormEnrichProposalProps(
              self.state.proposal,
              self.props.wrapped.minVotesCount,
              self.props.wrapped.toEnrichMinScore,
              "enrich",
              self.state.isLocked,
              self.props.wrapped.context
            )
          )(),
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

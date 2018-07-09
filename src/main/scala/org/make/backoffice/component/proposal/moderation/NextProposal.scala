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

package org.make.backoffice.component.proposal.moderation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.proposal.common.ShowProposalComponents.{Context, ShowProposalComponentsProps}
import org.make.backoffice.model.SingleProposal
import org.make.backoffice.service.proposal.ProposalService
import org.make.backoffice.facade.MaterialUi._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object NextProposal {

  final case class NextProposalProps() extends RouterProps
  final case class NextProposalState(proposal: Option[SingleProposal])

  lazy val reactClass: ReactClass =
    React.createClass[NextProposalProps, NextProposalState](
      displayName = "NextProposal",
      getInitialState = { _ =>
        NextProposalState(None)
      },
      componentDidMount = { self =>
        ProposalService.getProposalById(self.props.`match`.params("id")).onComplete {
          case Success(proposalResponse) => self.setState(_.copy(proposal = Some(proposalResponse.data)))
          case Failure(e)                => js.Dynamic.global.console.log(e.getMessage)
        }
      },
      componentWillReceiveProps = { (self, props) =>
        ProposalService.getProposalById(props.`match`.params("id")).onComplete {
          case Success(proposalResponse) => self.setState(_.copy(proposal = Some(proposalResponse.data)))
          case Failure(e)                => js.Dynamic.global.console.log(e.getMessage)
        }
      },
      shouldComponentUpdate = { (_, props, state) =>
        state.proposal.map(_.id).contains(props.`match`.params("id"))
      },
      componentWillUpdate = { (_, _, _) =>
        js.Dynamic.global.scrollTo(0, 0)
      },
      render = { self =>
        if (self.state.proposal.nonEmpty) {
          <.div()(
            <.Card()(<.CardTitle(^.title := self.state.proposal.map(_.content).getOrElse(""))()),
            <.ShowProposalComponents(
              ^.wrapped := ShowProposalComponentsProps(
                hash = org.scalajs.dom.window.location.hash,
                proposal = self.state.proposal,
                context = Context.StartModeration
              )
            )()
          )
        } else {
          <.div()()
        }
      }
    )
}

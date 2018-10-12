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

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.statictags.Element
import org.make.backoffice.client.NotFoundHttpException
import org.make.backoffice.component.Main
import org.make.backoffice.component.proposal.common.ShowProposalComponents.Context
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.SingleProposal
import org.make.backoffice.service.proposal.ProposalService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FormPostponeProposalComponent {

  case class FormPostponeProposalProps(proposal: SingleProposal, isLocked: Boolean = false, context: Context)
  case class FormPostponeProposalState(errorMessage: Option[String] = None, isLocked: Boolean = false)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[FormPostponeProposalProps, FormPostponeProposalState](
        displayName = "FormPostponeProposalComponent",
        getInitialState = { _ =>
          FormPostponeProposalState()
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(_.copy(errorMessage = None, isLocked = props.wrapped.isLocked))
        },
        render = { self =>
          def handleNextProposal: SyntheticEvent => Unit = {
            event =>
              event.preventDefault()
              val futureNextProposal =
                for {
                  _ <- ProposalService.postponeProposal(proposalId = self.props.wrapped.proposal.id)
                  nextProposal <- ProposalService
                    .nextProposalToModerate(
                      self.props.wrapped.proposal.operationId.toOption,
                      self.props.wrapped.proposal.themeId.toOption,
                      Some(self.props.wrapped.proposal.country),
                      Some(self.props.wrapped.proposal.language),
                      toEnrich = false,
                      minVotesCount = None,
                      minScore = None
                    )
                } yield nextProposal
              futureNextProposal.onComplete {
                case Success(proposalResponse) =>
                  self.props.history.push(s"/nextProposal/${proposalResponse.data.id}")
                case Failure(NotFoundHttpException) => self.props.history.push("/proposals")
                case Failure(_)                     => self.setState(_.copy(errorMessage = Some(Main.defaultErrorMessage)))
              }
          }

          def handlePostpone: SyntheticEvent => Unit = { event =>
            event.preventDefault()
            ProposalService.postponeProposal(proposalId = self.props.wrapped.proposal.id).onComplete {
              case Success(_) =>
                self.props.history.goBack()
              case Failure(NotFoundHttpException) => self.props.history.push("/proposals")
              case Failure(_)                     => self.setState(_.copy(errorMessage = Some(Main.defaultErrorMessage)))
            }
          }

          def handleSubmit: SyntheticEvent => Unit = {
            if (self.props.wrapped.context == ShowProposalComponents.Context.StartModeration)
              handleNextProposal
            else
              handlePostpone
          }

          val errorMessage: Option[Element] =
            self.state.errorMessage.map(msg => <.p()(msg))

          <.Card(^.style := Map("marginTop" -> "1em"))(
            <.CardTitle(^.title := "I want to postpone this proposal")(),
            <.CardActions()(
              <.RaisedButton(
                ^.disabled := self.state.isLocked,
                ^.label := "Confirm postpone",
                ^.onClick := handleSubmit
              )(),
              errorMessage
            )
          )
        }
      )
    )

}

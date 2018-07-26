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
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.statictags.Element
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.util.FormatToPercent._
import org.make.backoffice.model.{Proposal, SingleProposal, Vote}

object ValidatedProposalStats {

  case class ValidatedProposalStatsProps(proposal: SingleProposal)
  case class ValidatedProposalStatsState(votes: Seq[Vote] = Seq.empty)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[ValidatedProposalStatsProps, ValidatedProposalStatsState](
        displayName = "ValidatedProposalStats",
        getInitialState = { self =>
          ValidatedProposalStatsState(votes = self.props.wrapped.proposal.votes)
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(_.copy(votes = props.wrapped.proposal.votes))
        },
        render = { self =>
          def displayQualificationsStats(vote: Vote): Seq[Element] = {
            vote.qualifications.map { qualification =>
              <.p()(s"${qualification.key}: ${Proposal.qualificationRate(vote, qualification.key)}% (${Proposal
                .qualificationFromKey(vote.qualifications, qualification.key)})")
            }
          }

          def displayVotesStats: Seq[Element] = {
            self.state.votes.map { vote =>
              <.div(
                ^.style := Map(
                  "display" -> "inline-block",
                  "width" -> s"${formatToPercent(1, self.state.votes.length)}%"
                )
              )(
                <.p()(s"${vote.key}: ${Proposal
                  .voteRate(self.state.votes, vote.key)}% (${Proposal.voteFromKey(self.state.votes, vote.key)})"),
                <.div(^.style := Map("marginLeft" -> "1em"))(displayQualificationsStats(vote))
              )
            }
          }

          <.Card(^.style := Map("marginTop" -> "1em"))(
            <.CardTitle(^.title := "Stats")(),
            <.CardText()(<.h4()(s"Total votes: ${Proposal.totalVotes(self.state.votes)}"), <.div()(displayVotesStats))
          )
        }
      )
    )
}

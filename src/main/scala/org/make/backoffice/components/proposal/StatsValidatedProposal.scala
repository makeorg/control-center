package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.statictags.Element
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.helpers.FormatToPercent._
import org.make.backoffice.models.{Proposal, SingleProposal, Vote}

object StatsValidatedProposal {

  case class StatsProps(proposal: SingleProposal)
  case class StatsState(votes: Seq[Vote] = Seq.empty)

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[StatsProps, StatsState](
        getInitialState = { self =>
          StatsState(votes = self.props.wrapped.proposal.votes)
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(_.copy(votes = props.wrapped.proposal.votes))
        },
        render = { self =>
          def displayQualificationsStats(vote: Vote): Seq[Element] = {
            vote.qualifications.map { qualification =>
              <.p()(s"${qualification.key}: ${Proposal.qualificationRate(vote.qualifications, qualification.key)}% (${Proposal
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

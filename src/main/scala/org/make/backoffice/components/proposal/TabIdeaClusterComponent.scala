package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.models.{Proposal, SingleProposal}

object TabIdeaClusterComponent {

  case class TabIdeaClusterState(proposal: SingleProposal)

  def ideaProposalsElements(proposal: SingleProposal): ReactElement = {
    <.TableBody(^.displayRowCheckbox := false)(
      proposal.ideaProposals.map { ideaProposal =>
        <.TableRow(^.key := s"${ideaProposal.id}")(
          <.TableRowColumn()(
            <.a(^.href := s"#/validated_proposals/${ideaProposal.id}/show")(ideaProposal.content)
          )
        )
      }
    )
  }

  lazy val reactClass: ReactClass = React.createClass[Unit, TabIdeaClusterState](
      displayName = "TabIdeaClusterComponent",
      getInitialState = { self =>
        val proposal = self.props.native.record.asInstanceOf[SingleProposal]

        TabIdeaClusterState(proposal = proposal)
      },
      componentWillReceiveProps = { (self, props) =>
        val proposal = props.native.record.asInstanceOf[SingleProposal]
        self.setState(_.copy(proposal = proposal))
      },
      render = self =>
          <.Card()(
            <.Table(^.selectable := false)(ideaProposalsElements(self.state.proposal))
          )
    )
}

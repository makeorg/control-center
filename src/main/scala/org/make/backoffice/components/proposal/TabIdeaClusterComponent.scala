package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.models.SingleProposal

object TabIdeaClusterComponent {

  case class TabIdeaClusterState(proposal: SingleProposal,
                                 ideaProposalsElements: ReactElement)

  def ideaProposalsElements(proposal: SingleProposal): ReactElement = {
    <.TableBody(^.displayRowCheckbox := false)(
      proposal.ideaProposals
        .map(_.map { ideaProposal =>
          <.TableRow(^.key := s"${ideaProposal.id}")(
            <.TableRowColumn()(
              <.a(^.href := s"#/validated_proposals/${ideaProposal.id}/show")(ideaProposal.content)
            )
          )
        })
        .getOrElse(
          <.TableRow(^.key := "empty")(<.TableRowColumn()("No Similar proposal"))
        )
    )
  }

  lazy val reactClass: ReactClass = React.createClass[Unit, TabIdeaClusterState](
      displayName = "TabIdeaClusterComponent",
      getInitialState = { self =>
        val proposal = self.props.native.record.asInstanceOf[SingleProposal]

        TabIdeaClusterState(proposal = proposal, ideaProposalsElements = ideaProposalsElements(proposal))
      },
      componentWillReceiveProps = { (self, props) =>
        val proposal = props.native.record.asInstanceOf[SingleProposal]
        self.setState(_.copy(proposal = proposal, ideaProposalsElements = ideaProposalsElements(proposal)))
      },
      render = self =>
          <.Card()(
            <.Table(^.selectable := false)(self.state.ideaProposalsElements)
          )
    )
}

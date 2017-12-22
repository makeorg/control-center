package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.facades.AdminOnRest.ShowButton._
import org.make.backoffice.models.{Proposal, SingleProposal}
import org.make.services.idea.IdeaServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object TabIdeaClusterComponent {

  case class TabIdeaClusterState(proposal: SingleProposal,
                                 ideaProposals: Seq[Proposal] = Seq.empty,
                                 ideaName: String = "")

  def ideaProposalsElements(ideaProposals: Seq[Proposal]): ReactElement = {
    <.TableBody(^.displayRowCheckbox := false)(ideaProposals.map { ideaProposal =>
      <.TableRow(^.key := s"${ideaProposal.id}")(
        <.TableRowColumn(^.style := Map("width" -> "10%"))(
          <.ShowButton(^.basePath := "/validated_proposals", ^.record := js.Dynamic.literal("id" -> ideaProposal.id))()
        ),
        <.TableRowColumn()(ideaProposal.content)
      )
    })
  }

  lazy val reactClass: ReactClass =
    React.createClass[Unit, TabIdeaClusterState](
      displayName = "TabIdeaClusterComponent",
      getInitialState = { self =>
        val proposal = self.props.native.record.asInstanceOf[SingleProposal]
        TabIdeaClusterState(proposal = proposal)
      },
      componentWillReceiveProps = { (self, props) =>
        val proposal = props.native.record.asInstanceOf[SingleProposal]
        self.setState(_.copy(proposal = proposal, ideaProposals = proposal.ideaProposals.toSeq))
        proposal.idea.map { idea =>
          IdeaServiceComponent.ideaService.getIdea(idea.value).onComplete {
            case Success(response) => self.setState(_.copy(ideaName = response.data.name))
            case Failure(e)        => js.Dynamic.global.console(e.getMessage)
          }
        }
      },
      render = self =>
        <.Card()(
          <.CardTitle(^.title := self.state.ideaName)(),
          <.Table(^.selectable := false)(ideaProposalsElements(self.state.ideaProposals))
      )
    )
}

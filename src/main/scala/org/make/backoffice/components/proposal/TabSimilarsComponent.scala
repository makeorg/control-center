package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.components.proposal.SimilarClusterComponent.ClusterProps
import org.make.backoffice.components.proposal.FormValidateProposalComponent.proposalService
import org.make.backoffice.components.proposal.SimilarProposalsComponent.SimilarProposalsProps
import org.make.backoffice.facades.MaterialUi.MaterialUiVirtualDOMElements
import org.make.backoffice.models.{Proposal, ProposalId, SingleProposal}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object TabSimilarsComponent {
  case class TabSimilarsState(proposal: SingleProposal,
                              similarProposals: Seq[Proposal] = Seq.empty,
                              selectedSimilarProposals: Seq[String] = Seq.empty,
                              errorMessage: Option[String] = None)

  lazy val reactClass: ReactClass =
    React.createClass[Unit, TabSimilarsState](displayName = "TabSimilarsComponent", getInitialState = { self =>
      val proposal = self.props.native.record.asInstanceOf[SingleProposal]
      TabSimilarsState(proposal)
    }, componentWillReceiveProps = { (self, props) =>
      val proposal = props.native.record.asInstanceOf[SingleProposal]
      self.setState(_.copy(proposal = proposal))
    }, render = {
      self =>
        def setSelectedSimilarProposals(selectedSimilarProposals: Seq[String]): Unit = {
          self.setState(_.copy(selectedSimilarProposals = selectedSimilarProposals))
        }

        def handleSubmit: (SyntheticEvent) => Unit = {
          event =>
            event.preventDefault()
            proposalService
              .updateProposal(
                proposalId = self.state.proposal.id,
                newContent = None,
                theme = None,
                labels = self.state.proposal.labels,
                tags = self.state.proposal.tags,
                similarProposals = self.state.selectedSimilarProposals.map(ProposalId.apply)
              )
              .onComplete {
                case Success(_) =>
                  self.setState(_.copy(errorMessage = None))
                case Failure(_) =>
                  self.setState(_.copy(errorMessage = Some("Oooops, something went wrong")))
              }
        }

        if (self.state.proposal.similarProposals.getOrElse(js.Array()).isEmpty) {
          <.div()(
            <.SimilarProposalsComponent(
              ^.wrapped := SimilarProposalsProps(
                self.state.proposal,
                setSelectedSimilarProposals,
                self.state.proposal.theme.toOption,
                self.state.proposal.context.operation.toOption
              )
            )(),
            <.RaisedButton(
              ^.style := Map("marginTop" -> "1em"),
              ^.label := s"Set similar",
              ^.onClick := handleSubmit
            )(),
            self.state.errorMessage.map(msg => <.p()(msg))
          )
        } else {
          <.SimilarClusterComponent(^.wrapped := ClusterProps(self.state.proposal))()
        }
    })
}

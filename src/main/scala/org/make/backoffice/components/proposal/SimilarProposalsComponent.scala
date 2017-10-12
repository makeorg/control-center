package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import io.github.shogowada.statictags.Element
import org.make.backoffice.models.{ProposalId, ProposalsResult, SingleProposal}
import org.make.services.proposal.ProposalServiceComponent
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object SimilarProposalsComponent {
  case class SimilarProposalsProps(proposal: SingleProposal, setSimilarProposals: Seq[String] => Unit)
  case class SimilarProposalsState(similarProposals: ProposalsResult, selectedSimilars: Seq[String])

  lazy val reactClass: ReactClass =
    React.createClass[SimilarProposalsProps, SimilarProposalsState](
      getInitialState = { self =>
        SimilarProposalsState(ProposalsResult.empty, Seq.empty)
      },
      componentWillMount = { self =>
        ProposalServiceComponent.proposalService.getDuplicates(ProposalId(self.props.wrapped.proposal.id)).onComplete {
          case Success(proposalsResult) => self.setState(_.copy(similarProposals = proposalsResult))
          case Failure(e)               => scalajs.js.Dynamic.global.console.log(s"get duplicate failed with error $e")
        }
      },
      render = { self =>
        def handleAddSimilar: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
          val proposalId: String = event.target.value
          val selectedSimilars =
            if (self.state.selectedSimilars.contains(proposalId)) {
              self.state.selectedSimilars.filterNot(_ == proposalId)
            } else {
              self.state.selectedSimilars :+ proposalId
            }
          self.setState(_.copy(selectedSimilars = selectedSimilars))
          self.props.wrapped.setSimilarProposals(selectedSimilars)
        }
        val similars: Seq[Element] =
          self.state.similarProposals.results.toSeq.flatMap { similar =>
            Seq(
              <.input(
                ^.`type`.checkbox,
                ^.value := similar.id,
                ^.id := s"similar-${similar.id}",
                ^.name := s"similar-${similar.id}",
                ^.checked := self.state.selectedSimilars.contains(similar.id),
                ^.onChange := handleAddSimilar
              )(),
              <.label(^.`for` := s"similar-${similar.id}")(similar.content),
              <.hr()()
            )
          }
        <.div()(similars)
      }
    )
}

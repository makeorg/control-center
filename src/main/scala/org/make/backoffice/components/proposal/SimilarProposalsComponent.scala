package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.backoffice.facades.DataSourceConfig
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.models.{Proposal, ProposalId, SingleProposal}
import org.make.client.request.{Filter, Pagination}
import org.make.services.proposal.{Accepted, ProposalServiceComponent}
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object SimilarProposalsComponent {
  case class SimilarProposalsProps(proposal: SingleProposal, setSimilarProposals: Seq[String] => Unit)
  case class SimilarProposalsState(similarProposals: Seq[Proposal],
                                   selectedSimilars: Seq[String],
                                   searchProposalContent: String,
                                   foundSimilarProposals: Seq[Proposal])

  lazy val reactClass: ReactClass =
    React.createClass[SimilarProposalsProps, SimilarProposalsState](
      getInitialState = { _ =>
        SimilarProposalsState(Seq.empty, Seq.empty, "", Seq.empty)
      },
      componentWillMount = { self =>
        ProposalServiceComponent.proposalService.getDuplicates(ProposalId(self.props.wrapped.proposal.id)).onComplete {
          case Success(proposalsResult) => self.setState(_.copy(similarProposals = proposalsResult.results))
          case Failure(e)               => scalajs.js.Dynamic.global.console.log(s"get duplicate failed with error $e")
        }
      },
      render = { self =>
        def handleAddSimilar: (FormSyntheticEvent[HTMLInputElement], Boolean) => Unit = { (event, _) =>
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

        def handleUpdateInput(searchText: String, dataSource: js.Array[js.Object], params: js.Object): Unit = {
          self.setState(_.copy(searchProposalContent = searchText))
          if (searchText.length >= 3) {
            ProposalServiceComponent.proposalService
              .proposals(
                Some(Pagination(1, 10)),
                None,
                Some(Seq(Filter("content", searchText), Filter("status", Accepted.shortName)))
              )
              .onComplete {
                case Success(proposals) => self.setState(_.copy(foundSimilarProposals = proposals.data))
                case Failure(e)         => js.Dynamic.global.console.log(e.getMessage)
              }
          }
        }

        def handleNewRequest(chosenRequest: js.Object, index: Int): Unit = {
          val similar = chosenRequest.asInstanceOf[Proposal]
          val selectedSimilars = (self.state.selectedSimilars ++ Seq(similar.id)).distinct
          val similarProposals = if (self.state.similarProposals.map(_.id).contains(similar.id)) {
            self.state.similarProposals
          } else {
            self.state.similarProposals ++ Seq(similar)
          }
          self.setState(
            _.copy(searchProposalContent = "", similarProposals = similarProposals, selectedSimilars = selectedSimilars)
          )
          self.props.wrapped.setSimilarProposals(selectedSimilars)
        }

        def handleInvalidateSimilarProposal(similarProposalId: String): SyntheticEvent => Unit = { _ =>
          ProposalServiceComponent.proposalService
            .invalidateSimilarProposal(ProposalId(self.props.wrapped.proposal.id), ProposalId(similarProposalId))
          self.setState(_.copy(selectedSimilars = self.state.selectedSimilars.filterNot(_ == similarProposalId)))
        }

        val similars =
          self.state.similarProposals.flatMap {
            similar =>
              Seq(
                <.Checkbox(
                  ^.value := similar.id,
                  ^.label := similar.content,
                  ^.checked := self.state.selectedSimilars.contains(similar.id),
                  ^.onCheck := handleAddSimilar,
                  ^.style := Map("max-width" -> "90%", "display" -> "inline-block")
                )(),
                <.a(
                  ^.onClick := handleInvalidateSimilarProposal(similar.id),
                  ^.title := "Invalidate this similar proposal suggestion",
                  ^.style := Map("font-size" -> "1.5em", "cursor" -> "pointer", "text-decoration" -> "none")
                )("\uD83D\uDEAB"),
                <.hr(^.style := Map("margin-top" -> "10px"))()
              )
          }

        val searchNew: ReactElement =
          <.AutoComplete(
            ^.id := "search-similar-proposals",
            ^.hintText := "Search proposal",
            ^.dataSource := self.state.foundSimilarProposals,
            ^.dataSourceConfig := DataSourceConfig("content", "id"),
            ^.searchText := self.state.searchProposalContent,
            ^.onUpdateInput := handleUpdateInput,
            ^.onNewRequest := handleNewRequest
          )()

        <.CardActions()(searchNew, <.br()(), similars)
      }
    )
}

package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import io.github.shogowada.statictags.Element
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
          val selectedSimilars = (self.state.selectedSimilars ++ Seq(chosenRequest.asInstanceOf[Proposal].id)).distinct
          val similarProposals = (self.state.similarProposals ++ Seq(chosenRequest.asInstanceOf[Proposal])).distinct
          self.setState(
            _.copy(searchProposalContent = "", similarProposals = similarProposals, selectedSimilars = selectedSimilars)
          )
          self.props.wrapped.setSimilarProposals(selectedSimilars)
        }

        val similars: Seq[Element] =
          self.state.similarProposals.flatMap { similar =>
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

        <.div()(searchNew, <.br()(), similars)
      }
    )
}

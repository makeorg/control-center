package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.backoffice.facades.DataSourceConfig
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.models._
import org.make.client.request.{Filter, Pagination}
import org.make.services.proposal.{Accepted, ProposalServiceComponent}
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object SimilarProposalsComponent {
  case class SimilarProposalsProps(proposal: SingleProposal,
                                   setSimilarProposals: Seq[String] => Unit,
                                   maybeThemeId: Option[ThemeId],
                                   maybeOperation: Option[String])
  case class SimilarProposalsState(similarProposals: Seq[Proposal],
                                   selectedSimilars: Seq[String],
                                   searchProposalContent: String,
                                   foundSimilarProposals: Seq[Proposal])

  def loadSimilarProposals(self: Self[SimilarProposalsProps, SimilarProposalsState],
                           props: SimilarProposalsProps): Future[ProposalsResult] = {
    val proposalId: ProposalId = ProposalId(props.proposal.id)
    val themeId: Option[ThemeId] = props.maybeThemeId
    val operation: Option[String] = props.maybeOperation
    ProposalServiceComponent.proposalService
      .getDuplicates(proposalId, themeId, operation)
  }

  lazy val reactClass: ReactClass =
    React.createClass[SimilarProposalsProps, SimilarProposalsState](
      getInitialState = { self =>
        SimilarProposalsState(Seq.empty, Seq.empty, "", Seq.empty)
      },
      componentWillMount = { (self) =>
        if (self.props.wrapped.proposal.status != Accepted.shortName) {
          loadSimilarProposals(self, self.props.wrapped).onComplete {
            case Success(proposalsResult) =>
              self.setState(_.copy(similarProposals = proposalsResult.results))
            case Failure(e) => scalajs.js.Dynamic.global.console.log(s"get duplicate failed with error $e")
          }
        }
      },
      componentWillReceiveProps = { (self, props) =>
        if (props.wrapped.proposal.status != Accepted.shortName) {
          loadSimilarProposals(self, props.wrapped).onComplete {
            case Success(proposalsResult) =>
              val newSimilarProposals =
                proposalsResult.results.filterNot(similar => self.state.similarProposals.map(_.id).contains(similar.id))
              self.setState(_.copy(similarProposals = self.state.similarProposals ++ newSimilarProposals))
            case Failure(e) => scalajs.js.Dynamic.global.console.log(s"get duplicate failed with error $e")
          }
        }
      },
      render = { self =>
        def handleAddSimilar: (FormSyntheticEvent[HTMLInputElement], Boolean) => Unit = {
          (event, _) =>
            val proposalId: String = event.target.value
            val selectedSimilars =
              if (self.state.selectedSimilars.contains(proposalId)) {
                ProposalServiceComponent.proposalService
                  .invalidateSimilarProposal(ProposalId(self.props.wrapped.proposal.id), ProposalId(proposalId))
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
            val filters: Option[Seq[Filter]] = Some(
              Seq(Filter("content", searchText), Filter("status", Accepted.shortName)) ++
                (self.props.wrapped.maybeOperation match {
                  case Some(operation) => Seq(Filter("operation", operation))
                  case None            => Seq.empty
                })
            )
            ProposalServiceComponent.proposalService
              .proposals(Some(Pagination(1, 10)), None, filters)
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

        val similars =
          self.state.similarProposals.flatMap { similar =>
            Seq(
              <.Checkbox(
                ^.value := similar.id,
                ^.label := similar.content,
                ^.checked := self.state.selectedSimilars.contains(similar.id),
                ^.onCheck := handleAddSimilar,
                ^.style := Map("maxWidth" -> "90%", "display" -> "inline-block")
              )(),
              <.hr(^.style := Map("marginTop" -> "10px"))()
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
            ^.onNewRequest := handleNewRequest,
            ^.fullWidth := true,
            ^.popoverProps := Map("canAutoPosition" -> "true")
          )()

        <.CardActions()(searchNew, <.br()(), similars)
      }
    )
}

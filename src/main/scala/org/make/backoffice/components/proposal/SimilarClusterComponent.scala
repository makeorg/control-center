package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.helpers.Configuration
import org.make.backoffice.models.{Proposal, SingleProposal, ThemeId}
import org.make.client.request.{Filter, Pagination}
import org.make.services.proposal.ProposalServiceComponent.ProposalService
import org.make.services.proposal.{Accepted, ProposalServiceComponent}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object SimilarClusterComponent {
  val proposalService: ProposalService = ProposalServiceComponent.proposalService

  case class ClusterState(proposal: SingleProposal, similarProposals: Seq[Proposal] = Seq.empty)

  lazy val reactClass: ReactClass =
    React.createClass[Unit, ClusterState](displayName = "ClusterComponent", getInitialState = { self =>
      val proposal = self.props.native.record.asInstanceOf[SingleProposal]
      ClusterState(proposal)
    }, componentWillReceiveProps = {
      (self, props) =>
        val proposal = props.native.record.asInstanceOf[SingleProposal]
        self.setState(_.copy(proposal = proposal))
        proposal.similarProposals match {
          case similar if similar.isEmpty                    => self.setState(_.copy(similarProposals = Seq.empty))
          case similar if similar.toOption.forall(_.isEmpty) => self.setState(_.copy(similarProposals = Seq.empty))
          case _ =>
            proposalService
              .proposals(
                pagination = Some(Pagination(page = 1, perPage = 500)),
                None,
                Some(
                  Seq(
                    Filter(field = "status", value = js.Array(Accepted.shortName)),
                    Filter(
                      field = "proposalIds",
                      value =
                        proposal.similarProposals.toOption.map(similar => similar.map(_.value)).getOrElse(js.Array())
                    )
                  )
                )
              )
              .onComplete {
                case Success(response) => self.setState(_.copy(similarProposals = response.data.toSeq))
                case Failure(e)        => js.Dynamic.global.console(e.getMessage)
              }
        }
    }, render = {
      self =>
        def handleDeleteFromCluster(proposalId: String): (SyntheticEvent) => Unit =
          (event) => {
            event.preventDefault()
            proposalService.removeFromCluster(proposalId)
          }

        <.Table(^.selectable := false)(
          <.TableHeader(^.displaySelectAll := false)(
            <.TableRow()(
              <.TableRowColumn()("Content"),
              <.TableRowColumn()("Theme"),
              <.TableRowColumn()("Operation"),
              <.TableRowColumn()("Author name"),
              <.TableRowColumn()("Author age"),
              <.TableRowColumn()()
            )
          ),
          <.TableBody(^.displayRowCheckbox := false)(self.state.similarProposals.map { proposal =>
            <.TableRow()(
              <.TableRowColumn()(proposal.content),
              <.TableRowColumn()(
                proposal.themeId.map(themeId => Configuration.getThemeFromThemeId(ThemeId(themeId))).getOrElse("")
              ),
              <.TableRowColumn()(proposal.context.operation.getOrElse("")),
              <.TableRowColumn()(proposal.author.firstName.getOrElse("")),
              <.TableRowColumn()(proposal.author.age.getOrElse("")),
              <.TableRowColumn()(
                <.RaisedButton(
                  ^.secondary := true,
                  ^.style := Map("float" -> "right"),
                  ^.label := "Delete",
                  ^.onClick := handleDeleteFromCluster(proposal.id)
                )()
              )
            )
          })
        )
    })
}

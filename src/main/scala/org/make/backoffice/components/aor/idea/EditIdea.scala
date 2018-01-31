package org.make.backoffice.components.aor.idea

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.facades.AdminOnRest.Edit._
import org.make.backoffice.facades.AdminOnRest.Fields._
import org.make.backoffice.facades.AdminOnRest.FormTab._
import org.make.backoffice.facades.AdminOnRest.Inputs._
import org.make.backoffice.facades.AdminOnRest.ShowButton._
import org.make.backoffice.facades.AdminOnRest.TabbedForm._
import org.make.backoffice.facades.DataSourceConfig
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.models.{Idea, IdeaId, Proposal, ProposalId}
import org.make.client.request.{Filter, Pagination}
import org.make.client.{MakeServices, Resource}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.|
import scala.util.{Failure, Success}

object EditIdea extends MakeServices {

  case class TitleState(idea: Idea)

  lazy val ideaTitle: ReactClass = React.createClass[Unit, TitleState](getInitialState = self => {
    val idea = self.props.native.record.asInstanceOf[Idea]
    TitleState(idea)
  }, componentWillUpdate = (self, props, _) => {
    val idea = props.native.record.asInstanceOf[Idea]
    self.setState(_.copy(idea))
  }, render = self => {
    <.h1()(self.state.idea.name)
  })

  case class DataGridState(ideas: Seq[Idea],
                           proposalsList: Seq[Proposal],
                           searchIdeaContent: String,
                           selectedIdeaId: Option[IdeaId],
                           selectedIds: Seq[ProposalId],
                           snackbarOkOpen: Boolean = false,
                           snackbarKoOpen: Boolean = false)

  lazy val dataGrid: ReactClass =
    React
      .createClass[Unit, DataGridState](
        displayName = "dataGrid",
        getInitialState = _ => DataGridState(Seq.empty, Seq.empty, "", None, Seq.empty),
        componentDidMount = self => {
          if (self.state.ideas.isEmpty) {
            ideaService
              .listIdeas(
                Some(Pagination(page = 1, perPage = 1000)),
                None,
                Some(Seq(Filter("operationId", self.props.native.record.operationId)))
              )
              .onComplete {
                case Success(ideaResponse) => self.setState(_.copy(ideas = ideaResponse.data.toSeq))
                case Failure(e)            => js.Dynamic.global.console.log(s"Failed with error $e")
              }
          }
          proposalService.proposalsByIdea(self.props.native.record.id.toString).onComplete {
            case Success(proposals) =>
              self.setState(_.copy(proposalsList = proposals.data.toSeq))
            case Failure(e) => js.Dynamic.global.console.log(s"Failed with error $e")
          }
        },
        render = self => {
          def onRowSelection(ids: Seq[String]): js.Function1[js.Array[Int] | String, Unit] = (rowNumber) => {
            var selectedIds: Seq[String] = Seq.empty
            (rowNumber: Any) match {
              case rows: js.Array[_] if rows.nonEmpty => selectedIds = rows.map(n => ids(n.asInstanceOf[Int])).toSeq
              case all: String if all == "all"        => selectedIds = ids
              case _                                  => selectedIds = Seq.empty
            }
            self.setState(_.copy(selectedIds = selectedIds.map(ProposalId(_))))
          }

          def handleUpdateInput: (String, js.Array[js.Object], js.Object) => Unit = (searchText, _, _) => {
            self.setState(_.copy(searchIdeaContent = searchText))
          }

          def handleNewRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
            val idea = chosenRequest.asInstanceOf[Idea]
            self.setState(_.copy(selectedIdeaId = Some(IdeaId(idea.id))))
          }

          def filterAutoComplete: (String, String) => Boolean = (searchText, key) => {
            key.indexOf(searchText) != -1
          }

          def onSnackbarClose: (String) => Unit = (_) => {
            self.setState(_.copy(snackbarOkOpen = false, snackbarKoOpen = false))
          }

          def onClickChangeIdea: (SyntheticEvent) => Unit = {
            event =>
              event.preventDefault()
              self.state.selectedIdeaId match {
                case Some(ideaId) =>
                  if (self.state.selectedIds.nonEmpty) {
                    proposalService.changeProposalsIdea(ideaId, self.state.selectedIds).onComplete {
                      case Success(_) =>
                        val newProposalsList = self.state.proposalsList
                          .filterNot(proposal => self.state.selectedIds.exists(_.value == proposal.id))
                        self.setState(
                          _.copy(
                            proposalsList = newProposalsList,
                            selectedIds = Seq.empty,
                            searchIdeaContent = "",
                            snackbarOkOpen = true
                          )
                        )
                      case Failure(_) => self.setState(_.copy(snackbarKoOpen = true))
                    }
                  }
                case None =>
              }
          }

          if (self.state.proposalsList.nonEmpty) {
            <.div()(
              <.h2()(s"Proposals list (${self.state.proposalsList.length}):"),
              <.Table(
                ^.multiSelectable := true,
                ^.onRowSelection := onRowSelection(self.state.proposalsList.map(_.id)),
                ^.style := Map("tableLayout" -> "auto"),
                ^.fixedHeader := false
              )(
                <.TableHeader()(<.TableRow()(<.TableHeaderColumn()("Content"), <.TableHeaderColumn()())),
                <.TableBody(^.deselectOnClickaway := false)(self.state.proposalsList.map { proposal =>
                  <.TableRow(^.key := proposal.id, ^.selected := self.state.selectedIds.exists(_.value == proposal.id))(
                    <.TableRowColumn(^.style := Map("whiteSpace" -> "normal", "wordWrap" -> "break-word"))(
                      proposal.content
                    ),
                    <.TableRowColumn()(
                      <.ShowButton(
                        ^.basePath := "/validated_proposals",
                        ^.record := js.Dynamic.literal("id" -> proposal.id)
                      )()
                    )
                  )
                })
              ),
              <.br()(),
              <.AutoComplete(
                ^.id := "search-proposal-idea",
                ^.hintText := "Search idea",
                ^.dataSource := self.state.ideas,
                ^.dataSourceConfig := DataSourceConfig("name", "id"),
                ^.searchText := self.state.searchIdeaContent,
                ^.hintText := "Search idea",
                ^.onUpdateInput := handleUpdateInput,
                ^.onNewRequest := handleNewRequest,
                ^.fullWidth := true,
                ^.popoverProps := Map("canAutoPosition" -> true),
                ^.openOnFocus := true,
                ^.filterAutoComplete := filterAutoComplete,
                ^.menuProps := Map("maxHeight" -> 400)
              )(),
              <.FlatButton(
                ^.fullWidth := true,
                ^.label := "Change idea",
                ^.secondary := true,
                ^.onClick := onClickChangeIdea
              )(),
              <.Snackbar(
                ^.open := self.state.snackbarOkOpen,
                ^.message := "Proposal(s) update successfully",
                ^.autoHideDuration := 3000,
                ^.onRequestClose := onSnackbarClose
              )(),
              <.Snackbar(
                ^.open := self.state.snackbarKoOpen,
                ^.message := "Proposal(s) update failed",
                ^.autoHideDuration := 3000,
                ^.onRequestClose := onSnackbarClose,
                ^.bodyStyle := Map("backgroundColor" -> "red")
              )()
            )
          } else {
            <.h3()("No proposals")
          }
        }
      )

  case class EditProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[EditProps, Unit](
        displayName = "EditIdea",
        render = self => {
          <.Edit(
            ^.resource := Resource.ideas,
            ^.location := self.props.location,
            ^.`match` := self.props.`match`,
            ^.editTitle := <.IdeaTitle()()
          )(
            <.TabbedForm()(
              <.FormTab(^.label := "Infos")(
                <.TextField(^.source := "id")(),
                <.TextInput(^.source := "name", ^.options := Map("fullWidth" -> true))(),
                <.ReferenceField(
                  ^.source := "operationId",
                  ^.label := "operation",
                  ^.reference := Resource.operations,
                  ^.linkType := false,
                  ^.allowEmpty := true
                )(<.TextField(^.source := "slug")()),
                <.TextField(^.source := "country")(),
                <.TextField(^.source := "language")(),
                <.TextField(^.source := "question")()
              ),
              <.FormTab(^.label := "Proposals list")(<.CustomDatagrid()())
            )
          )
        }
      )

}

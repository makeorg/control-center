package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.components.proposal.NewIdeaComponent.NewIdeaProps
import org.make.backoffice.facades.DataSourceConfig
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.models._
import org.make.services.idea.IdeaServiceComponent
import org.make.services.proposal.ProposalServiceComponent
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object NewIdeaComponent {

  case class NewIdeaProps(setProposalIdea: Option[IdeaId] => Unit, setIdeas: Idea => Unit, operation: Option[String])
  case class NewIdeaState(ideaName: String, open: Boolean = false)

  private def createIdea(self: React.Self[NewIdeaProps, NewIdeaState]): (SyntheticEvent) => Unit = { _ =>
    IdeaServiceComponent.ideaService
      .createIdea(name = self.state.ideaName, operation = self.props.wrapped.operation)
      .onComplete {
        case Success(idea) =>
          self.props.wrapped.setProposalIdea(Some(idea.ideaId))
          self.props.wrapped.setIdeas(idea)
          self.setState(_.copy(open = false))
        case Failure(e) => js.Dynamic.global.console.log(s"Fail to create idea: $e")
      }
  }

  private def handleOpen(self: React.Self[NewIdeaProps, NewIdeaState]): (SyntheticEvent) => Unit = { event =>
    event.preventDefault()
    self.setState(_.copy(open = true))
  }

  private def handleClose(self: React.Self[NewIdeaProps, NewIdeaState]): (SyntheticEvent) => Unit = { event =>
    event.preventDefault()
    self.setState(_.copy(open = false))
  }

  private def actionsModal(self: React.Self[NewIdeaProps, NewIdeaState]): Seq[ReactElement] = {
    Seq(
      <.FlatButton(^.label := "Cancel", ^.secondary := true, ^.onClick := handleClose(self))(),
      <.FlatButton(^.label := "Create", ^.primary := true, ^.onClick := createIdea(self))()
    )
  }

  lazy val reactClass: ReactClass =
    React
      .createClass[NewIdeaProps, NewIdeaState](
        displayName = "NewIdeaComponent",
        getInitialState = (_) => NewIdeaState(ideaName = "new_idea"),
        render = { self =>
          def handleIdeaNameEdition: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
            val newContent: String = event.target.value
            self.setState(_.copy(ideaName = newContent))
          }

          <.div(^.style := Map("display" -> "inline-block"))(
            <.FlatButton(^.label := "New idea", ^.primary := true, ^.onClick := handleOpen(self))(),
            <.Dialog(
              ^.title := "Choose your idea name",
              ^.open := self.state.open,
              ^.modal := true,
              ^.actionsModal := actionsModal(self)
            )(
              <.TextFieldMaterialUi(
                ^.value := self.state.ideaName,
                ^.onChange := handleIdeaNameEdition,
                ^.floatingLabelText := "idea name"
              )()
            )
          )
        }
      )

}

object ProposalIdeaComponent {
  case class ProposalIdeaProps(proposal: SingleProposal, setProposalIdea: Option[IdeaId] => Unit, ideaName: String)
  case class ProposalIdeaState(ideas: Seq[Idea],
                               selectedIdea: Option[IdeaId],
                               searchIdeaContent: String,
                               foundProposalIdeas: Seq[Idea],
                               similarResult: Seq[SimilarResult],
                               ideaName: Option[String],
                               isLoading: Boolean = true)

  def loadIdeas(self: Self[ProposalIdeaProps, ProposalIdeaState], props: ProposalIdeaProps): Future[Seq[Idea]] = {
    IdeaServiceComponent.ideaService.listIdeas(None, None, props.proposal.operationId.toOption, None)
  }

  def loadDuplicates(props: ProposalIdeaProps): Future[Seq[SimilarResult]] = {
    ProposalServiceComponent.proposalService
      .getDuplicates(
        props.proposal.id,
        props.proposal.themeId.toOption.map(ThemeId(_)),
        props.proposal.operationId.toOption
      )
  }

  lazy val reactClass: ReactClass =
    React.createClass[ProposalIdeaProps, ProposalIdeaState](
      displayName = "ProposalIdeaComponent",
      getInitialState = { _ =>
        ProposalIdeaState(Seq.empty, None, "", Seq.empty, Seq.empty, None)
      },
      componentDidMount = { (self) =>
        loadIdeas(self, self.props.wrapped).onComplete {
          case Success(listIdeas) =>
            self.setState(_.copy(ideas = listIdeas, foundProposalIdeas = listIdeas))
          case Failure(e) => scalajs.js.Dynamic.global.console.log(s"get ideas failed with error $e")
        }
        loadDuplicates(self.props.wrapped).onComplete {
          case Success(similarResult) => self.setState(_.copy(similarResult = similarResult, isLoading = false))
          case Failure(e) =>
            self.setState(_.copy(isLoading = false))
            scalajs.js.Dynamic.global.console.log(s"get similar failed with error $e")
        }
        self.setState(_.copy(selectedIdea = self.props.wrapped.proposal.idea.toOption))
      },
      render = { self =>
        def handleUpdateInput: (String, js.Array[js.Object], js.Object) => Unit = (searchText, _, _) => {
          self.setState(_.copy(searchIdeaContent = searchText))
        }

        def handleNewRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
          val idea = chosenRequest.asInstanceOf[Idea]
          val selectedIdea = Some(idea.ideaId)
          self.setState(_.copy(selectedIdea = selectedIdea, ideaName = Some(idea.name)))
          self.props.wrapped.setProposalIdea(selectedIdea)
        }

        def setIdeas(newIdea: Idea): Unit = {
          self.setState(
            _.copy(
              foundProposalIdeas = self.state.ideas ++ Seq(newIdea),
              ideas = self.state.ideas ++ Seq(newIdea),
              selectedIdea = Some(newIdea.ideaId),
              searchIdeaContent = newIdea.name
            )
          )
        }

        def filterAutoComplete: (String, String) => Boolean = (searchText, key) => {
          key.indexOf(searchText) != -1
        }

        val searchNew: ReactElement =
          <.AutoComplete(
            ^.id := "search-proposal-idea",
            ^.hintText := "Search idea",
            ^.dataSource := self.state.foundProposalIdeas,
            ^.dataSourceConfig := DataSourceConfig("name", "ideaId"),
            ^.searchText := self.state.searchIdeaContent,
            ^.hintText := "Search idea",
            ^.onUpdateInput := handleUpdateInput,
            ^.onNewRequest := handleNewRequest,
            ^.fullWidth := true,
            ^.popoverProps := Map("canAutoPosition" -> true),
            ^.openOnFocus := true,
            ^.filterAutoComplete := filterAutoComplete,
            ^.menuProps := Map("maxHeight" -> 400)
          )()

        def onCheckSimilarIdea(ideaId: IdeaId,
                               ideaName: String): (FormSyntheticEvent[HTMLInputElement], Boolean) => Unit =
          (_, isChecked) => {
            if (isChecked) {
              self.setState(_.copy(selectedIdea = Some(ideaId), ideaName = Some(ideaName)))
              self.props.wrapped.setProposalIdea(Some(ideaId))
            } else {
              self.setState(_.copy(selectedIdea = None, ideaName = None))
              self.props.wrapped.setProposalIdea(self.props.wrapped.proposal.idea.toOption)
            }

            self.setState(_.copy(searchIdeaContent = ""))
          }

        <.Card(^.style := Map("marginTop" -> "1em"))(
          <.CardTitle(^.title := "Idea", ^.subtitle := self.state.ideaName.getOrElse(self.props.wrapped.ideaName))(),
          <.CardActions()(<.h4()("Similar ideas:"), if (self.state.isLoading) {
            <.CircularProgress()()
          } else {
            self.state.similarResult.map { idea =>
              <.Checkbox(
                ^.label := idea.ideaName,
                ^.checked := self.state.selectedIdea.contains(idea.ideaId),
                ^.onCheck := onCheckSimilarIdea(idea.ideaId, idea.ideaName)
              )()
            }
          }, searchNew, <.br()(), <.NewIdeaComponent(^.wrapped := NewIdeaProps(self.props.wrapped.setProposalIdea, setIdeas, self.props.wrapped.proposal.context.operation.toOption))())
        )
      }
    )
}

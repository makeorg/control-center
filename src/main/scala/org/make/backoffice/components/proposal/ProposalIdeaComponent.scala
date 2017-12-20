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
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object NewIdeaComponent {

  case class NewIdeaProps(setProposalIdea: Option[IdeaId] => Unit, setIdeas: Idea => Unit, operation: Option[String])
  case class NewIdeaState(ideaName: String, open: Boolean = false)

  private def createIdea(self: React.Self[NewIdeaProps, NewIdeaState]): (SyntheticEvent) => Unit = { _ =>
    IdeaServiceComponent.ideaService.createIdea(name = self.state.ideaName, operation = self.props.wrapped.operation).onComplete {
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
  case class ProposalIdeaProps(proposal: SingleProposal,
                               setProposalIdea: Option[IdeaId] => Unit,
                               maybeOperation: Option[String])
  case class ProposalIdeaState(ideas: Seq[Idea],
                               selectedIdea: Option[IdeaId],
                               searchIdeaContent: String,
                               foundProposalIdeas: Seq[Idea])

  def loadIdeas(self: Self[ProposalIdeaProps, ProposalIdeaState], props: ProposalIdeaProps): Future[Seq[Idea]] = {
    IdeaServiceComponent.ideaService.listIdeas(None, None, props.maybeOperation, None)
  }

  lazy val reactClass: ReactClass =
    React.createClass[ProposalIdeaProps, ProposalIdeaState](
      displayName = "ProposalIdeaComponent",
      getInitialState = { _ =>
        ProposalIdeaState(Seq.empty, None, "", Seq.empty)
      },
      componentDidMount = { (self) =>
        loadIdeas(self, self.props.wrapped).onComplete {
          case Success(listIdeas) =>
            self.setState(_.copy(ideas = listIdeas, foundProposalIdeas = listIdeas))
          case Failure(e) => scalajs.js.Dynamic.global.console.log(s"get ideas failed with error $e")
        }
        self.setState(_.copy(selectedIdea = self.props.wrapped.proposal.idea.toOption))
      },
      render = { self =>
        def handleUpdateInput(searchText: String, dataSource: js.Array[js.Object], params: js.Object): Unit = {
          self.setState(_.copy(searchIdeaContent = searchText))
        }

        def handleNewRequest(chosenRequest: js.Object, index: Int): Unit = {
          val idea = chosenRequest.asInstanceOf[Idea]
          val selectedIdea = Some(idea.ideaId)
          self.setState(_.copy(selectedIdea = selectedIdea))
          self.props.wrapped.setProposalIdea(selectedIdea)
        }

        def handleIdeaChange: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
          val idea = Some(IdeaId(value))
          self.setState(_.copy(selectedIdea = idea))
          self.props.wrapped.setProposalIdea(idea)
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
            ^.onUpdateInput := handleUpdateInput,
            ^.onNewRequest := handleNewRequest,
            ^.fullWidth := true,
            ^.popoverProps := Map("canAutoPosition" -> true),
            ^.openOnFocus := true,
            ^.filterAutoComplete := filterAutoComplete
          )()

        <.CardActions()(
          searchNew,
          <.br()(),
          <.NewIdeaComponent(^.wrapped := NewIdeaProps(
            self.props.wrapped.setProposalIdea,
            setIdeas,
            self.props.wrapped.proposal.context.operation.toOption
          ))()
        )
      }
    )
}

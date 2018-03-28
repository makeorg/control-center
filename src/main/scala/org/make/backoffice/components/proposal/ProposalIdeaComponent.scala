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
import org.make.backoffice.facades.AdminOnRest.EditButton._
import org.make.backoffice.models._
import org.make.client.ListTotalResponse
import org.make.client.request.{Filter, Pagination}
import org.make.services.idea.IdeaService
import org.make.services.proposal.ProposalService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object NewIdeaComponent {

  case class NewIdeaProps(setProposalIdea: Option[IdeaId] => Unit,
                          setIdeas: Idea                  => Unit,
                          operation: Option[String],
                          theme: Option[String],
                          language: Option[String],
                          country: Option[String],
                          question: Option[String])
  case class NewIdeaState(ideaName: String, open: Boolean = false)

  private def createIdea(self: React.Self[NewIdeaProps, NewIdeaState]): (SyntheticEvent) => Unit = { _ =>
    IdeaService
      .createIdea(
        name = self.state.ideaName,
        operation = self.props.wrapped.operation,
        theme = self.props.wrapped.theme,
        language = self.props.wrapped.language,
        country = self.props.wrapped.country,
        question = self.props.wrapped.question
      )
      .onComplete {
        case Success(ideaResponse) =>
          self.props.wrapped.setProposalIdea(Some(IdeaId(ideaResponse.data.id)))
          self.props.wrapped.setIdeas(ideaResponse.data)
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
                ^.floatingLabelText := "idea name",
                ^.fullWidth := true
              )()
            )
          )
        }
      )

}

object ProposalIdeaComponent {
  case class ProposalIdeaProps(proposal: SingleProposal, setProposalIdea: Option[IdeaId] => Unit, ideaName: String)
  case class ProposalIdeaState(ideas: Seq[Idea],
                               selectedIdeaId: Option[IdeaId],
                               searchIdeaContent: String,
                               foundProposalIdeas: Seq[Idea],
                               similarResult: Seq[SimilarResult],
                               ideaName: Option[String],
                               isLoading: Boolean = true)

  def loadIdeas(self: Self[ProposalIdeaProps, ProposalIdeaState],
                props: ProposalIdeaProps): Future[ListTotalResponse[Idea]] = {
    var filters = Seq(
      Filter.apply(field = "language", value = props.proposal.language),
      Filter.apply(field = "country", value = props.proposal.country)
    )

    if (props.proposal.operationId.isDefined) {
      filters +:= Filter.apply(field = "operationId", value = props.proposal.operationId)
    }

    if (props.proposal.themeId.isDefined) {
      filters +:= Filter.apply(field = "themeId", value = props.proposal.themeId)
    }

    IdeaService
      .listIdeas(pagination = Some(Pagination(page = 1, perPage = 1000)), filters = Some(filters))
  }

  def loadDuplicates(props: ProposalIdeaProps): Future[Seq[SimilarResult]] = {
    ProposalService.getDuplicates(props.proposal.id)
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
            self.setState(_.copy(ideas = listIdeas.data.toSeq, foundProposalIdeas = listIdeas.data.toSeq))
          case Failure(e) => scalajs.js.Dynamic.global.console.log(s"get ideas failed with error $e")
        }
        loadDuplicates(self.props.wrapped).onComplete {
          case Success(similarResult) => self.setState(_.copy(similarResult = similarResult, isLoading = false))
          case Failure(e) =>
            self.setState(_.copy(isLoading = false))
            scalajs.js.Dynamic.global.console.log(s"get similar failed with error $e")
        }
        self.setState(_.copy(selectedIdeaId = self.props.wrapped.proposal.ideaId.map(IdeaId(_)).toOption))
        if (self.props.wrapped.ideaName.isEmpty) {
          self.props.wrapped.proposal.ideaId.foreach { ideaId =>
            IdeaService.getIdea(ideaId).onComplete {
              case Success(ideaResponse) => self.setState(_.copy(ideaName = Some(ideaResponse.data.name)))
              case Failure(e)            => js.Dynamic.global.console.log(s"Failed with error $e")
            }
          }
        }
      },
      render = { self =>
        def handleUpdateInput: (String, js.Array[js.Object], js.Object) => Unit = (searchText, _, _) => {
          self.setState(_.copy(searchIdeaContent = searchText))
        }

        def handleNewRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
          val idea = chosenRequest.asInstanceOf[Idea]
          val selectedIdea = Some(IdeaId(idea.id))
          self.setState(_.copy(selectedIdeaId = selectedIdea, ideaName = Some(idea.name)))
          self.props.wrapped.setProposalIdea(selectedIdea)
        }

        def setIdeas(newIdea: Idea): Unit = {
          self.setState(
            _.copy(
              foundProposalIdeas = self.state.ideas ++ Seq(newIdea),
              ideas = self.state.ideas ++ Seq(newIdea),
              selectedIdeaId = Some(IdeaId(newIdea.id)),
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
          )()

        def onCheckSimilarIdea(ideaId: String,
                               ideaName: String): (FormSyntheticEvent[HTMLInputElement], Boolean) => Unit =
          (_, isChecked) => {
            if (isChecked) {
              self.setState(_.copy(selectedIdeaId = Some(IdeaId(ideaId)), ideaName = Some(ideaName)))
              self.props.wrapped.setProposalIdea(Some(IdeaId(ideaId)))
            } else {
              self.setState(_.copy(selectedIdeaId = None, ideaName = None))
              self.props.wrapped.setProposalIdea(self.props.wrapped.proposal.ideaId.map(IdeaId(_)).toOption)
            }

            self.setState(_.copy(searchIdeaContent = ""))
          }

        <.Card(^.style := Map("marginTop" -> "1em"))(
          <.CardTitle(^.title := "Idea", ^.subtitle := self.state.ideaName.getOrElse(self.props.wrapped.ideaName))(),
          if (self.state.selectedIdeaId.isDefined) {
            <.CardActions()(
              <.EditButton(
                ^.label := "Edit Idea",
                ^.basePath := "/ideas",
                ^.record := js.Dynamic.literal("id" -> self.state.selectedIdeaId.map(_.value).getOrElse("").toString)
              )()
            )
          },
          <.CardActions()(<.h4()("Similar ideas:"), if (self.state.isLoading) {
            <.CircularProgress()()
          } else {
            self.state.similarResult.map { idea =>
              <.Checkbox(
                ^.label := idea.ideaName,
                ^.checked := self.state.selectedIdeaId.exists(_.value == idea.ideaId),
                ^.onCheck := onCheckSimilarIdea(idea.ideaId, idea.ideaName)
              )()
            }
          }, searchNew, <.br()(), <.NewIdeaComponent(^.wrapped := NewIdeaProps(self.props.wrapped.setProposalIdea, setIdeas, self.props.wrapped.proposal.operationId.toOption, self.props.wrapped.proposal.themeId.toOption, Some(self.props.wrapped.proposal.language), Some(self.props.wrapped.proposal.country), self.props.wrapped.proposal.context.question.toOption))())
        )
      }
    )
}

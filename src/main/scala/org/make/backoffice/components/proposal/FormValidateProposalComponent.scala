package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.statictags.Element
import org.make.backoffice.components.RichVirtualDOMElements
import org.make.backoffice.components.proposal.ProposalIdeaComponent.ProposalIdeaProps
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.helpers.Configuration
import org.make.backoffice.models._
import org.make.services.idea.IdeaServiceComponent
import org.make.services.idea.IdeaServiceComponent.IdeaService
import org.make.services.operation.OperationServiceComponent
import org.make.services.operation.OperationServiceComponent.OperationService
import org.make.services.proposal.ProposalServiceComponent
import org.make.services.proposal.ProposalServiceComponent.ProposalService
import org.make.services.tag.TagServiceComponent
import org.make.services.tag.TagServiceComponent.TagService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object FormValidateProposalComponent {
  val proposalService: ProposalService = ProposalServiceComponent.proposalService
  val ideaService: IdeaService = IdeaServiceComponent.ideaService
  val operationService: OperationService = OperationServiceComponent.operationService
  val tagService: TagService = TagServiceComponent.tagService

  case class FormProps(proposal: SingleProposal, action: String, isLocked: Boolean = false)
  case class FormState(content: String,
                       maxLength: Int,
                       labels: Seq[String] = Seq.empty,
                       notifyUser: Boolean = true,
                       theme: Option[ThemeId] = None,
                       operation: Option[Operation] = None,
                       tags: Seq[Tag] = Seq.empty,
                       tagsList: Seq[Tag] = Seq.empty,
                       errorMessage: Option[String] = None,
                       similarProposals: Seq[String] = Seq.empty,
                       idea: Option[IdeaId] = None,
                       ideaName: String = "",
                       isLocked: Boolean = false)

  def setTagsFromTagIds(self: Self[FormProps, FormState], props: FormProps): Unit = {
    if (!js.isUndefined(props.proposal.tagIds)) {
      props.proposal.tagIds.foreach { tagId =>
        tagService.tags.onComplete {
          case Success(tags) =>
            self.setState(_.copy(tags = tags.find(_.tagId.value == tagId) match {
              case Some(tag) => self.state.tags :+ tag
              case None      => self.state.tags
            }))
          case Failure(e) => js.Dynamic.global.console.log(s"Fail with error: $e")
        }
      }
    }
  }

  def setTagsListAndOperation(self: Self[FormProps, FormState], props: FormProps): Unit = {
    props.proposal.themeId.toOption match {
      case Some(themeId) =>
        self.setState(_.copy(tagsList = Configuration.getTagsFromThemeId(themeId)))
      case None =>
        props.proposal.operationId.toOption.foreach { operationId =>
          val futureOperationTags = for {
            operation <- operationService.getOperationById(operationId)
            tags      <- tagService.tags
          } yield (operation, tags)
          futureOperationTags.onComplete {
            case Success((operation, tags)) =>
              val tagsList =
                operation.countriesConfiguration.headOption
                  .map(_.tagIds.flatMap(tagId => tags.find(_.tagId.value == tagId.value)))
              self.setState(_.copy(operation = Some(operation), tagsList = tagsList.map(_.toSeq).getOrElse(Seq.empty)))
            case Failure(e) => js.Dynamic.global.console.log(s"File with error: $e")
          }
        }
    }
  }

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[FormProps, FormState](
          displayName = "FormValidateProposalComponent",
          getInitialState = { self =>
            FormState(
              content = self.props.wrapped.proposal.content,
              maxLength =
                Configuration.businessConfig.map(_.proposalMaxLength).getOrElse(Configuration.defaultProposalMaxLength),
              labels = self.props.wrapped.proposal.labels,
              theme = self.props.wrapped.proposal.themeId.toOption.map(ThemeId(_)),
              isLocked = self.props.wrapped.isLocked,
              similarProposals =
                self.props.wrapped.proposal.similarProposals.map(_.toSeq.map(_.value)).getOrElse(Seq.empty)
            )
          },
          componentDidMount = self => {
            setTagsFromTagIds(self, self.props.wrapped)
            setTagsListAndOperation(self, self.props.wrapped)
          },
          componentWillReceiveProps = { (self, props) =>
            self.setState(
              _.copy(
                labels = props.wrapped.proposal.labels,
                theme = props.wrapped.proposal.themeId.toOption.map(ThemeId(_)),
                isLocked = props.wrapped.isLocked,
                similarProposals =
                  self.props.wrapped.proposal.similarProposals.toOption.map(_.toSeq.map(_.value)).getOrElse(Seq.empty)
              )
            )
            setTagsFromTagIds(self, props.wrapped)
            setTagsListAndOperation(self, props.wrapped)
            props.wrapped.proposal.idea.toOption.foreach { idea =>
              ideaService.getIdea(idea.value).onComplete {
                case Success(response) =>
                  self.setState(_.copy(idea = Some(IdeaId(response.data.id)), ideaName = response.data.name))
                case Failure(e) => js.Dynamic.global.console.log(e.getMessage)
              }
            }
          },
          render = { self =>
            def handleContentEdition: (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
              val newContent: String = event.target.value.substring(0, self.state.maxLength)
              self.setState(_.copy(content = newContent))
            }

            def handleThemeChange: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
              val theme = Some(ThemeId(value))
              self.setState(_.copy(theme = theme, tags = Seq.empty))
            }

            def handleTagChange: (js.Object, js.UndefOr[Int], js.Array[String]) => Unit = { (_, _, values) =>
              val tags: Seq[Tag] = values.toSeq.map { value =>
                self.state.tagsList.find(tag => tag.label == value).getOrElse(Tag(TagId(""), ""))
              }
              self.setState(_.copy(tags = tags))
            }

            def handleNotifyUserChange: (js.Object, Boolean) => Unit = { (_, checked) =>
              self.setState(_.copy(notifyUser = checked))
            }

            def handleLabelSelection: (FormSyntheticEvent[HTMLInputElement], Boolean) => Unit = { (event, _) =>
              val label: String = event.target.value
              val selectedLabels: Seq[String] = {
                if (self.state.labels.contains(label)) {
                  self.state.labels.filter(_ != label)
                } else {
                  self.state.labels :+ label
                }
              }

              self.setState(_.copy(labels = selectedLabels))
            }

            def handleSubmitUpdate: (SyntheticEvent) => Unit = {
              event =>
                event.preventDefault()
                val mayBeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                proposalService
                  .updateProposal(
                    proposalId = self.props.wrapped.proposal.id,
                    newContent = mayBeNewContent,
                    labels = self.state.labels,
                    theme = self.state.theme,
                    tags = self.state.tags.map(_.tagId),
                    similarProposals = self.state.similarProposals.map(ProposalId.apply),
                    idea = self.state.idea
                  )
                  .onComplete {
                    case Success(_) =>
                      self.props.history.push("/validated_proposals")
                      self.setState(_.copy(errorMessage = None))
                    case Failure(_) =>
                      self.setState(_.copy(errorMessage = Some("Oooops, something went wrong")))
                  }
            }

            def handleSubmitValidate: (SyntheticEvent) => Unit = {
              event =>
                event.preventDefault()
                val mayBeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                proposalService
                  .validateProposal(
                    proposalId = self.props.wrapped.proposal.id,
                    newContent = mayBeNewContent,
                    sendNotificationEmail = self.state.notifyUser,
                    labels = self.state.labels,
                    theme = self.state.theme,
                    similarProposals = self.state.similarProposals.map(ProposalId.apply),
                    tags = self.state.tags.map(_.tagId),
                    idea = self.state.idea
                  )
                  .onComplete {
                    case Success(_) =>
                      self.props.history.push("/proposals")
                      self.setState(_.copy(errorMessage = None))
                    case Failure(_) =>
                      self.setState(_.copy(errorMessage = Some("Oooops, something went wrong")))
                  }
            }

            def handleSubmit: (SyntheticEvent) => Unit = {
              if (self.props.wrapped.action == "validate")
                handleSubmitValidate
              else
                handleSubmitUpdate
            }

            val selectTheme = <.SelectField(
              ^.disabled := self.state.operation.nonEmpty,
              ^.floatingLabelText := "Theme",
              ^.floatingLabelFixed := true,
              ^.value := self.state.theme.map(_.value).getOrElse("Select a theme"),
              ^.onChangeSelect := handleThemeChange,
              ^.fullWidth := true
            )(Configuration.businessConfig.map { bc =>
              bc.themes.map { theme =>
                <.MenuItem(
                  ^.key := theme.themeId.value,
                  ^.value := theme.themeId.value,
                  ^.primaryText := theme.translations.toArray
                    .find(_.language == Configuration.defaultLanguage)
                    .map(_.title)
                    .getOrElse("")
                )()
              }
            })

            val selectTags = <.SelectField(
              ^.disabled := self.state.theme.isEmpty && self.state.operation.isEmpty,
              ^.multiple := true,
              ^.floatingLabelText := "Tags",
              ^.floatingLabelFixed := true,
              ^.valueSelect := self.state.tags.map(_.label),
              ^.onChangeMultipleSelect := handleTagChange,
              ^.fullWidth := true
            )(self.state.tagsList.map { tag =>
              <.MenuItem(
                ^.key := tag.tagId.value,
                ^.insetChildren := true,
                ^.checked := self.state.tags.contains(tag),
                ^.value := tag.label,
                ^.primaryText := tag.label
              )()
            })

            val errorMessage: Option[Element] =
              self.state.errorMessage.map(msg => <.p()(msg))

            def setProposalIdea(idea: Option[IdeaId]): Unit = {
              self.setState(_.copy(idea = idea))
            }

            <.Card(^.style := Map("marginTop" -> "1em"))(
              <.CardTitle(^.title := s"I want to ${self.props.wrapped.action} this proposal")(),
              <.CardActions()(
                <.TextFieldMaterialUi(
                  ^.floatingLabelText := "Proposal content",
                  ^.value := self.state.content,
                  ^.onChange := handleContentEdition,
                  ^.fullWidth := true
                )(),
                <.span()(s"${self.state.content.length}/${self.state.maxLength}"),
                <.br()(),
                selectTheme,
                selectTags,
                <.Checkbox(
                  ^.disabled := self.props.wrapped.action == "update",
                  ^.label := "Notify user",
                  ^.checked := self.state.notifyUser && self.props.wrapped.action == "validate",
                  ^.onCheck := handleNotifyUserChange,
                  ^.style := Map("maxWidth" -> "25em")
                )(),
                <.Checkbox(
                  ^.label := "Local",
                  ^.value := Label.Local.name,
                  ^.checked := self.state.labels.contains(Label.Local.name),
                  ^.onCheck := handleLabelSelection,
                  ^.style := Map("maxWidth" -> "25em")
                )(),
                <.Checkbox(
                  ^.label := "Action",
                  ^.value := Label.Action.name,
                  ^.checked := self.state.labels.contains(Label.Action.name),
                  ^.onCheck := handleLabelSelection,
                  ^.style := Map("maxWidth" -> "25em")
                )(),
                <.Checkbox(
                  ^.label := "Star",
                  ^.value := Label.Star.name,
                  ^.checked := self.state.labels.contains(Label.Star.name),
                  ^.onCheck := handleLabelSelection,
                  ^.style := Map("maxWidth" -> "25em")
                )(),
                <.ProposalIdeaComponent(
                  ^.wrapped := ProposalIdeaProps(self.props.wrapped.proposal, setProposalIdea, self.state.ideaName)
                )(),
                <.RaisedButton(
                  ^.style := Map("marginTop" -> "1em"),
                  ^.label := s"Confirm ${if (self.props.wrapped.action == "validate") "validation" else "changes"}",
                  ^.onClick := handleSubmit,
                  ^.disabled := self.state.isLocked
                )(),
                errorMessage
              )
            )
          }
        )
    )
}

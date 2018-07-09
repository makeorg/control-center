/*
 * Make.org Control Center
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package org.make.backoffice.component.proposal.common

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.statictags.Element
import org.make.backoffice.client.{BadRequestHttpException, NotFoundHttpException}
import org.make.backoffice.component.{Main, RichVirtualDOMElements}
import org.make.backoffice.component.proposal.common.ProposalIdeaComponent.ProposalIdeaProps
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model._
import org.make.backoffice.service.idea.IdeaService
import org.make.backoffice.service.operation.OperationService
import org.make.backoffice.service.proposal.ProposalService
import org.make.backoffice.service.tag.TagService
import org.make.backoffice.service.tag.TagTypeService
import org.make.backoffice.util.Configuration
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object FormValidateProposalComponent {

  case class FormProps(proposal: SingleProposal,
                       action: String,
                       isLocked: Boolean = false,
                       context: ShowProposalComponents.Context)
  case class FormState(content: String,
                       maxLength: Int,
                       labels: Seq[String] = Seq.empty,
                       notifyUser: Boolean = true,
                       theme: Option[ThemeId] = None,
                       operation: Option[Operation] = None,
                       tagTypes: Seq[TagType] = Seq.empty,
                       tags: Seq[Tag] = Seq.empty,
                       tagsList: Seq[Tag] = Seq.empty,
                       errorMessage: Seq[String] = Seq.empty,
                       similarProposals: Seq[String] = Seq.empty,
                       ideaId: Option[IdeaId] = None,
                       ideaName: String = "",
                       isLocked: Boolean = false)

  def setTagsFromTagIds(self: Self[FormProps, FormState], props: FormProps): Unit = {
    if (!js.isUndefined(props.proposal.tagIds)) {
      props.proposal.tagIds.foreach { tagId =>
        TagService.tags().onComplete {
          case Success(tags) =>
            self.setState(_.copy(tags = tags.find(_.id == tagId) match {
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
        TagTypeService.tagTypes.onComplete {
          case Success(tagTypes) =>
            self.setState(_.copy(tagsList = Configuration.getTagsFromThemeId(themeId), tagTypes = tagTypes))
          case Failure(_) => self.setState(_.copy(tagsList = Configuration.getTagsFromThemeId(themeId)))
        }
      case None =>
        props.proposal.operationId.toOption.foreach { operationIdValue =>
          val futureOperationTags = for {
            operation <- OperationService.getOperationById(OperationId(operationIdValue))
            tagTypes  <- TagTypeService.tagTypes
            tags      <- TagService.tags(operation.map(_.id))
          } yield (operation, tagTypes, tags)
          futureOperationTags.onComplete {
            case Success((Some(operation), tagTypes, tags)) =>
              self.setState(_.copy(operation = Some(operation), tagsList = tags, tagTypes = tagTypes))
            case Success(_) => self.setState(_.copy(operation = None, tagsList = Seq.empty))
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
            self.props.wrapped.proposal.ideaId.toOption.foreach { ideaId =>
              IdeaService.getIdea(ideaId).onComplete {
                case Success(response) =>
                  self.setState(_.copy(ideaId = Some(IdeaId(response.data.id)), ideaName = response.data.name))
                case Failure(e) => js.Dynamic.global.console.log(e.getMessage)
              }
            }
          },
          componentWillReceiveProps = { (self, props) =>
            self.setState(
              _.copy(
                labels = props.wrapped.proposal.labels,
                theme = props.wrapped.proposal.themeId.toOption.map(ThemeId(_)),
                isLocked = props.wrapped.isLocked,
                similarProposals =
                  props.wrapped.proposal.similarProposals.toOption.map(_.toSeq.map(_.value)).getOrElse(Seq.empty)
              )
            )
            if (self.props.wrapped.proposal.id != props.wrapped.proposal.id) {
              self.setState(_.copy(content = props.wrapped.proposal.content, tags = Seq.empty))
            }
            setTagsFromTagIds(self, props.wrapped)
            setTagsListAndOperation(self, props.wrapped)
            props.wrapped.proposal.ideaId.toOption.foreach { ideaId =>
              IdeaService.getIdea(ideaId).onComplete {
                case Success(response) =>
                  self.setState(_.copy(ideaId = Some(IdeaId(response.data.id)), ideaName = response.data.name))
                case Failure(_) => self.setState(_.copy(ideaId = None, ideaName = ""))
              }
            }
          },
          render = { self =>
            def handleContentEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
              val newContent: String = event.target.value.substring(0, self.state.maxLength)
              self.setState(_.copy(content = newContent))
            }

            def handleThemeChange: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
              val theme = Some(ThemeId(value))
              self.setState(_.copy(theme = theme, tags = Seq.empty))
            }

            def handleTagChange: (js.Object, js.UndefOr[Int], js.Array[String]) => Unit = { (_, _, values) =>
              val tags: Seq[Tag] = values.toSeq.flatMap { value =>
                self.state.tagsList.find(tag => tag.label == value) match {
                  case Some(tag) => Seq(tag)
                  case _         => Seq.empty
                }
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

            def handleSubmitUpdate: SyntheticEvent => Unit = {
              event =>
                event.preventDefault()
                val mayBeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                ProposalService
                  .updateProposal(
                    proposalId = self.props.wrapped.proposal.id,
                    newContent = mayBeNewContent,
                    labels = self.state.labels,
                    theme = self.state.theme,
                    tags = self.state.tags.map(tag => TagId(tag.id)),
                    similarProposals = self.state.similarProposals.map(ProposalId.apply),
                    ideaId = self.state.ideaId,
                    operationId = self.props.wrapped.proposal.operationId.toOption.map(OperationId.apply)
                  )
                  .onComplete {
                    case Success(_) =>
                      self.props.history.goBack()
                      self.setState(_.copy(errorMessage = Seq.empty))
                    case Failure(BadRequestHttpException(errors)) =>
                      self.setState(_.copy(errorMessage = errors.map(_.message.getOrElse(""))))
                    case Failure(_) =>
                      self.setState(_.copy(errorMessage = Seq(Main.defaultErrorMessage)))
                  }
            }

            def handleSubmitValidate: SyntheticEvent => Unit = {
              event =>
                event.preventDefault()
                val mayBeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                ProposalService
                  .validateProposal(
                    proposalId = self.props.wrapped.proposal.id,
                    newContent = mayBeNewContent,
                    sendNotificationEmail = self.state.notifyUser,
                    labels = self.state.labels,
                    theme = self.state.theme,
                    similarProposals = self.state.similarProposals.map(ProposalId.apply),
                    tags = self.state.tags.map(tag => TagId(tag.id)),
                    ideaId = self.state.ideaId,
                    operationId = self.props.wrapped.proposal.operationId.toOption.map(OperationId.apply)
                  )
                  .onComplete {
                    case Success(_) =>
                      self.props.history.goBack()
                      self.setState(_.copy(errorMessage = Seq.empty))
                    case Failure(BadRequestHttpException(errors)) =>
                      self.setState(_.copy(errorMessage = errors.map(_.message.getOrElse(""))))
                    case Failure(_) =>
                      self.setState(_.copy(errorMessage = Seq(Main.defaultErrorMessage)))
                  }
            }

            def handleNextProposal: SyntheticEvent => Unit = {
              event =>
                event.preventDefault()
                val mayBeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                val futureNextProposal =
                  for {
                    _ <- ProposalService.validateProposal(
                      proposalId = self.props.wrapped.proposal.id,
                      newContent = mayBeNewContent,
                      sendNotificationEmail = self.state.notifyUser,
                      labels = self.state.labels,
                      theme = self.state.theme,
                      similarProposals = self.state.similarProposals.map(ProposalId.apply),
                      tags = self.state.tags.map(tag => TagId(tag.id)),
                      ideaId = self.state.ideaId,
                      operationId = self.props.wrapped.proposal.operationId.toOption.map(OperationId.apply)
                    )
                    nextProposal <- ProposalService
                      .nexProposalToModerate(
                        self.props.wrapped.proposal.operationId.toOption,
                        self.props.wrapped.proposal.themeId.toOption,
                        Some(self.props.wrapped.proposal.country),
                        Some(self.props.wrapped.proposal.language)
                      )
                  } yield nextProposal
                futureNextProposal.onComplete {
                  case Success(proposalResponse) =>
                    self.props.history.push(s"/nextProposal/${proposalResponse.data.id}")
                  case Failure(NotFoundHttpException) => self.props.history.push("/proposals")
                  case Failure(BadRequestHttpException(errors)) =>
                    self.setState(_.copy(errorMessage = errors.map(_.message.getOrElse(""))))
                  case Failure(_) =>
                    self.setState(_.copy(errorMessage = Seq(Main.defaultErrorMessage)))
                }
            }

            def handleSubmit: SyntheticEvent => Unit = {
              if (self.props.wrapped.action == "validate" &&
                  self.props.wrapped.context == ShowProposalComponents.Context.StartModeration)
                handleNextProposal
              else if (self.props.wrapped.action == "validate" &&
                       self.props.wrapped.context == ShowProposalComponents.Context.List)
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

            val tagsGroupByTagType: Map[TagType, Seq[Tag]] = {
              self.state.tagsList
                .groupBy[String](_.tagTypeId)
                .flatMap {
                  case (tagTypeId, tags) => self.state.tagTypes.find(_.tagTypeId == tagTypeId).map(_ -> tags)
                }
                .toSeq
                .sortBy {
                  case (tagType, _) => tagType.weight
                }
                .reverse
                .toMap
            }

            val selectTags = <.SelectField(
              ^.disabled := self.state.theme.isEmpty && self.state.operation.isEmpty,
              ^.multiple := true,
              ^.floatingLabelText := "Tags",
              ^.floatingLabelFixed := true,
              ^.valueSelect := self.state.tags.map(_.label),
              ^.onChangeMultipleSelect := handleTagChange,
              ^.fullWidth := true
            )(tagsGroupByTagType.map {
              case (tagType, tags) =>
                Seq(
                  <.MenuItem(
                    ^.key := tagType.tagTypeId,
                    ^.insetChildren := false,
                    ^.checked := false,
                    ^.value := tagType.label,
                    ^.primaryText := tagType.label
                  )(),
                  tags.sortWith(_.weight > _.weight).map { tag =>
                    <.MenuItem(
                      ^.key := tag.id,
                      ^.insetChildren := true,
                      ^.checked := self.state.tags.contains(tag),
                      ^.value := tag.label,
                      ^.primaryText := tag.label
                    )()
                  }
                )
            })

            val errorMessage: Seq[Element] =
              self.state.errorMessage.map(msg => <.p()(msg))

            def setProposalIdea(ideaId: Option[IdeaId]): Unit = {
              self.setState(_.copy(ideaId = ideaId))
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
                <.TextFieldMaterialUi(
                  ^.floatingLabelText := "Language",
                  ^.value := self.props.wrapped.proposal.language,
                  ^.disabled := true
                )(),
                <.TextFieldMaterialUi(
                  ^.floatingLabelText := "Country",
                  ^.value := self.props.wrapped.proposal.country,
                  ^.disabled := true
                )(),
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

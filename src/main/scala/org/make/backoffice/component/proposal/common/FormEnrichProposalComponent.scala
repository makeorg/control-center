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
import org.make.backoffice.component.proposal.common.ProposalIdeaComponent.ProposalIdeaProps
import org.make.backoffice.component.{Main, RichVirtualDOMElements}
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model._
import org.make.backoffice.service.idea.IdeaService
import org.make.backoffice.service.proposal.{Pending, ProposalService}
import org.make.backoffice.service.tag.{TagService, TagTypeService}
import org.make.backoffice.util.Configuration
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object FormEnrichProposalComponent {

  case class FormEnrichProposalProps(proposal: SingleProposal,
                                     minVotesCount: Option[String],
                                     toEnrichMinScore: Option[String],
                                     action: String,
                                     isLocked: Boolean = false,
                                     context: ShowProposalComponents.Context)
  case class FormEnrichProposalState(content: String,
                                     maxLength: Int,
                                     labels: Seq[String] = Seq.empty,
                                     notifyUser: Boolean = true,
                                     operation: Option[Operation] = None,
                                     tagTypes: Seq[TagType] = Seq.empty,
                                     tags: Seq[Tag] = Seq.empty,
                                     tagsList: Seq[Tag] = Seq.empty,
                                     errorMessage: Seq[String] = Seq.empty,
                                     similarProposals: Seq[String] = Seq.empty,
                                     ideaId: Option[IdeaId] = None,
                                     ideaName: String = "",
                                     isLocked: Boolean = false)

  def setTagsFromTagIds(self: Self[FormEnrichProposalProps, FormEnrichProposalState],
                        props: FormEnrichProposalProps): Unit = {
    if (!js.isUndefined(props.proposal.tagIds)) {
      props.proposal.tagIds.foreach { tagId =>
        TagService.tags(questionId = props.proposal.questionId.toOption).onComplete {
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

  def setTagsList(self: Self[FormEnrichProposalProps, FormEnrichProposalState],
                  props: FormEnrichProposalProps): Unit = {
    props.proposal.questionId.toOption.foreach { questionIdValue =>
      val futureTags = for {
        tagTypes <- TagTypeService.tagTypes
        tags     <- TagService.tags(questionId = Some(questionIdValue))
      } yield (tagTypes, tags)
      futureTags.onComplete {
        case Success((tagTypes, tags)) =>
          self.setState(_.copy(tagsList = tags, tagTypes = tagTypes))
        case Success(_) => self.setState(_.copy(tagsList = Seq.empty))
        case Failure(e) => js.Dynamic.global.console.log(s"File with error: $e")
      }
    }
  }

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[FormEnrichProposalProps, FormEnrichProposalState](
          displayName = "FormEnrichProposalComponent",
          getInitialState = { self =>
            FormEnrichProposalState(
              content = self.props.wrapped.proposal.content,
              maxLength =
                Configuration.businessConfig.map(_.proposalMaxLength).getOrElse(Configuration.defaultProposalMaxLength),
              labels = self.props.wrapped.proposal.labels,
              isLocked = self.props.wrapped.isLocked,
              similarProposals =
                self.props.wrapped.proposal.similarProposals.map(_.toSeq.map(_.value)).getOrElse(Seq.empty)
            )
          },
          componentDidMount = self => {
            setTagsFromTagIds(self, self.props.wrapped)
            setTagsList(self, self.props.wrapped)
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
                isLocked = props.wrapped.isLocked,
                similarProposals =
                  props.wrapped.proposal.similarProposals.toOption.map(_.toSeq.map(_.value)).getOrElse(Seq.empty)
              )
            )
            if (self.props.wrapped.proposal.id != props.wrapped.proposal.id) {
              self.setState(_.copy(content = props.wrapped.proposal.content, tags = Seq.empty))
            }
            setTagsFromTagIds(self, props.wrapped)
            setTagsList(self, props.wrapped)
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
                    tags = self.state.tags.map(tag => TagId(tag.id)),
                    similarProposals = self.state.similarProposals.map(ProposalId.apply),
                    ideaId = self.state.ideaId,
                    questionId = self.props.wrapped.proposal.questionId.toOption.map(QuestionId.apply)
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
                val maybeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                ProposalService
                  .validateProposal(
                    proposalId = self.props.wrapped.proposal.id,
                    newContent = maybeNewContent,
                    sendNotificationEmail = self.state.notifyUser,
                    questionId = self.props.wrapped.proposal.questionId.toOption.map(QuestionId.apply),
                    tags = self.state.tags.map(tag => TagId(tag.id)),
                    ideaId = self.state.ideaId
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
                val minVotesCount = self.props.wrapped.minVotesCount.getOrElse(Configuration.toEnrichMinVotesCount)
                val minScore = self.props.wrapped.toEnrichMinScore.getOrElse(Configuration.toEnrichMinScore)
                val mayBeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                val futureNextProposal =
                  for {
                    _ <- ProposalService.updateProposal(
                      proposalId = self.props.wrapped.proposal.id,
                      newContent = mayBeNewContent,
                      labels = self.state.labels,
                      tags = self.state.tags.map(tag => TagId(tag.id)),
                      similarProposals = self.state.similarProposals.map(ProposalId.apply),
                      ideaId = self.state.ideaId,
                      questionId = self.props.wrapped.proposal.questionId.toOption.map(QuestionId.apply)
                    )
                    nextProposal <- ProposalService
                      .nextProposalToModerate(
                        self.props.wrapped.proposal.questionId.toOption,
                        toEnrich = true,
                        minVotesCount = Some(minVotesCount),
                        minScore = Some(minScore)
                      )
                  } yield nextProposal
                futureNextProposal.onComplete {
                  case Success(proposalResponse) =>
                    self.props.history.push(
                      s"/nextProposal/${proposalResponse.data.id}?minVotesCount=$minVotesCount&minScore=$minScore"
                    )
                  case Failure(NotFoundHttpException) => self.props.history.push("/proposals")
                  case Failure(BadRequestHttpException(errors)) =>
                    self.setState(_.copy(errorMessage = errors.map(_.message.getOrElse(""))))
                  case Failure(_) =>
                    self.setState(_.copy(errorMessage = Seq(Main.defaultErrorMessage)))
                }
            }

            def handleSubmit: SyntheticEvent => Unit = {
              if (self.props.wrapped.action == "enrich" &&
                  self.props.wrapped.context == ShowProposalComponents.Context.StartModeration) {
                handleNextProposal
              } else if (self.props.wrapped.proposal.status == Pending.shortName) {
                handleSubmitValidate
              } else {
                handleSubmitUpdate
              }
            }

            val tagsGroupByTagType: Seq[(TagType, Seq[Tag])] = {
              self.state.tagsList
                .groupBy[String](_.tagTypeId)
                .flatMap {
                  case (tagTypeId, tags) => self.state.tagTypes.find(_.id == tagTypeId).map(_ -> tags)
                }
                .toSeq
                .sortBy {
                  case (tagType, _) => tagType.weight * -1
                }
            }

            val selectTags = <.SelectField(
              ^.disabled := self.state.tagsList.isEmpty,
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
                    ^.key := tagType.id,
                    ^.insetChildren := false,
                    ^.checked := false,
                    ^.value := tagType.label,
                    ^.primaryText := tagType.label
                  )(),
                  tags.sortBy(_.weight * -1).map { tag =>
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

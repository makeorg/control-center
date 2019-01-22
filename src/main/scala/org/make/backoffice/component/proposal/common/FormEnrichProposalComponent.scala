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
import org.make.backoffice.facade.AdminOnRest.Fields.FieldsVirtualDOMElements
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model._
import org.make.backoffice.service.idea.IdeaService
import org.make.backoffice.service.proposal.{Pending, ProposalService}
import org.make.backoffice.service.tag.TagTypeService
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
                                     notifyUser: Boolean = true,
                                     operation: Option[Operation] = None,
                                     tagTypes: Seq[TagType] = Seq.empty,
                                     selectedTags: Seq[TagId] = Seq.empty,
                                     tagsList: Seq[PredictedTag] = Seq.empty,
                                     predictedTagsModelName: Option[String] = None,
                                     errorMessage: Seq[String] = Seq.empty,
                                     ideaId: Option[IdeaId] = None,
                                     ideaName: String = "",
                                     isLocked: Boolean = false,
                                     tagListLoaded: Boolean = false)

  def setTags(self: Self[FormEnrichProposalProps, FormEnrichProposalState], props: FormEnrichProposalProps): Unit = {
    val futureTags = for {
      tagTypes      <- TagTypeService.tagTypes
      predictedTags <- ProposalService.getProposalTags(proposalId = props.proposal.id)
    } yield (tagTypes, predictedTags)

    futureTags.onComplete {
      case Success((tagTypes, predictedTags)) =>
        self.setState(
          _.copy(
            tagsList = predictedTags.tags,
            predictedTagsModelName = predictedTags.modelName.toOption,
            tagTypes = tagTypes,
            tagListLoaded = true,
            selectedTags = predictedTags.tags.filter(_.checked).map(tag => TagId(tag.id))
          )
        )
      case Failure(e) => js.Dynamic.global.console.log(s"Error: $e")
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
              maxLength = Configuration.proposalMaxLength,
              isLocked = self.props.wrapped.isLocked,
            )
          },
          componentDidMount = self => {
            setTags(self, self.props.wrapped)
            self.props.wrapped.proposal.ideaId.toOption.foreach { ideaId =>
              IdeaService.getIdea(ideaId).onComplete {
                case Success(response) =>
                  self.setState(_.copy(ideaId = Some(IdeaId(response.data.id)), ideaName = response.data.name))
                case Failure(e) => js.Dynamic.global.console.log(e.getMessage)
              }
            }
          },
          componentWillReceiveProps = { (self, props) =>
            self.setState(_.copy(isLocked = props.wrapped.isLocked))
            if (self.props.wrapped.proposal.id != props.wrapped.proposal.id) {
              self.setState(_.copy(content = props.wrapped.proposal.content, selectedTags = Seq.empty))
            }
            setTags(self, props.wrapped)
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

            def handleTagChange: (FormSyntheticEvent[HTMLInputElement], Boolean) => Unit = {
              (event, _) =>
                val tagId: String = event.target.value

                val newSelectedTags: Seq[TagId] = {
                  if (self.state.selectedTags.map(_.value).contains(tagId)) {
                    self.state.selectedTags.filter(_.value != tagId)
                  } else {
                    self.state.tagsList.find(_.id == tagId) match {
                      case Some(predictedTag) => self.state.selectedTags :+ TagId(predictedTag.id)
                      case _                  => self.state.selectedTags
                    }
                  }
                }

                self.setState(_.copy(selectedTags = newSelectedTags))
            }

            def handleNotifyUserChange: (js.Object, Boolean) => Unit = { (_, checked) =>
              self.setState(_.copy(notifyUser = checked))
            }

            def handleSubmitUpdate: SyntheticEvent => Unit = {
              event =>
                event.preventDefault()
                val mayBeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                val predictedTags = self.state.tagsList.filter(_.predicted).map(tag => TagId(tag.id))
                val predictedTagsParam = if (predictedTags.isEmpty) None else Some(predictedTags)
                ProposalService
                  .updateProposal(
                    proposalId = self.props.wrapped.proposal.id,
                    newContent = mayBeNewContent,
                    tags = self.state.selectedTags,
                    ideaId = self.state.ideaId,
                    questionId = self.props.wrapped.proposal.questionId.toOption.map(QuestionId.apply),
                    predictedTags = predictedTagsParam,
                    modelName = self.state.predictedTagsModelName
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
                val predictedTags = self.state.tagsList.filter(_.predicted).map(tag => TagId(tag.id))
                val predictedTagsParam = if (predictedTags.isEmpty) None else Some(predictedTags)
                ProposalService
                  .validateProposal(
                    proposalId = self.props.wrapped.proposal.id,
                    newContent = maybeNewContent,
                    sendNotificationEmail = self.state.notifyUser,
                    questionId = self.props.wrapped.proposal.questionId.toOption.map(QuestionId.apply),
                    tags = self.state.selectedTags,
                    ideaId = self.state.ideaId,
                    predictedTags = predictedTagsParam,
                    modelName = self.state.predictedTagsModelName
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
                val predictedTags = self.state.tagsList.filter(_.predicted).map(tag => TagId(tag.id))
                val predictedTagsParam = if (predictedTags.isEmpty) None else Some(predictedTags)
                val futureNextProposal =
                  for {
                    _ <- ProposalService.updateProposal(
                      proposalId = self.props.wrapped.proposal.id,
                      newContent = mayBeNewContent,
                      tags = self.state.selectedTags,
                      ideaId = self.state.ideaId,
                      questionId = self.props.wrapped.proposal.questionId.toOption.map(QuestionId.apply),
                      predictedTags = predictedTagsParam,
                      modelName = self.state.predictedTagsModelName
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

            val groupedTagsWithTagType: Map[String, (Option[TagType], Seq[PredictedTag])] =
              self.state.tagsList.groupBy[String](_.tagTypeId).map {
                case (tagTypeId, tags) => (tagTypeId, (self.state.tagTypes.find(_.id == tagTypeId), tags))
              }
            val groupedTagsWithTagTypeOrdered: Seq[(String, (Option[TagType], Seq[PredictedTag]))] =
              groupedTagsWithTagType.toSeq.sortBy {
                case (_, (tagType, _)) => -1 * tagType.map(_.weight).getOrElse(2000.toFloat)
              }

            val checkboxTags: Seq[Element] = groupedTagsWithTagTypeOrdered.map {
              case (_, (maybeTagType, tags)) =>
                <.div(^.style := Map("maxWidth" -> "25em"))(
                  Seq(<.FieldTitle(^.label := maybeTagType.map(_.label).getOrElse("None"))(), tags.map { tag =>
                    <.Checkbox(
                      ^.key := tag.id,
                      ^.checked := self.state.selectedTags.map(_.value).contains(tag.id),
                      ^.value := tag.id,
                      ^.label := tag.label,
                      ^.onCheck := handleTagChange
                    )()
                  })
                )
            }

            val errorMessage: Seq[Element] =
              self.state.errorMessage.map(msg => <.p()(msg))

            def setProposalIdea(ideaId: Option[IdeaId]): Unit = {
              self.setState(_.copy(ideaId = ideaId))
            }

            <.Card(^.style := Map("marginTop" -> "1em"))(
              <.CardTitle(^.title := s"I want to ${self.props.wrapped.action} this proposal")(),
              <.CardActions()(
                <.Card(^.style := Map("marginTop" -> "1em"))(
                  <.CardActions()(
                    <.TextFieldMaterialUi(
                      ^.floatingLabelText := "Proposal content",
                      ^.value := self.state.content,
                      ^.onChange := handleContentEdition,
                      ^.fullWidth := true
                    )(),
                    <.span()(s"${self.state.content.length}/${self.state.maxLength}"),
                  )
                ),
                <.Card(^.style := Map("marginTop" -> "1em"))(
                  <.CardTitle(^.title := "Tags")(),
                  <.CardActions()(<.div(^.style := Map("display" -> "flex"))(if (self.state.tagListLoaded) {
                    checkboxTags
                  } else {
                    <.CircularProgress()()
                  }))
                ),
                <.ProposalIdeaComponent(
                  ^.wrapped := ProposalIdeaProps(self.props.wrapped.proposal, setProposalIdea, self.state.ideaName)
                )(),
                <.Card(^.style := Map("marginTop" -> "1em"))(
                  <.CardActions()(
                    <.Checkbox(
                      ^.disabled := self.props.wrapped.action == "update",
                      ^.label := "Notify user",
                      ^.checked := self.state.notifyUser && self.props.wrapped.action == "validate",
                      ^.onCheck := handleNotifyUserChange,
                      ^.style := Map("maxWidth" -> "25em")
                    )()
                  )
                ),
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

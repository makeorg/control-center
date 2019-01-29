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
import org.make.backoffice.component.Main
import org.make.backoffice.facade.AdminOnRest.Fields.FieldsVirtualDOMElements
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model._
import org.make.backoffice.service.proposal.ProposalService
import org.make.backoffice.service.tag.TagTypeService
import org.make.backoffice.util.Configuration
import org.scalajs.dom.raw.HTMLInputElement
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object FormValidateProposalWithTagsComponent {

  case class FormValidateProposalWithTagsProps(proposal: SingleProposal,
                                               isLocked: Boolean = false,
                                               context: ShowProposalComponents.Context)
  case class FormValidateProposalWithTagsState(content: String,
                                               maxLength: Int,
                                               notifyUser: Boolean = true,
                                               tagTypes: Seq[TagType] = Seq.empty,
                                               selectedTags: Seq[TagId] = Seq.empty,
                                               tagsList: Seq[PredictedTag] = Seq.empty,
                                               predictedTagsModelName: Option[String] = None,
                                               errorMessage: Seq[String] = Seq.empty,
                                               isLocked: Boolean = false,
                                               tagListLoaded: Boolean = false)

  def setTags(self: Self[FormValidateProposalWithTagsProps, FormValidateProposalWithTagsState],
              props: FormValidateProposalWithTagsProps): Unit = {
    val futureTags = for {
      tagTypes <- TagTypeService.tagTypes
      tags     <- ProposalService.getProposalTags(proposalId = props.proposal.id)
    } yield (tagTypes, tags)

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
        .createClass[FormValidateProposalWithTagsProps, FormValidateProposalWithTagsState](
          displayName = "FormValidateProposalWithTagsComponent",
          getInitialState = { self =>
            FormValidateProposalWithTagsState(
              content = self.props.wrapped.proposal.content,
              maxLength = Configuration.proposalMaxLength,
              isLocked = self.props.wrapped.isLocked,
            )
          },
          componentDidMount = self => {
            setTags(self, self.props.wrapped)
          },
          componentWillReceiveProps = { (self, props) =>
            self.setState(_.copy(isLocked = props.wrapped.isLocked))
            if (self.props.wrapped.proposal.id != props.wrapped.proposal.id) {
              self.setState(_.copy(content = props.wrapped.proposal.content, selectedTags = Seq.empty))
            }
            setTags(self, props.wrapped)
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
                val mayBeNewContent =
                  if (self.state.content != self.props.wrapped.proposal.content) {
                    Some(self.state.content)
                  } else { None }
                val predictedTags = self.state.tagsList.filter(_.predicted).map(tag => TagId(tag.id))
                val predictedTagsParam = if (predictedTags.isEmpty) None else Some(predictedTags)
                val futureNextProposal =
                  for {
                    _ <- ProposalService.validateProposal(
                      proposalId = self.props.wrapped.proposal.id,
                      newContent = mayBeNewContent,
                      tags = self.state.selectedTags,
                      questionId = self.props.wrapped.proposal.questionId.toOption.map(QuestionId.apply),
                      sendNotificationEmail = self.state.notifyUser,
                      predictedTags = predictedTagsParam,
                      modelName = self.state.predictedTagsModelName
                    )
                    nextProposal <- ProposalService
                      .nextProposalToModerate(
                        self.props.wrapped.proposal.questionId.toOption,
                        toEnrich = false,
                        minVotesCount = None,
                        minScore = None
                      )
                  } yield nextProposal
                futureNextProposal.onComplete {
                  case Success(proposalResponse) =>
                    self.props.history.push(s"/nextProposal/${proposalResponse.data.id}?withTags=true")
                  case Failure(NotFoundHttpException) => self.props.history.push("/proposals")
                  case Failure(BadRequestHttpException(errors)) =>
                    self.setState(_.copy(errorMessage = errors.map(_.message.getOrElse(""))))
                  case Failure(_) =>
                    self.setState(_.copy(errorMessage = Seq(Main.defaultErrorMessage)))
                }
            }

            def handleSubmit: SyntheticEvent => Unit = {
              if (self.props.wrapped.context == ShowProposalComponents.Context.StartModeration) {
                handleNextProposal
              } else {
                handleSubmitValidate
              }
            }

            val groupedTagsWithTagType: Map[Option[TagType], Seq[PredictedTag]] =
              self.state.tagsList.groupBy[String](_.tagTypeId).map {
                case (tagTypeId, tags) => (self.state.tagTypes.find(_.id == tagTypeId), tags)
              }
            val groupedTagsWithTagTypeOrdered: Seq[(Option[TagType], Seq[PredictedTag])] =
              groupedTagsWithTagType.toSeq.sortBy {
                case (tagType, _) => -1 * tagType.map(_.weight).getOrElse(2000.toFloat)
              }.map {
                case (tagType, tags) => (tagType, tags.sortBy(tag => -1 * tag.weight))
              }

            val checkboxTags: Seq[Element] = groupedTagsWithTagTypeOrdered.map {
              case (maybeTagType, tags) =>
                <.div(^.className := FormValidateProposalWithTagsStyles.gridWrapper.htmlClass)(
                  Seq(
                    <.div(^.className := FormValidateProposalWithTagsStyles.gridTitle.htmlClass)(
                      <.FieldTitle(^.label := maybeTagType.map(_.label).getOrElse("None"))()
                    ),
                    tags.map { tag =>
                      <.Checkbox(
                        ^.key := tag.id,
                        ^.checked := self.state.selectedTags.map(_.value).contains(tag.id),
                        ^.value := tag.id,
                        ^.label := tag.label,
                        ^.onCheck := handleTagChange
                      )()
                    }
                  )
                )
            }

            val errorMessage: Seq[Element] =
              self.state.errorMessage.map(msg => <.p()(msg))

            <.Card(^.style := Map("marginTop" -> "1em"))(
              <.CardTitle(^.title := s"I want to validate this proposal")(),
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
                  <.CardActions()(<.div()(if (self.state.tagListLoaded) {
                    checkboxTags
                  } else {
                    <.CircularProgress()()
                  }))
                ),
                <.Card(^.style := Map("marginTop" -> "1em"))(
                  <.CardActions()(
                    <.Checkbox(
                      ^.label := "Notify user",
                      ^.checked := self.state.notifyUser,
                      ^.onCheck := handleNotifyUserChange,
                      ^.style := Map("maxWidth" -> "25em")
                    )()
                  )
                ),
                <.RaisedButton(
                  ^.style := Map("marginTop" -> "1em"),
                  ^.label := s"Confirm validation",
                  ^.onClick := handleSubmit,
                  ^.disabled := self.state.isLocked
                )(),
                errorMessage
              ),
              <.style()(FormValidateProposalWithTagsStyles.render[String])
            )
          }
        )
    )
}

object FormValidateProposalWithTagsStyles extends StyleSheet.Inline {

  import dsl._

  val gridWrapper: StyleA =
    style(
      display.grid,
      margin(10.px),
      gridTemplateColumns := "repeat(5, 1fr)",
      media.maxWidth(720.px)(gridTemplateColumns := "repeat(2, 1fr)")
    )

  val gridTitle: StyleA = style(gridColumn := "1 / 6", gridRow := "1", media.maxWidth(720.px)(gridColumn := "1 / 3"))
}

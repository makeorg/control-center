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

package org.make.backoffice.component.homepage

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, MouseSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.request.Filter
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.{FeaturedOperation, Question}
import org.make.backoffice.service.operation.{CurrentOperationService, UpdateCurrentOperationRequest}
import org.make.backoffice.service.question.QuestionService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object EditCurrentOperation {

  case class EditCurrentOperationProps() extends RouterProps
  case class EditCurrentOperationState(id: String,
                                       questionId: String,
                                       description: String,
                                       label: String,
                                       picture: String,
                                       altPicture: String,
                                       linkLabel: String,
                                       internalLink: Option[String],
                                       externalLink: Option[String],
                                       questionsList: Seq[Question],
                                       internalLinkChecked: Boolean,
                                       externalLinkChecked: Boolean,
                                       snackbarOpen: Boolean,
                                       snackbarMessage: String,
                                       questionError: String,
                                       descriptionError: String,
                                       labelError: String,
                                       pictureError: String,
                                       altPictureError: String,
                                       linkLabelError: String)

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[EditCurrentOperationProps, EditCurrentOperationState](
        displayName = "EditCurrentOperation",
        getInitialState = _ => {
          EditCurrentOperationState(
            id = "",
            questionId = "",
            description = "",
            label = "",
            picture = "",
            altPicture = "",
            linkLabel = "",
            internalLink = None,
            externalLink = None,
            questionsList = Seq.empty,
            internalLinkChecked = false,
            externalLinkChecked = false,
            snackbarOpen = false,
            snackbarMessage = "",
            questionError = "",
            descriptionError = "",
            labelError = "",
            pictureError = "",
            altPictureError = "",
            linkLabelError = ""
          )
        },
        componentWillMount = self => {
          val id = self.props.`match`.params("id")
          CurrentOperationService.getCurrentOperation(id).onComplete {
            case Success(operation) =>
              self.setState(
                _.copy(
                  id = id,
                  questionId = operation.questionId,
                  description = operation.description,
                  label = operation.label,
                  picture = operation.picture,
                  altPicture = operation.altPicture,
                  linkLabel = operation.linkLabel,
                  internalLink = operation.internalLink.toOption,
                  externalLink = operation.externalLink.toOption,
                  internalLinkChecked = operation.internalLink.isDefined,
                  externalLinkChecked = operation.externalLink.isDefined
                )
              )
            case Failure(_) =>
          }
          QuestionService
            .questions(
              filters = Some(Seq(Filter("operationKind", "GREAT_CAUSE,PUBLIC_CONSULTATION,BUSINESS_CONSULTATION")))
            )
            .onComplete {
              case Success(questionsResponse) =>
                self.setState(
                  _.copy(questionsList = questionsResponse.data.filterNot(_.slug.contains("huffpost")).sortBy(_.slug))
                )
              case Failure(e) => js.Dynamic.global.console.log(e.getMessage)
            }
        },
        render = self => {

          def onSelectQuestion: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
            self.setState(_.copy(questionId = value, questionError = ""))
          }

          def onChangeDescription: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            if (value.length > 140) {
              self.setState(
                _.copy(description = value, descriptionError = "Description length must be lower than 140 characters")
              )
            } else {
              self.setState(_.copy(description = value, descriptionError = ""))
            }
          }

          def onChangeLabel: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(label = value, labelError = ""))
          }

          def onChangePicture: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(picture = value, pictureError = ""))
          }

          def onChangeAltPicture: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            if (value.length > 130) {
              self.setState(
                _.copy(
                  altPicture = value,
                  altPictureError = "Alternative text length must be lower than 130 characters"
                )
              )
            } else {
              self.setState(_.copy(altPicture = value, altPictureError = ""))
            }
          }

          def onChangeLinkLabel: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(linkLabel = value, linkLabelError = ""))
          }

          def onSelectInternalLink: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
            self.setState(_.copy(internalLink = Some(value)))
          }

          def onChangeExternalLink: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(externalLink = Some(value)))
          }

          def onToggleInternalLink: MouseSyntheticEvent => Unit = { _ =>
            self.setState(
              _.copy(
                internalLinkChecked = !self.state.internalLinkChecked,
                externalLinkChecked = self.state.internalLinkChecked
              )
            )
          }

          def onToggleExternalLink: MouseSyntheticEvent => Unit = { _ =>
            self.setState(
              _.copy(
                externalLinkChecked = !self.state.externalLinkChecked,
                internalLinkChecked = self.state.externalLinkChecked
              )
            )
          }

          def checkError: Boolean = {
            var error: Boolean = false

            if (self.state.questionId.isEmpty) {
              self.setState(_.copy(questionError = "question must not be empty"))
              error = true
            }
            if (self.state.description.isEmpty) {
              self.setState(_.copy(descriptionError = "description must not be empty"))
              error = true
            }
            if (self.state.label.isEmpty) {
              self.setState(_.copy(labelError = "label must not be empty"))
              error = true
            }
            if (self.state.picture.isEmpty) {
              self.setState(_.copy(pictureError = "picture must not be empty"))
              error = true
            }
            if (self.state.altPicture.isEmpty) {
              self.setState(_.copy(altPictureError = "alternative text must not be empty"))
              error = true
            }
            if (self.state.linkLabel.isEmpty) {
              self.setState(_.copy(linkLabelError = "link label must not be empty"))
              error = true
            }
            error
          }

          def handleUpdate: SyntheticEvent => Unit = {
            _ =>
              if (!checkError) {
                val request = UpdateCurrentOperationRequest(
                  questionId = self.state.questionId,
                  description = self.state.description,
                  label = self.state.label,
                  picture = self.state.picture,
                  altPicture = self.state.altPicture,
                  linkLabel = self.state.linkLabel,
                  internalLink = if (!self.state.internalLinkChecked) None else self.state.internalLink,
                  externalLink = if (!self.state.externalLinkChecked) None else self.state.externalLink
                )
                CurrentOperationService.putCurrentOperation(self.state.id, request).onComplete {
                  case Success(_) =>
                    self
                      .setState(_.copy(snackbarOpen = true, snackbarMessage = "Current operation updated successfully"))
                    self.props.history.push("/homepage")
                  case Failure(_) =>
                    self
                      .setState(_.copy(snackbarOpen = true, snackbarMessage = "Current operation failed to be updated"))
                }
              }
          }

          def onSnackbarClose: String => Unit = _ => {
            self.setState(_.copy(snackbarOpen = false, snackbarMessage = ""))
          }

          <.div()(
            <.Card(^.style := Map("marginTop" -> "30px"))(
              <.CardTitle(^.title := s"Current Operation #${self.state.id}")(),
              <.CardText(^.style := Map("display" -> "grid"))(
                <.SelectField(
                  ^.floatingLabelText := "question",
                  ^.value := self.state.questionId,
                  ^.fullWidth := true,
                  ^.floatingLabelFixed := true,
                  ^.errorText := self.state.questionError,
                  ^.onChangeSelect := onSelectQuestion
                )(self.state.questionsList.map { question =>
                  <.MenuItem(
                    ^.key := question.id,
                    ^.value := question.id,
                    ^.primaryText := s"${question.slug} : ${question.question}"
                  )()
                }),
                <.TextFieldMaterialUi(
                  ^.name := "description",
                  ^.floatingLabelText := "Description",
                  ^.floatingLabelFixed := true,
                  ^.value := self.state.description,
                  ^.fullWidth := true,
                  ^.errorText := self.state.descriptionError,
                  ^.onChange := onChangeDescription
                )(),
                <.TextFieldMaterialUi(
                  ^.name := "label",
                  ^.floatingLabelText := "Label",
                  ^.floatingLabelFixed := true,
                  ^.value := self.state.label,
                  ^.fullWidth := true,
                  ^.errorText := self.state.labelError,
                  ^.onChange := onChangeLabel
                )(),
                <.TextFieldMaterialUi(
                  ^.name := "picture",
                  ^.floatingLabelText := "Picture",
                  ^.floatingLabelFixed := true,
                  ^.value := self.state.picture,
                  ^.fullWidth := true,
                  ^.errorText := self.state.pictureError,
                  ^.onChange := onChangePicture
                )(),
                <.TextFieldMaterialUi(
                  ^.name := "alternativeText",
                  ^.floatingLabelText := "Alternative text",
                  ^.floatingLabelFixed := true,
                  ^.value := self.state.altPicture,
                  ^.fullWidth := true,
                  ^.errorText := self.state.altPictureError,
                  ^.onChange := onChangeAltPicture
                )(),
                <.TextFieldMaterialUi(
                  ^.name := "linkLabel",
                  ^.floatingLabelText := "Link label",
                  ^.floatingLabelFixed := true,
                  ^.value := self.state.linkLabel,
                  ^.fullWidth := true,
                  ^.errorText := self.state.linkLabelError,
                  ^.onChange := onChangeLinkLabel,
                  ^.style := Map("marginBottom" -> "15px")
                )(),
                <.div(^.style := Map("display" -> "flex"))(
                  <.span(^.onClick := onToggleInternalLink)(
                    <.Toggle(
                      ^.label := "Internal link",
                      ^.toggled := self.state.internalLinkChecked,
                      ^.labelPosition := "right",
                      ^.style := Map("width" -> "25%")
                    )()
                  ),
                  <.SelectField(
                    ^.label := "internal link",
                    ^.value := self.state.internalLink.getOrElse(""),
                    ^.disabled := !self.state.internalLinkChecked,
                    ^.fullWidth := true,
                    ^.onChangeSelect := onSelectInternalLink
                  )(FeaturedOperation.internalLinkMap.map {
                    case (id, text) => <.MenuItem(^.key := id, ^.value := id, ^.primaryText := text)()
                  })
                ),
                <.div(^.style := Map("display" -> "flex"))(
                  <.span(^.onClick := onToggleExternalLink)(
                    <.Toggle(
                      ^.label := "External link",
                      ^.toggled := self.state.externalLinkChecked,
                      ^.labelPosition := "right",
                      ^.style := Map("width" -> "25%")
                    )()
                  ),
                  <.TextFieldMaterialUi(
                    ^.name := "external link",
                    ^.value := self.state.externalLink.getOrElse(""),
                    ^.style := Map("width" -> "100%"),
                    ^.onChange := onChangeExternalLink,
                    ^.disabled := !self.state.externalLinkChecked
                  )()
                )
              ),
              <.Toolbar(^.style := Map("backgroundColor" -> "#e8e8e8"))(
                <.ToolbarGroup()(<.RaisedButton(^.primary := true, ^.label := "Save", ^.onClick := handleUpdate)())
              ),
              <.Snackbar(
                ^.open := self.state.snackbarOpen,
                ^.message := self.state.snackbarMessage,
                ^.autoHideDuration := 3000,
                ^.onRequestClose := onSnackbarClose
              )()
            )
          )
        }
      )

}

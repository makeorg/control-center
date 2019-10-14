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
import org.make.backoffice.client.request.Filter
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.facade.ReactDropzone._
import org.make.backoffice.model.{FeaturedOperation, Question}
import org.make.backoffice.service.homepage.{HomepageService, UploadResponse}
import org.make.backoffice.service.operation.{
  CreateFeaturedOperationRequest,
  FeaturedOperationService,
  UpdateFeaturedOperationRequest
}
import org.make.backoffice.service.question.QuestionService
import org.scalajs.dom.FormData
import org.scalajs.dom.raw.HTMLInputElement
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object HeaderComponent {

  case class HeaderComponentProps(featuredOperation: Option[FeaturedOperation],
                                  slot: Int,
                                  questionsList: Seq[Question],
                                  reloadComponent: () => Unit)
  case class HeaderComponentState(id: String,
                                  questionId: Option[String],
                                  title: String,
                                  description: Option[String],
                                  landscapePicture: String,
                                  portraitPicture: String,
                                  picturePreview: String,
                                  altPicture: String,
                                  label: String,
                                  buttonLabel: String,
                                  internalLink: Option[String],
                                  externalLink: Option[String],
                                  slot: Int,
                                  internalLinkChecked: Boolean,
                                  externalLinkChecked: Boolean,
                                  cardExpended: Boolean,
                                  toCreate: Boolean,
                                  questionsList: Seq[Question],
                                  snackbarOpen: Boolean,
                                  snackbarMessage: String,
                                  titleError: String,
                                  descriptionError: String,
                                  altPictureError: String,
                                  labelError: String,
                                  buttonLabelError: String,
                                  landscapePictureFile: Option[ImageFile],
                                  portraitPictureFile: Option[ImageFile])
  case class ImageUrlValidateProps(uploadFile: String => MouseSyntheticEvent => Unit)

  lazy val reactClass: ReactClass =
    React
      .createClass[HeaderComponentProps, HeaderComponentState](
        displayName = "HeaderComponent",
        getInitialState = self => {
          HeaderComponentState(
            id = "",
            questionId = None,
            title = "",
            description = None,
            landscapePicture = "",
            portraitPicture = "",
            picturePreview = "",
            altPicture = "",
            label = "",
            buttonLabel = "",
            internalLink = None,
            externalLink = None,
            slot = self.props.wrapped.slot,
            internalLinkChecked = false,
            externalLinkChecked = false,
            cardExpended = false,
            toCreate = true,
            questionsList = self.props.wrapped.questionsList,
            snackbarOpen = false,
            snackbarMessage = "",
            titleError = "",
            descriptionError = "",
            altPictureError = "",
            labelError = "",
            buttonLabelError = "",
            landscapePictureFile = None,
            portraitPictureFile = None
          )
        },
        componentWillReceiveProps = (self, props) => {
          props.wrapped.featuredOperation.foreach {
            featuredOperation =>
              self.setState(
                _.copy(
                  id = featuredOperation.id,
                  questionId = featuredOperation.questionId.toOption,
                  title = featuredOperation.title,
                  description = featuredOperation.description.toOption,
                  landscapePicture = featuredOperation.landscapePicture,
                  portraitPicture = featuredOperation.portraitPicture,
                  picturePreview = featuredOperation.landscapePicture,
                  altPicture = featuredOperation.altPicture,
                  label = featuredOperation.label,
                  buttonLabel = featuredOperation.buttonLabel,
                  internalLink = featuredOperation.internalLink.toOption,
                  externalLink = featuredOperation.externalLink.toOption,
                  slot = featuredOperation.slot,
                  internalLinkChecked = featuredOperation.internalLink.isDefined,
                  externalLinkChecked = featuredOperation.externalLink.isDefined,
                  cardExpended = true,
                  toCreate = false,
                  questionsList = props.wrapped.questionsList,
                  landscapePictureFile = None,
                  portraitPictureFile = None
                )
              )
          }
          self.setState(_.copy(questionsList = props.wrapped.questionsList))
        },
        render = self => {

          def onExpendChange: Boolean => Unit = { isExpended =>
            self.setState(_.copy(cardExpended = isExpended))
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

          def onChangeLabel: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(label = value, labelError = ""))
          }

          def onChangeTitle: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(title = value, titleError = ""))
          }

          def onChangeDescription: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = if (event.target.value.isEmpty) None else Some(event.target.value)
            if (value.exists(_.length > 140)) {
              self.setState(
                _.copy(description = value, descriptionError = "Description length must be lower than 140 characters")
              )
            } else {
              self.setState(_.copy(description = value, descriptionError = ""))
            }
          }

          def onChangeButtonLabel: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(buttonLabel = value, buttonLabelError = ""))
          }

          def onChangeExternalLink: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = if (event.target.value.isEmpty) None else Some(event.target.value)
            self.setState(_.copy(externalLink = value))
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

          def onSelectInternalLink: (js.Object, js.UndefOr[Int], String) => Unit = {
            (_, _, value) =>
              self.setState(_.copy(internalLink = Some(value), questionId = None))
              if (value == "ACTIONS") {
                QuestionService.questions(None, None, Some(Seq(Filter("operationKind", "GREAT_CAUSE")))).onComplete {
                  case Success(questions) => self.setState(_.copy(questionsList = questions.data))
                  case Failure(e)         => js.Dynamic.global.console.log(e.getMessage)
                }
              } else {
                self.setState(_.copy(questionsList = self.props.wrapped.questionsList))
              }
          }

          def onSelectQuestion: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
            self.setState(_.copy(questionId = Some(value)))
          }

          def checkError: Boolean = {
            var error: Boolean = false

            if (self.state.label.isEmpty) {
              self.setState(_.copy(labelError = "label must not be empty"))
              error = true
            }
            if (self.state.altPicture.isEmpty) {
              self.setState(_.copy(altPictureError = "alternative text must not be empty"))
              error = true
            }
            if (self.state.title.isEmpty) {
              self.setState(_.copy(titleError = "link label must not be empty"))
              error = true
            }
            if (self.state.buttonLabel.isEmpty) {
              self.setState(_.copy(buttonLabelError = "link label must not be empty"))
              error = true
            }
            error
          }

          def uploadImage(file: ImageFile): Future[UploadResponse] = {
            val formDataLandscape: FormData = new FormData()
            formDataLandscape.append("data", file)
            HomepageService.uploadImage(formDataLandscape)
          }

          def handleCreation: SyntheticEvent => Unit = {
            _ =>
              if (!checkError) {
                self.state.landscapePictureFile.map {
                  landscapePictureFile =>
                    self.state.portraitPictureFile.map {
                      portraitPictureFile =>
                        uploadImage(landscapePictureFile).map {
                          landscapePictureUploadResponse =>
                            uploadImage(portraitPictureFile).map {
                              portraitPictureUploadResponse =>
                                val request = CreateFeaturedOperationRequest(
                                  questionId = self.state.questionId,
                                  title = self.state.title,
                                  description = self.state.description,
                                  landscapePicture = landscapePictureUploadResponse.path,
                                  portraitPicture = portraitPictureUploadResponse.path,
                                  altPicture = self.state.altPicture,
                                  label = self.state.label,
                                  buttonLabel = self.state.buttonLabel,
                                  internalLink = if (!self.state.internalLinkChecked) None else self.state.internalLink,
                                  externalLink = if (!self.state.externalLinkChecked) None else self.state.externalLink,
                                  slot = self.state.slot
                                )
                                FeaturedOperationService.postFeaturedOperation(request).onComplete {
                                  case Success(_) =>
                                    self
                                      .setState(
                                        _.copy(
                                          snackbarOpen = true,
                                          snackbarMessage = "Featured operation created successfully",
                                          landscapePictureFile = None,
                                          portraitPictureFile = None
                                        )
                                      )
                                  case Failure(_) =>
                                    self
                                      .setState(
                                        _.copy(
                                          snackbarOpen = true,
                                          snackbarMessage = "featured operation failed to be created"
                                        )
                                      )
                                }
                            }
                        }
                    }.getOrElse(
                      self
                        .setState(_.copy(snackbarOpen = true, snackbarMessage = "mobile picture must not be empty"))
                    )
                }.getOrElse(
                  self
                    .setState(_.copy(snackbarOpen = true, snackbarMessage = "desktop picture must not be empty"))
                )
              }
          }

          def maybeFutureImages: Future[(Option[String], Option[String])] = {
            val maybeFutureLandscapePicture: Option[Future[UploadResponse]] = self.state.landscapePictureFile.map {
              landscapePictureFile =>
                uploadImage(landscapePictureFile)
            }

            val maybeFuturePortraitPicture: Option[Future[UploadResponse]] = self.state.portraitPictureFile.map {
              portraitPictureFile =>
                uploadImage(portraitPictureFile)
            }

            (maybeFutureLandscapePicture, maybeFuturePortraitPicture) match {
              case (None, None)                       => Future.successful((None, None))
              case (Some(landscapePicturePath), None) => landscapePicturePath.map(path => (Some(path.path), None))
              case (None, Some(portraitPicturePath))  => portraitPicturePath.map(path => (None, Some(path.path)))
              case (Some(landscapePicturePath), Some(portraitPicturePath)) =>
                landscapePicturePath.flatMap(
                  landscapePath =>
                    portraitPicturePath.map(portraitPath => (Some(landscapePath.path), Some(portraitPath.path)))
                )
            }
          }

          def handleUpdate: SyntheticEvent => Unit = {
            _ =>
              if (!checkError) {
                maybeFutureImages.map {
                  case (maybeLandscapePicturePath, maybePortraitPicturePath) =>
                    val request = UpdateFeaturedOperationRequest(
                      questionId = self.state.questionId,
                      title = self.state.title,
                      description = self.state.description,
                      landscapePicture = maybeLandscapePicturePath.getOrElse(self.state.landscapePicture),
                      portraitPicture = maybePortraitPicturePath.getOrElse(self.state.portraitPicture),
                      altPicture = self.state.altPicture,
                      label = self.state.label,
                      buttonLabel = self.state.buttonLabel,
                      internalLink = if (!self.state.internalLinkChecked) None else self.state.internalLink,
                      externalLink = if (!self.state.externalLinkChecked) None else self.state.externalLink,
                      slot = self.state.slot
                    )
                    FeaturedOperationService.putFeaturedOperation(self.state.id, request).onComplete {
                      case Success(_) =>
                        self
                          .setState(
                            _.copy(
                              snackbarOpen = true,
                              snackbarMessage = "Featured operation updated successfully",
                              landscapePictureFile = None,
                              portraitPictureFile = None
                            )
                          )
                      case Failure(_) =>
                        self
                          .setState(
                            _.copy(snackbarOpen = true, snackbarMessage = "featured operation failed to be updated")
                          )
                    }
                }
              }
          }

          def handleDelete: SyntheticEvent => Unit = {
            _ =>
              FeaturedOperationService.deleteFeaturedOperation(self.state.id).onComplete {
                case Success(_) =>
                  self.setState(
                    _.copy(
                      id = "",
                      questionId = None,
                      title = "",
                      description = None,
                      landscapePicture = "",
                      portraitPicture = "",
                      picturePreview = "",
                      altPicture = "",
                      label = "",
                      buttonLabel = "",
                      internalLink = None,
                      externalLink = None,
                      slot = self.props.wrapped.slot,
                      internalLinkChecked = false,
                      externalLinkChecked = false,
                      cardExpended = false,
                      toCreate = true,
                      snackbarOpen = true,
                      snackbarMessage = "Featured operation deleted successfully",
                      landscapePictureFile = None,
                      portraitPictureFile = None
                    )
                  )
                  self.props.wrapped.reloadComponent()
                case Failure(_) =>
                  self
                    .setState(_.copy(snackbarOpen = true, snackbarMessage = "featured operation failed to be deleted"))
              }
          }

          def onSnackbarClose: String => Unit = _ => {
            self.setState(_.copy(snackbarOpen = false, snackbarMessage = ""))
          }

          def setLandscapePicture: js.Array[ImageFile] => Unit = { imgFile =>
            self.setState(_.copy(picturePreview = imgFile(0).preview, landscapePictureFile = Some(imgFile(0))))
          }

          def setPortraitPicture: js.Array[ImageFile] => Unit = { imgFile =>
            self.setState(_.copy(portraitPictureFile = Some(imgFile(0))))
          }

          <.div()(
            <.Card(
              ^.style := Map("marginTop" -> "30px"),
              ^.expanded := self.state.cardExpended,
              ^.onExpandChange := onExpendChange
            )(
              <.CardTitle(
                ^.title := s"Slot ${self.state.slot}",
                ^.actAsExpander := true,
                ^.showExpandableButton := true
              )(),
              <.CardText(^.style := Map("display" -> "grid"), ^.expandable := true)(
                <.h3(^.style := Map("color" -> "blue"))("Image"),
                <.div(^.style := Map("display" -> "flex"))(
                  <.div(^.style := Map("width" -> "70%"))(
                    <.TextFieldMaterialUi(
                      ^.name := "Alternative text",
                      ^.floatingLabelText := "Alternative text",
                      ^.floatingLabelFixed := true,
                      ^.value := self.state.altPicture,
                      ^.onChange := onChangeAltPicture,
                      ^.errorText := self.state.altPictureError,
                      ^.style := Map("width" -> "90%")
                    )(),
                    <.Dropzone(
                      ^.multiple := false,
                      ^.className := HeaderComponentStyles.dropzone.htmlClass,
                      ^.onDropDropzone := setLandscapePicture
                    )("Desktop picture"),
                    <.Dropzone(
                      ^.multiple := false,
                      ^.className := HeaderComponentStyles.dropzone.htmlClass,
                      ^.onDropDropzone := setPortraitPicture
                    )("Mobile picture")
                  ),
                  <.div(^.style := Map("width" -> "30%"))(
                    <.img(
                      ^.src := self.state.landscapePicture,
                      ^.style := Map("maxWidth" -> "100%", "height" -> "auto")
                    )()
                  )
                ),
                <.h3(^.style := Map("color" -> "blue"))("Label"),
                <.TextFieldMaterialUi(
                  ^.name := "label",
                  ^.value := self.state.label,
                  ^.onChange := onChangeLabel,
                  ^.errorText := self.state.labelError,
                  ^.fullWidth := true
                )(),
                <.h3(^.style := Map("color" -> "blue"))("Title"),
                <.TextFieldMaterialUi(
                  ^.name := "title",
                  ^.value := self.state.title,
                  ^.onChange := onChangeTitle,
                  ^.errorText := self.state.titleError,
                  ^.fullWidth := true
                )(),
                <.h3(^.style := Map("color" -> "blue"))("Description"),
                <.TextFieldMaterialUi(
                  ^.name := "description",
                  ^.value := self.state.description.getOrElse(""),
                  ^.onChange := onChangeDescription,
                  ^.errorText := self.state.descriptionError,
                  ^.fullWidth := true
                )(),
                <.h3(^.style := Map("color" -> "blue"))("Button text"),
                <.TextFieldMaterialUi(
                  ^.name := "button text",
                  ^.value := self.state.buttonLabel,
                  ^.errorText := self.state.buttonLabelError,
                  ^.onChange := onChangeButtonLabel
                )(),
                <.h3(^.style := Map("color" -> "blue"))("Link"),
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
                    ^.floatingLabelText := "Internal link",
                    ^.floatingLabelFixed := true,
                    ^.style := Map("width" -> "50%"),
                    ^.value := self.state.internalLink.getOrElse(""),
                    ^.disabled := !self.state.internalLinkChecked,
                    ^.onChangeSelect := onSelectInternalLink
                  )(FeaturedOperation.internalLinkMap.map {
                    case (id, text) => <.MenuItem(^.key := id, ^.value := id, ^.primaryText := text)()
                  }),
                  <.SelectField(
                    ^.style := Map("width" -> "50%"),
                    ^.label := "question",
                    ^.floatingLabelText := "Question",
                    ^.floatingLabelFixed := true,
                    ^.value := self.state.questionId.getOrElse(""),
                    ^.disabled := !self.state.internalLinkChecked,
                    ^.onChangeSelect := onSelectQuestion
                  )(self.state.questionsList.map { question =>
                    <.MenuItem(
                      ^.key := question.id,
                      ^.value := question.id,
                      ^.primaryText := s"${question.slug} : ${question.question}"
                    )()
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
                ),
                <.CardActions()(if (self.state.toCreate) {
                  <.RaisedButton(
                    ^.label := "Save",
                    ^.primary := true,
                    ^.style := Map("float" -> "right"),
                    ^.onClick := handleCreation
                  )()
                } else {
                  <.RaisedButton(
                    ^.label := "Update",
                    ^.primary := true,
                    ^.style := Map("float" -> "right"),
                    ^.onClick := handleUpdate
                  )()
                }, <.RaisedButton(^.label := "Delete", ^.secondary := true, ^.style := Map("float" -> "right"), ^.onClick := handleDelete)())
              )
            ),
            <.Snackbar(
              ^.open := self.state.snackbarOpen,
              ^.message := self.state.snackbarMessage,
              ^.autoHideDuration := 3000,
              ^.onRequestClose := onSnackbarClose
            )(),
            <.style()(HeaderComponentStyles.render[String])
          )
        }
      )

}

object HeaderComponentStyles extends StyleSheet.Inline {

  import dsl._

  val dropzone: StyleA =
    style(
      background := "#efefef",
      cursor.pointer,
      padding(1.rem),
      marginTop(1.rem),
      textAlign.center,
      color(rgb(9, 9, 9)),
      width(90.%%)
    )

}

@js.native
trait ImageFile extends js.Object {
  val name: String
  val preview: String
}

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

package org.make.backoffice.component.question

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.backoffice.client.{BadRequestHttpException, Resource}
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.FormTab._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SaveButton._
import org.make.backoffice.facade.AdminOnRest.TabbedForm._
import org.make.backoffice.facade.AdminOnRest.required
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.Question
import org.make.backoffice.service.question.QuestionService
import org.make.backoffice.util.DateParser
import org.scalajs.dom._
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object EditQuestion {

  case class EditQuestionProps() extends RouterProps
  case class EditQuestionState(fileConsultationImage: Option[File],
                               fileDescriptionImage: Option[File],
                               snackbarOpen: Boolean,
                               errorMessage: String)
  case class ImageUrlValidateProps(uploadFile: String => MouseSyntheticEvent => Unit)

  def apply(): ReactClass = reactClass

  lazy val consultationImagePreview: ReactClass = React.createClass[Unit, Unit](render = self => {
    val question: Question = self.props.native.record.asInstanceOf[Question]

    question.consultationImage.toOption match {
      case Some(imgUrl) if imgUrl != null =>
        <.div(^.className := EditQuestionStyles.imgPreview.htmlClass)(
          <.label()("Consultation Image"),
          <.br.empty,
          <.img(^.src := imgUrl, ^.title := "question consultationImage", ^.width := 150)()
        )
      case _ =>
        <.div(^.className := EditQuestionStyles.imgPreview.htmlClass)(<.label()("Consultation Image"))
    }
  })

  lazy val descriptionImagePreview: ReactClass = React.createClass[Unit, Unit](render = self => {
    val question: Question = self.props.native.record.asInstanceOf[Question]

    question.descriptionImage.toOption match {
      case Some(imgUrl) if imgUrl != null =>
        <.div(^.className := EditQuestionStyles.imgPreview.htmlClass)(
          <.label()("Description Image"),
          <.br.empty,
          <.img(^.src := imgUrl, ^.title := "question descriptionImage", ^.width := 150)()
        )
      case _ =>
        <.div(^.className := EditQuestionStyles.imgPreview.htmlClass)(<.label()("Description Image"))
    }
  })

  lazy val imageUrlValidate: ReactClass = React.createClass[ImageUrlValidateProps, Unit](render = self => {
    val question: Question = self.props.native.record.asInstanceOf[Question]

    <.RaisedButton(
      ^.className := EditQuestionStyles.validateImage.htmlClass,
      ^.onClick := self.props.wrapped.uploadFile(question.id),
      ^.label := "Upload Image",
      ^.secondary := true
    )()

  })

  private lazy val reactClass =
    React
      .createClass[EditQuestionProps, EditQuestionState](
        displayName = "EditQuestion",
        getInitialState = _ =>
          EditQuestionState(
            fileConsultationImage = None,
            fileDescriptionImage = None,
            snackbarOpen = false,
            errorMessage = ""
        ),
        render = self => {
          def getFileConsultationImage: ImageFileWrapper => Unit = { imgFile =>
            self.setState(_.copy(fileConsultationImage = Some(imgFile.`0`.rawFile)))
          }

          def getFileDescriptionImage: ImageFileWrapper => Unit = { imgFile =>
            self.setState(_.copy(fileDescriptionImage = Some(imgFile.`0`.rawFile)))
          }

          def handleConsultationImage(imgFile: File): String => MouseSyntheticEvent => Unit = {
            questionId => _ =>
              val formData = new FormData()
              formData.append("data", imgFile)
              QuestionService
                .uploadConsultationImage(questionId, formData = formData)
                .onComplete {
                  case Success(_) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "Image uploaded successfully"))
                  case Failure(BadRequestHttpException(_)) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "Bad request"))
                  case Failure(_) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "Internal Error"))
                }
          }

          def handleDescriptionImage(imgFile: File): String => MouseSyntheticEvent => Unit = {
            questionId => _ =>
              val formData = new FormData()
              formData.append("data", imgFile)
              QuestionService
                .uploadDescriptionImage(questionId, formData = formData)
                .onComplete {
                  case Success(_) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "Image uploaded successfully"))
                  case Failure(BadRequestHttpException(_)) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "Bad request"))
                  case Failure(_) =>
                    self.setState(_.copy(snackbarOpen = true, errorMessage = "Internal Error"))
                }
          }

          <.div()(
            <.Edit(
              ^.resource := Resource.operationsOfQuestions,
              ^.location := self.props.location,
              ^.`match` := self.props.`match`,
              ^.hasList := true
            )(
              <.TabbedForm(^.submitOnEnter := false)(
                <.FormTab(^.label := "infos")(
                  <.DateTimeInput(
                    ^.label := "Start Date",
                    ^.labelTime := "Start Time",
                    ^.translateLabel := ((label: String) => label),
                    ^.source := "startDate",
                    ^.parse := ((date: js.UndefOr[js.Date]) => date.map(DateParser.parseDate))
                  )(),
                  <.DateTimeInput(
                    ^.label := "End Date",
                    ^.labelTime := "End Time",
                    ^.translateLabel := ((label: String) => label),
                    ^.source := "endDate",
                    ^.parse := ((date: js.UndefOr[js.Date]) => date.map(DateParser.parseDate))
                  )(),
                  <.ReferenceField(
                    ^.label := "Operation",
                    ^.translateLabel := ((label: String) => label),
                    ^.source := "operationId",
                    ^.reference := Resource.operations,
                    ^.linkType := false
                  )(<.TextField(^.source := "slug")()),
                  <.TextField(
                    ^.label := "Operation Title",
                    ^.translateLabel := ((label: String) => label),
                    ^.source := "operationTitle"
                  )(),
                  <.TextField(^.source := "country")(),
                  <.TextField(^.source := "language")(),
                  <.TextInput(
                    ^.source := "question",
                    ^.allowEmpty := false,
                    ^.validate := required,
                    ^.options := Map("fullWidth" -> true)
                  )(),
                  <.TextField(^.source := "slug")()
                ),
                <.FormTab(^.label := "Configuration")(
                  <.h2(^.style := Map("color" -> "red"))("Global"),
                  <.BooleanInput(^.label := "Can propose", ^.source := "canPropose")(),
                  <.BooleanInput(^.label := "Display results", ^.source := "displayResults")(),
                  <.LongTextInput(
                    ^.label := "Description (multiline)",
                    ^.source := "description",
                    ^.options := Map("fullWidth" -> true, "floatingLabelFixed" -> true)
                  )(),
                  <.h2(^.style := Map("color" -> "red"))("Cards"),
                  <.DependentInput(^.dependsOn := "canPropose", ^.dependsValue := true)(
                    <.BooleanInput(
                      ^.label := "Push proposal card",
                      ^.source := "sequenceCardsConfiguration.pushProposalCard.enabled"
                    )()
                  ),
                  <.DependentInput(^.dependsOn := "canPropose", ^.dependsValue := false)(
                    <.BooleanInput(
                      ^.label := "Push proposal card",
                      ^.source := "disabledPushProposalCard",
                      ^.options := Map("disabled" -> true)
                    )()
                  ),
                  <.hr.empty,
                  <.h3()("Intro Card"),
                  <.BooleanInput(^.label := "Yes / No", ^.source := "sequenceCardsConfiguration.introCard.enabled")(),
                  <.TextInput(
                    ^.label := "Title",
                    ^.source := "sequenceCardsConfiguration.introCard.title",
                    ^.options := Map("fullWidth" -> true)
                  )(),
                  <.LongTextInput(
                    ^.label := "Description (multiline)",
                    ^.source := "sequenceCardsConfiguration.introCard.description",
                    ^.options := Map(
                      "fullWidth" -> true,
                      "floatingLabelFixed" -> true,
                      "hintText" -> ("Prenez position sur ces solutions et proposez les vôtres !\n" +
                        "Les meilleures détermineront nos actions"),
                      "hintStyle" -> js.Dictionary("whiteSpace" -> "pre")
                    )
                  )(),
                  <.hr.empty,
                  <.h3()("Signup Card"),
                  <.BooleanInput(^.label := "Yes / No", ^.source := "sequenceCardsConfiguration.signUpCard.enabled")(),
                  <.TextInput(
                    ^.label := "Title",
                    ^.source := "sequenceCardsConfiguration.signUpCard.title",
                    ^.options := Map(
                      "fullWidth" -> true,
                      "floatingLabelFixed" -> true,
                      "hintText" -> "Recevez les résultats de la consultation et soyez informé(e) des actions à venir"
                    )
                  )(),
                  <.TextInput(
                    ^.label := "Next card button text",
                    ^.source := "sequenceCardsConfiguration.signUpCard.nextCtaText",
                    ^.options := Map(
                      "fullWidth" -> true,
                      "floatingLabelFixed" -> true,
                      "hintText" -> "NON MERCI, JE NE SOUHAITE PAS ÊTRE INFORMÉ(E) DES RÉSULTATS"
                    )
                  )(),
                  <.hr.empty,
                  <.h3()("Final Card"),
                  <.BooleanInput(^.label := "Yes / No", ^.source := "sequenceCardsConfiguration.finalCard.enabled")(),
                  <.DependentInput(
                    ^.dependsOn := "sequenceCardsConfiguration.finalCard.enabled",
                    ^.dependsValue := true
                  )(
                    <.BooleanInput(
                      ^.label := "With sharing",
                      ^.source := "sequenceCardsConfiguration.finalCard.sharingEnabled"
                    )()
                  ),
                  <.DependentInput(
                    ^.dependsOn := "sequenceCardsConfiguration.finalCard.enabled",
                    ^.dependsValue := false
                  )(
                    <.BooleanInput(
                      ^.label := "With sharing",
                      ^.source := "disabledSharingEnabled",
                      ^.options := Map("disabled" -> true)
                    )()
                  ),
                  <.TextInput(
                    ^.label := "Title",
                    ^.source := "sequenceCardsConfiguration.finalCard.title",
                    ^.options := Map(
                      "fullWidth" -> true,
                      "floatingLabelFixed" -> true,
                      "hintText" -> "Merci pour votre participation !"
                    )
                  )(),
                  <.LongTextInput(
                    ^.label := "Share text (multiline)",
                    ^.source := "sequenceCardsConfiguration.finalCard.shareDescription",
                    ^.options := Map(
                      "fullWidth" -> true,
                      "floatingLabelFixed" -> true,
                      "hintText" -> (
                        "Vous souhaitez aller plus loin sur cette consultation ?\n" +
                          "Invitez vos proches et/ou votre communauté à participer\n" +
                          "Découvrez toutes les propositions sur cette consultation"
                      ),
                      "hintStyle" -> js.Dictionary("whiteSpace" -> "pre")
                    )
                  )(),
                  <.TextInput(
                    ^.label := "Learn more title",
                    ^.source := "sequenceCardsConfiguration.finalCard.learnMoreTitle",
                    ^.options := Map(
                      "fullWidth" -> true,
                      "floatingLabelFixed" -> true,
                      "hintText" -> "Découvrez toutes les propositions."
                    )
                  )(),
                  <.TextInput(
                    ^.label := "Learn more button text",
                    ^.source := "sequenceCardsConfiguration.finalCard.learnMoreTextButton",
                    ^.options := Map("fullWidth" -> true, "floatingLabelFixed" -> true, "hintText" -> "En savoir +")
                  )(),
                  <.TextInput(
                    ^.label := "Link url (operation page)",
                    ^.source := "sequenceCardsConfiguration.finalCard.linkUrl",
                    ^.`type` := "url",
                    ^.options := Map("fullWidth" -> true)
                  )(),
                  <.hr.empty,
                  <.h3()("About page"),
                  <.TextInput(
                    ^.label := "Link url",
                    ^.source := "aboutUrl",
                    ^.`type` := "url",
                    ^.options := Map("fullWidth" -> true)
                  )(),
                  <.hr.empty,
                  <.h2(^.style := Map("color" -> "red"))("Metas"),
                  <.TextInput(
                    ^.label := "Title",
                    ^.source := "metas.title",
                    ^.options := Map(
                      "fullWidth" -> true,
                      "floatingLabelFixed" -> true,
                      "hintText" -> "Vous avez une idée sur le sujet ?"
                    )
                  )(),
                  <.TextInput(
                    ^.label := "Description",
                    ^.source := "metas.description",
                    ^.options := Map(
                      "fullWidth" -> true,
                      "floatingLabelFixed" -> true,
                      "hintText" -> "Participez à la consultation initiée par [Nom partenaires fondateurs] avec Make.org"
                    )
                  )(),
                  <.TextInput(
                    ^.label := "Picture",
                    ^.source := "metas.picture",
                    ^.`type` := "url",
                    ^.options := Map("fullWidth" -> true)
                  )()
                ),
                <.FormTab(^.label := "Theme")(
                  <.ColorInput(^.source := "theme.gradientStart", ^.label := "Gradient start")(),
                  <.ColorInput(^.source := "theme.gradientEnd", ^.label := "Gradient End")(),
                  <.ColorInput(^.source := "theme.color", ^.label := "Color")(),
                  <.ColorInput(^.source := "theme.fontColor", ^.label := "Font color")(),
                  <.br.empty,
                  <.ConsultationImagePreview.empty,
                  <.ImageInput(
                    ^.name := "consultationImagePreviewInput",
                    ^.source := "consultationImagePreviewInput",
                    ^.label := "Upload new image",
                    ^.accept := "image/*",
                    ^.onChange := getFileConsultationImage,
                    ^.style := Map("width" -> "50%")
                  )(<.ImageField(^.source := "src", ^.title := "title", ^.style := Map("width" -> "50%"))()),
                  self.state.fileConsultationImage.map { file =>
                    <.ImageUrlValidate(^.wrapped := ImageUrlValidateProps(uploadFile = handleConsultationImage(file)))()
                  }.getOrElse(<.div()()),
                  <.br.empty,
                  <.DescriptionImagePreview.empty,
                  <.ImageInput(
                    ^.name := "descriptionImagePreviewInput",
                    ^.source := "descriptionImagePreviewInput",
                    ^.label := "Upload new image",
                    ^.accept := "image/*",
                    ^.onChange := getFileDescriptionImage,
                    ^.style := Map("width" -> "50%")
                  )(<.ImageField(^.source := "src", ^.title := "title", ^.style := Map("width" -> "50%"))()),
                  self.state.fileDescriptionImage.map { file =>
                    <.ImageUrlValidate(^.wrapped := ImageUrlValidateProps(uploadFile = handleDescriptionImage(file)))()
                  }.getOrElse(<.div()())
                )
              )
            ),
            <.Snackbar(
              ^.open := self.state.snackbarOpen,
              ^.message := self.state.errorMessage,
              ^.autoHideDuration := 5000,
              ^.onRequestClose := (_ => self.setState(_.copy(snackbarOpen = false)))
            )(),
            <.style()(EditQuestionStyles.render[String])
          )
        }
      )
}

object EditQuestionStyles extends StyleSheet.Inline {

  import dsl._

  val imgPreview: StyleA =
    style(
      display.inlineBlock,
      maxWidth(50.%%),
      unsafeChild("label")(color(rgba(0, 0, 0, 0.3)), fontSize(12.px), fontWeight._700)
    )

  val validateImage: StyleA =
    style(width(50.%%))

}

@js.native
trait ImageFileWrapper extends js.Object {
  val `0`: ImageFile
}

@js.native
trait ImageFile extends js.Object {
  val rawFile: File
  val src: String
  val title: String
}

object ImageFile {
  def apply(rawFile: File, src: String, title: String): ImageFile =
    js.Dynamic
      .literal(rawFile = rawFile, src = src, title = title)
      .asInstanceOf[ImageFile]
}

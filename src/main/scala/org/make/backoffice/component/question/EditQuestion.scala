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
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.backoffice.client.Resource
import org.make.backoffice.component.CustomAORValueInput.CustomAORValueProps
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.images.ImageUploadField.ImageUploadFieldProps
import org.make.backoffice.component.images.ImageUploadFieldStyle
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.FormTab._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SaveButton._
import org.make.backoffice.facade.AdminOnRest.TabbedForm._
import org.make.backoffice.facade.AdminOnRest.required
import org.make.backoffice.facade.reduxForm.Field._
import org.make.backoffice.facade.reduxForm.FieldHolder
import org.make.backoffice.service.question.QuestionService
import org.make.backoffice.util.DateParser
import org.scalajs.dom.FormData
import scalacss.DevDefaults._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object EditQuestion {

  case class EditQuestionProps() extends RouterProps
  case class EditQuestionState(displayResults: Boolean, resultsLink: String)

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[EditQuestionProps, EditQuestionState](
        displayName = "EditQuestion",
        render = self => {

          def uploadQuestionImage(questionId: String): FormData => Future[String] = { data =>
            QuestionService
              .uploadImage(questionId, formData = data)
              .map(_.path)
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
                  <.TextInput(
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
                  <.TextInput(
                    ^.label := "Short title (30 characters max)",
                    ^.source := "shortTitle",
                    ^.options := Map("fullWidth" -> true, "maxLength" -> 30)
                  )(),
                  <.TextField(^.source := "slug")()
                ),
                <.FormTab(^.label := "Configuration")(
                  <.h2(^.style := Map("color" -> "red"))("Global"),
                  <.BooleanInput(^.label := "Can propose", ^.source := "canPropose")(),
                  <.BooleanInput(^.label := "Display Results", ^.source := "displayResults")(),
                  <.DependentInput(^.dependsOn := "displayResults", ^.dependsValue := true)(
                    <.TextInput(
                      ^.label := "Results link",
                      ^.source := "resultsLink",
                      ^.`type` := "url",
                      ^.options := Map("fullWidth" -> true)
                    )()
                  ),
                  <.DependentInput(^.dependsOn := "displayResults", ^.dependsValue := false)(
                    <.Field(
                      ^.name := "resultsLink",
                      ^.source := "resultsLink",
                      ^.label := "Hidden",
                      ^.style := Map("display" -> "none"),
                      ^.component := { holder: FieldHolder =>
                        <.CustomAORValueInputComponent(
                          ^.wrapped := CustomAORValueProps(initialValue = "", input = holder.input, label = "hidden")
                        )()
                      }
                    )()
                  ),
                  <.BooleanInput(^.label := "Featured", ^.source := "featured")(),
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
                  <.UploadImageComponent(
                    ^.wrapped := ImageUploadFieldProps(
                      "metas.picture",
                      "Picture",
                      ImageUploadFieldStyle.dropzone.htmlClass,
                      ImageUploadFieldStyle.preview.htmlClass,
                      uploadQuestionImage(self.props.native.`match`.params.id.toString)
                    )
                  )(),
                ),
                <.FormTab(^.label := "Theme")(
                  <.ColorInput(^.source := "theme.gradientStart", ^.label := "Gradient start")(),
                  <.ColorInput(^.source := "theme.gradientEnd", ^.label := "Gradient end")(),
                  <.ColorInput(^.source := "theme.color", ^.label := "Color")(),
                  <.ColorInput(^.source := "theme.fontColor", ^.label := "Font color")(),
                  <.ColorInput(^.source := "theme.secondaryColor", ^.label := "Secondary color")(),
                  <.ColorInput(^.source := "theme.secondaryFontColor", ^.label := "Secondary font color")(),
                  <.UploadImageComponent(
                    ^.wrapped := ImageUploadFieldProps(
                      "consultationImage",
                      "Consultation Image",
                      ImageUploadFieldStyle.dropzone.htmlClass,
                      ImageUploadFieldStyle.preview.htmlClass,
                      uploadQuestionImage(self.props.native.`match`.params.id.toString)
                    )
                  )(),
                  <.UploadImageComponent(
                    ^.wrapped := ImageUploadFieldProps(
                      "descriptionImage",
                      "Description Image",
                      ImageUploadFieldStyle.dropzone.htmlClass,
                      ImageUploadFieldStyle.preview.htmlClass,
                      uploadQuestionImage(self.props.native.`match`.params.id.toString)
                    )
                  )()
                )
              )
            ),
            <.style()(ImageUploadFieldStyle.render[String])
          )
        }
      )
}

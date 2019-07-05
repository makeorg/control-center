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
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.{FeaturedOperation, Question}
import org.make.backoffice.service.operation.{
  CreateFeaturedOperationRequest,
  FeaturedOperationService,
  UpdateFeaturedOperationRequest
}
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

object HeaderComponent {

  case class HeaderComponentProps(featuredOperation: Option[FeaturedOperation], slot: Int, questionsList: Seq[Question])
  case class HeaderComponentState(id: String,
                                  questionId: Option[String],
                                  title: String,
                                  description: Option[String],
                                  landscapePicture: String,
                                  portraitPicture: String,
                                  altPicture: String,
                                  label: String,
                                  buttonLabel: String,
                                  internalLink: Option[String],
                                  externalLink: Option[String],
                                  slot: Int,
                                  internalLinkChecked: Boolean,
                                  externalLinkChecked: Boolean,
                                  cardExpended: Boolean,
                                  toCreate: Boolean)

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
            altPicture = "",
            label = "",
            buttonLabel = "",
            internalLink = None,
            externalLink = None,
            slot = self.props.wrapped.slot,
            internalLinkChecked = false,
            externalLinkChecked = false,
            cardExpended = false,
            toCreate = true
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
                  altPicture = featuredOperation.altPicture,
                  label = featuredOperation.label,
                  buttonLabel = featuredOperation.buttonLabel,
                  internalLink = featuredOperation.internalLink.toOption,
                  externalLink = featuredOperation.externalLink.toOption,
                  slot = featuredOperation.slot,
                  internalLinkChecked = featuredOperation.internalLink.isDefined,
                  externalLinkChecked = featuredOperation.externalLink.isDefined,
                  cardExpended = true,
                  toCreate = false
                )
              )
          }
        },
        render = self => {

          def onExpendChange: Boolean => Unit = { isExpended =>
            self.setState(_.copy(cardExpended = isExpended))
          }

          def onChangeAltPicture: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(altPicture = value))
          }

          def onChangeLandscapePicture: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(landscapePicture = value))
          }

          def onChangePortraitPicture: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(portraitPicture = value))
          }

          def onChangeLabel: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(label = value))
          }

          def onChangeTitle: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(title = value))
          }

          def onChangeDescription: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = if (event.target.value.isEmpty) None else Some(event.target.value)
            self.setState(_.copy(description = value))
          }

          def onChangeButtonLabel: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(buttonLabel = value))
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

          def onSelectInternalLink: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
            self.setState(_.copy(internalLink = Some(value)))
          }

          def onSelectQuestion: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
            self.setState(_.copy(questionId = Some(value)))
          }

          def handleCreation: SyntheticEvent => Unit = {
            _ =>
              val request = CreateFeaturedOperationRequest(
                questionId = self.state.questionId,
                title = self.state.title,
                description = self.state.description,
                landscapePicture = self.state.landscapePicture,
                portraitPicture = self.state.portraitPicture,
                altPicture = self.state.altPicture,
                label = self.state.label,
                buttonLabel = self.state.buttonLabel,
                internalLink = if (!self.state.internalLinkChecked) None else self.state.internalLink,
                externalLink = if (!self.state.externalLinkChecked) None else self.state.externalLink,
                slot = self.state.slot
              )
              FeaturedOperationService.postFeaturedOperation(request)
          }

          def handleUpdate: SyntheticEvent => Unit = {
            _ =>
              val request = UpdateFeaturedOperationRequest(
                questionId = self.state.questionId,
                title = self.state.title,
                description = self.state.description,
                landscapePicture = self.state.landscapePicture,
                portraitPicture = self.state.portraitPicture,
                altPicture = self.state.altPicture,
                label = self.state.label,
                buttonLabel = self.state.buttonLabel,
                internalLink = if (!self.state.internalLinkChecked) None else self.state.internalLink,
                externalLink = if (!self.state.externalLinkChecked) None else self.state.externalLink,
                slot = self.state.slot
              )
              FeaturedOperationService.putFeaturedOperation(self.state.id, request)
          }

          def handleDelete: SyntheticEvent => Unit = {
            _ =>
              FeaturedOperationService.deleteFeaturedOperation(self.state.id)
              self.setState(
                _.copy(
                  id = "",
                  questionId = None,
                  title = "",
                  description = None,
                  landscapePicture = "",
                  portraitPicture = "",
                  altPicture = "",
                  label = "",
                  buttonLabel = "",
                  internalLink = None,
                  externalLink = None,
                  slot = self.props.wrapped.slot,
                  internalLinkChecked = false,
                  externalLinkChecked = false,
                  cardExpended = false,
                  toCreate = true
                )
              )
          }

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
              <.TextFieldMaterialUi(
                ^.name := "Alternative text",
                ^.floatingLabelText := "Alternative text",
                ^.floatingLabelFixed := true,
                ^.value := self.state.altPicture,
                ^.onChange := onChangeAltPicture
              )(),
              <.div()(
                <.TextFieldMaterialUi(
                  ^.name := "link desktop",
                  ^.floatingLabelText := "Link desktop",
                  ^.floatingLabelFixed := true,
                  ^.value := self.state.landscapePicture,
                  ^.onChange := onChangeLandscapePicture
                )(),
                <.TextFieldMaterialUi(
                  ^.name := "link mobile",
                  ^.floatingLabelText := "Link mobile",
                  ^.floatingLabelFixed := true,
                  ^.value := self.state.portraitPicture,
                  ^.onChange := onChangePortraitPicture
                )()
              ),
              <.h3(^.style := Map("color" -> "blue"))("Label"),
              <.TextFieldMaterialUi(
                ^.name := "label",
                ^.value := self.state.label,
                ^.onChange := onChangeLabel,
                ^.fullWidth := true
              )(),
              <.h3(^.style := Map("color" -> "blue"))("Title"),
              <.TextFieldMaterialUi(
                ^.name := "title",
                ^.value := self.state.title,
                ^.onChange := onChangeTitle,
                ^.fullWidth := true
              )(),
              <.h3(^.style := Map("color" -> "blue"))("Description"),
              <.TextFieldMaterialUi(
                ^.name := "description",
                ^.value := self.state.description.getOrElse(""),
                ^.onChange := onChangeDescription,
                ^.fullWidth := true
              )(),
              <.h3(^.style := Map("color" -> "blue"))("Button text"),
              <.TextFieldMaterialUi(
                ^.name := "button text",
                ^.value := self.state.buttonLabel,
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
                  ^.value := self.state.questionId.getOrElse(""),
                  ^.disabled := !self.state.internalLinkChecked,
                  ^.onChangeSelect := onSelectQuestion
                )(self.props.wrapped.questionsList.map { question =>
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
          )
        }
      )

}

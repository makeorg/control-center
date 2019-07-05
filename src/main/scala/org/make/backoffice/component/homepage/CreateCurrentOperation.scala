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

import java.time.ZonedDateTime

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, MouseSyntheticEvent, SyntheticEvent}
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.request.Filter
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.{FeaturedOperation, Question}
import org.make.backoffice.service.operation.{CreateCurrentOperationRequest, CurrentOperationService}
import org.make.backoffice.service.question.QuestionService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object CreateCurrentOperation {

  case class CreateCurrentOperationProps() extends RouterProps
  case class CreateCurrentOperationState(questionId: String,
                                         description: String,
                                         label: String,
                                         picture: String,
                                         altPicture: String,
                                         linkLabel: String,
                                         internalLink: Option[String],
                                         externalLink: Option[String],
                                         questionsList: Seq[Question],
                                         internalLinkChecked: Boolean,
                                         externalLinkChecked: Boolean)

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[CreateCurrentOperationProps, CreateCurrentOperationState](
        displayName = "CreateCurrentOperation",
        getInitialState = _ => {
          CreateCurrentOperationState(
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
            externalLinkChecked = false
          )
        },
        componentWillMount = self => {
          QuestionService
            .questions(filters = Some(Seq(Filter("openAt", ZonedDateTime.now()))))
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
            self.setState(_.copy(questionId = value))
          }

          def onChangeDescription: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(description = value))
          }

          def onChangeLabel: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(label = value))
          }

          def onChangePicture: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(picture = value))
          }

          def onChangeAltPicture: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(altPicture = value))
          }

          def onChangeLinkLabel: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val value = event.target.value
            self.setState(_.copy(linkLabel = value))
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

          def handleCreate: SyntheticEvent => Unit = {
            _ =>
              val request = CreateCurrentOperationRequest(
                questionId = self.state.questionId,
                description = self.state.description,
                label = self.state.label,
                picture = self.state.picture,
                altPicture = self.state.altPicture,
                linkLabel = self.state.linkLabel,
                internalLink = if (!self.state.internalLinkChecked) None else self.state.internalLink,
                externalLink = if (!self.state.externalLinkChecked) None else self.state.externalLink
              )
              CurrentOperationService.postCurrentOperation(request)
          }

          <.div()(
            <.Card(^.style := Map("marginTop" -> "30px"))(
              <.CardTitle(^.title := "Create Current Operation")(),
              <.CardText(^.style := Map("display" -> "grid"))(
                <.SelectField(
                  ^.floatingLabelText := "question",
                  ^.value := self.state.questionId,
                  ^.fullWidth := true,
                  ^.floatingLabelFixed := true,
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
                  ^.onChange := onChangeDescription
                )(),
                <.TextFieldMaterialUi(
                  ^.name := "label",
                  ^.floatingLabelText := "Label",
                  ^.floatingLabelFixed := true,
                  ^.value := self.state.label,
                  ^.fullWidth := true,
                  ^.onChange := onChangeLabel
                )(),
                <.TextFieldMaterialUi(
                  ^.name := "picture",
                  ^.floatingLabelText := "Picture",
                  ^.floatingLabelFixed := true,
                  ^.value := self.state.picture,
                  ^.fullWidth := true,
                  ^.onChange := onChangePicture
                )(),
                <.TextFieldMaterialUi(
                  ^.name := "alternativeText",
                  ^.floatingLabelText := "Alternative text",
                  ^.floatingLabelFixed := true,
                  ^.value := self.state.altPicture,
                  ^.fullWidth := true,
                  ^.onChange := onChangeAltPicture
                )(),
                <.TextFieldMaterialUi(
                  ^.name := "linkLabel",
                  ^.floatingLabelText := "Link label",
                  ^.floatingLabelFixed := true,
                  ^.value := self.state.linkLabel,
                  ^.fullWidth := true,
                  ^.onChange := onChangeLinkLabel
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
                <.ToolbarGroup()(<.RaisedButton(^.primary := true, ^.label := "Save", ^.onClick := handleCreate)())
              )
            )
          )
        }
      )

}

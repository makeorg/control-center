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
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.Question
import org.make.backoffice.service.question.QuestionService
import org.make.backoffice.service.question.QuestionService.{AuthorRequest, InitialProposalRequest}
import org.make.backoffice.util.Configuration
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object InitialProposalComponent {

  val contentTextError = "Proposal content is to small"
  val firstNameTextError = "User firstname must not be empty"
  val ageTextError = "User age must be between 13 and 120"

  case class InitialProposalComponentState(content: String,
                                           errorContent: Boolean,
                                           firstName: Option[String],
                                           errorFirstName: Boolean,
                                           lastName: Option[String],
                                           age: Option[String],
                                           errorAge: Boolean,
                                           postalCode: Option[String],
                                           profession: Option[String],
                                           questionId: String,
                                           snackbarOpen: Boolean,
                                           snackbarMessage: String)

  lazy val reactClass: ReactClass = React.createClass[Unit, InitialProposalComponentState](
    displayName = "InitialProposalComponent",
    getInitialState = self =>
      InitialProposalComponentState(
        content = "",
        errorContent = false,
        firstName = None,
        errorFirstName = false,
        lastName = None,
        age = None,
        errorAge = false,
        postalCode = None,
        profession = None,
        questionId = self.props.native.record.asInstanceOf[Question].id,
        snackbarOpen = false,
        snackbarMessage = ""
    ),
    render = { self =>
      def handleContentEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
        val newContent: String = event.target.value.substring(0, Configuration.proposalMaxLength)
        self.setState(_.copy(content = newContent))
      }

      def handleFirstnameEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
        val firstName: String = event.target.value
        self.setState(_.copy(firstName = Some(firstName)))
      }

      def handleLastnameEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
        val lastName: String = event.target.value
        self.setState(_.copy(lastName = Some(lastName)))
      }

      def handleAgeEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
        val age: String = event.target.value
        if (age.forall(Character.isDigit)) {
          self.setState(_.copy(age = Some(age)))
        }
      }

      def handlePostalCodeEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
        val postalCode: String = event.target.value
        self.setState(_.copy(postalCode = Some(postalCode)))
      }

      def handleProfessionEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
        val profession: String = event.target.value
        self.setState(_.copy(profession = Some(profession)))
      }

      def isValidFields(content: String, firstName: Option[String], age: Option[String]): Boolean = {
        var isValid: Boolean = true
        if (content.length < Configuration.proposalMinLength) {
          self.setState(_.copy(errorContent = true))
          isValid = false
        } else {
          self.setState(_.copy(errorContent = false))
        }
        if (firstName.forall(_.isEmpty)) {
          self.setState(_.copy(errorFirstName = true))
          isValid = false
        } else {
          self.setState(_.copy(errorFirstName = false))
        }
        if (age.exists(age => age.toInt < 13 || age.toInt > 120)) {
          self.setState(_.copy(errorAge = true))
          isValid = false
        } else {
          self.setState(_.copy(errorAge = false))
        }
        isValid
      }

      def onCreateProposal: SyntheticEvent => Unit = {
        event =>
          event.preventDefault()

          if (isValidFields(self.state.content, self.state.firstName, self.state.age)) {

            val request = InitialProposalRequest(
              content = self.state.content,
              author = AuthorRequest(
                age = self.state.age,
                firstName = self.state.firstName,
                lastName = self.state.lastName,
                postalCode = self.state.postalCode,
                profession = self.state.profession
              )
            )

            QuestionService.addInitialProposal(self.state.questionId, request).onComplete {
              case Success(_) =>
                self.setState(
                  _.copy(
                    content = "",
                    firstName = None,
                    lastName = None,
                    age = None,
                    postalCode = None,
                    profession = None,
                    snackbarOpen = true,
                    snackbarMessage = "Proposal successfully created"
                  )
                )
              case Failure(e) =>
                self.setState(_.copy(snackbarOpen = true, snackbarMessage = s"Error while creating proposal"))
            }
          }
      }

      <.Card(^.style := Map("marginTop" -> "1em"))(
        <.CardActions()(
          <.TextFieldMaterialUi(
            ^.floatingLabelText := "Proposal content",
            ^.value := self.state.content,
            ^.onChange := handleContentEdition,
            ^.errorText := { if (self.state.errorContent) contentTextError else "" },
            ^.fullWidth := true
          )(),
          <.span()(s"${self.state.content.length}/${Configuration.proposalMaxLength}"),
          <.TextFieldMaterialUi(
            ^.floatingLabelText := "Author firstname *",
            ^.value := self.state.firstName.getOrElse(""),
            ^.onChange := handleFirstnameEdition,
            ^.errorText := { if (self.state.errorFirstName) firstNameTextError else "" },
            ^.fullWidth := true
          )(),
          <.TextFieldMaterialUi(
            ^.floatingLabelText := "Author lastname",
            ^.value := self.state.lastName.getOrElse(""),
            ^.onChange := handleLastnameEdition,
            ^.fullWidth := true
          )(),
          <.TextFieldMaterialUi(
            ^.floatingLabelText := "Author age",
            ^.value := self.state.age.getOrElse(""),
            ^.onChange := handleAgeEdition,
            ^.errorText := { if (self.state.errorAge) ageTextError else "" },
            ^.fullWidth := true
          )(),
          <.TextFieldMaterialUi(
            ^.floatingLabelText := "Author postal code",
            ^.value := self.state.postalCode.getOrElse(""),
            ^.onChange := handlePostalCodeEdition,
            ^.fullWidth := true
          )(),
          <.TextFieldMaterialUi(
            ^.floatingLabelText := "Author profession",
            ^.value := self.state.profession.getOrElse(""),
            ^.onChange := handleProfessionEdition,
            ^.fullWidth := true
          )()
        ),
        <.CardActions()(
          <.RaisedButton(
            ^.label := "Create proposal",
            ^.primary := true,
            ^.fullWidth := true,
            ^.onClick := onCreateProposal
          )()
        ),
        <.Snackbar(
          ^.open := self.state.snackbarOpen,
          ^.message := self.state.snackbarMessage,
          ^.autoHideDuration := 5000,
          ^.onRequestClose := { _ =>
            self.setState(_.copy(snackbarOpen = false))
          }
        )()
      )
    }
  )

}

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
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.autoComplete.AutoComplete.AutoCompleteProps
import org.make.backoffice.facade.DataSourceConfig
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.{Personality, Question, SimpleUser}
import org.make.backoffice.service.personality.{
  CreatePersonalityRequest,
  PersonalityService,
  QuestionPersonalityService
}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object CreatePersonalityComponent {

  case class CreatePersonalityComponentProps(reloadComponent: () => Unit)
  case class CreatePersonalityComponentState(personalityRole: String,
                                             errorPersonalityRole: String,
                                             userId: Option[String],
                                             createPersonalityModalOpen: Boolean,
                                             snackbarOpen: Boolean,
                                             snackbarMessage: String)

  lazy val reactClass: ReactClass =
    React.createClass[CreatePersonalityComponentProps, CreatePersonalityComponentState](
      displayName = "CreatePersonalityComponent",
      getInitialState = { _ =>
        CreatePersonalityComponentState(
          personalityRole = "",
          errorPersonalityRole = "",
          userId = None,
          createPersonalityModalOpen = false,
          snackbarOpen = false,
          snackbarMessage = ""
        )
      },
      render = { self =>
        def onChangePersonalityRole: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
          self.setState(_.copy(personalityRole = value, errorPersonalityRole = ""))
        }

        def handleNewUserRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
          val user = chosenRequest.asInstanceOf[SimpleUser]
          self.setState(_.copy(userId = Some(user.id)))
        }

        def onCreatePersonality: SyntheticEvent => Unit = {
          _ =>
            var error = false
            var errorPersonalityRole = ""

            if (self.state.personalityRole.isEmpty) {
              errorPersonalityRole = "Personality role is required"
              error = true
            }

            if (!error) {

              val questionId = self.props.native.record.asInstanceOf[Question].id

              val request = CreatePersonalityRequest(
                userId = self.state.userId,
                questionId = questionId,
                personalityRole = self.state.personalityRole
              )

              QuestionPersonalityService.createPersonality(request = request).onComplete {
                case Success(_) =>
                  self.setState(
                    _.copy(
                      userId = Some(""),
                      personalityRole = "",
                      snackbarOpen = true,
                      snackbarMessage = "Personality successfully created",
                      createPersonalityModalOpen = false
                    )
                  )
                  self.props.wrapped.reloadComponent()
                case Failure(_) =>
                  self.setState(_.copy(snackbarOpen = true, snackbarMessage = s"Error while creating personality"))
              }
            } else {
              self.setState(_.copy(errorPersonalityRole = errorPersonalityRole))
            }
        }

        def handleOpenModal: SyntheticEvent => Unit = { _ =>
          self.setState(_.copy(createPersonalityModalOpen = true))
        }

        def handleCloseModal: SyntheticEvent => Unit = { _ =>
          self.setState(_.copy(createPersonalityModalOpen = false))
        }

        <.div()(
          <.CardActions()(
            <.FlatButton(^.label := "Create personality", ^.primary := true, ^.onClick := handleOpenModal)()
          ),
          <.Dialog(
            ^.title := "Create personality",
            ^.open := self.state.createPersonalityModalOpen,
            ^.autoScrollBodyContent := true,
            ^.actionsModal := Seq(
              <.FlatButton(^.label := "Cancel", ^.onClick := handleCloseModal)(),
              <.FlatButton(^.label := "Create personality", ^.primary := true, ^.onClick := onCreatePersonality)()
            )
          )(
            <.AutoCompleteComponent(
              ^.wrapped := AutoCompleteProps(
                searchRequest = PersonalityService.personalities,
                handleNewRequest = handleNewUserRequest,
                dataSourceConfig = DataSourceConfig("fullName", "id")
              )
            )(),
            <.SelectField(
              ^.floatingLabelText := "Role *",
              ^.value := self.state.personalityRole,
              ^.onChangeSelect := onChangePersonalityRole,
              ^.errorText := self.state.errorPersonalityRole
            )(Personality.personalityRoleMap.map {
              case (key, value) =>
                <.MenuItem(^.key := key, ^.value := key, ^.primaryText := value)()
            })
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

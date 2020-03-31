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
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.PersonalityRole
import org.make.backoffice.service.personality.{
  PersonalityRoleService,
  QuestionPersonalityService,
  UpdatePersonalityRequest
}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object EditPersonalityComponent {

  @js.native
  trait PersonalityRecord extends js.Object {
    val id: String
    val userId: String
    val personalityRoleId: String
  }

  case class EditPersonalityComponentProps(reloadComponent: () => Unit)
  case class EditPersonalityComponentState(personalityId: String,
                                           userId: String,
                                           selectedPersonalityRole: String,
                                           personalityRoles: Seq[PersonalityRole],
                                           errorPersonalityRole: String,
                                           editPersonalityModalOpen: Boolean,
                                           snackbarOpen: Boolean,
                                           snackbarMessage: String)

  lazy val reactClass: ReactClass =
    React.createClass[EditPersonalityComponentProps, EditPersonalityComponentState](
      displayName = "EditPersonalityComponent",
      getInitialState = { self =>
        val personality = self.props.native.record.asInstanceOf[PersonalityRecord]
        EditPersonalityComponentState(
          personalityId = personality.id,
          userId = personality.userId,
          selectedPersonalityRole = personality.personalityRoleId,
          personalityRoles = Seq.empty,
          errorPersonalityRole = "",
          editPersonalityModalOpen = false,
          snackbarOpen = false,
          snackbarMessage = ""
        )
      },
      componentWillReceiveProps = { (self, _) =>
        if (self.state.personalityRoles.isEmpty) {
          PersonalityRoleService.personalitieRoles.onComplete {
            case Success(personalityRoles) =>
              self.setState(_.copy(personalityRoles = personalityRoles, snackbarOpen = false))
            case Failure(e) =>
              self.setState(
                _.copy(snackbarMessage = s"fail to retrieve personality roles: ${e.getMessage}", snackbarOpen = true)
              )
          }
        }
      },
      render = { self =>
        val editSvgPath =
          "M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"

        val editIcon: ReactElement =
          <.SvgIcon()(React.createElement("path", js.Dictionary("d" -> editSvgPath)))

        def onChangePersonalityRole: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
          self.setState(_.copy(selectedPersonalityRole = value, errorPersonalityRole = ""))
        }

        def onEditPersonality: SyntheticEvent => Unit = {
          _ =>
            var error = false
            var errorPersonalityRole = ""

            if (self.state.selectedPersonalityRole.isEmpty) {
              errorPersonalityRole = "Personality role is required"
              error = true
            }

            if (!error) {

              val request =
                UpdatePersonalityRequest(
                  userId = self.state.userId,
                  personalityRoleId = self.state.selectedPersonalityRole
                )

              QuestionPersonalityService
                .editPersonality(request = request, personalityId = self.state.personalityId)
                .onComplete {
                  case Success(_) =>
                    self.setState(
                      _.copy(
                        userId = "",
                        selectedPersonalityRole = "",
                        snackbarOpen = true,
                        snackbarMessage = "Personality successfully updated",
                        editPersonalityModalOpen = false
                      )
                    )
                    self.props.wrapped.reloadComponent()
                  case Failure(_) =>
                    self.setState(_.copy(snackbarOpen = true, snackbarMessage = s"Error while updating personality"))
                }
            } else {
              self.setState(_.copy(errorPersonalityRole = errorPersonalityRole))
            }
        }

        def handleOpenModal: SyntheticEvent => Unit = { _ =>
          self.setState(_.copy(editPersonalityModalOpen = true))
        }

        def handleCloseModal: SyntheticEvent => Unit = { _ =>
          self.setState(_.copy(editPersonalityModalOpen = false))
        }

        <.div()(
          <.CardActions()(
            <.FlatButton(^.label := "Edit", ^.buttonIcon := editIcon, ^.primary := true, ^.onClick := handleOpenModal)()
          ),
          <.Dialog(
            ^.title := "Edit personality",
            ^.open := self.state.editPersonalityModalOpen,
            ^.autoScrollBodyContent := true,
            ^.actionsModal := Seq(
              <.FlatButton(^.label := "Cancel", ^.onClick := handleCloseModal)(),
              <.FlatButton(^.label := "Edit personality", ^.primary := true, ^.onClick := onEditPersonality)()
            )
          )(
            <.SelectField(
              ^.floatingLabelText := "Role",
              ^.value := self.state.selectedPersonalityRole,
              ^.onChangeSelect := onChangePersonalityRole,
              ^.errorText := self.state.errorPersonalityRole
            )(self.state.personalityRoles.map { personalityRole =>
              <.MenuItem(
                ^.key := personalityRole.id,
                ^.value := personalityRole.id,
                ^.primaryText := personalityRole.name
              )()
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

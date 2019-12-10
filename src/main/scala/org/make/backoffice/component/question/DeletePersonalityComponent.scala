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
//import org.make.backoffice.component.question.EditPersonalityComponent.PersonalityRecord
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.service.personality.QuestionPersonalityService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object DeletePersonalityComponent {

  case class DeletePersonalityComponentProps(reloadComponent: () => Unit)
  case class DeletePersonalityComponentState(personalityId: String,
                                             deletePersonalityModalOpen: Boolean,
                                             snackbarOpen: Boolean,
                                             snackbarMessage: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[DeletePersonalityComponentProps, DeletePersonalityComponentState](
        displayName = "DeletePersonality",
        getInitialState = { self =>
          DeletePersonalityComponentState(
            personalityId = self.props.native.record.id.toString,
            deletePersonalityModalOpen = false,
            snackbarOpen = false,
            snackbarMessage = ""
          )
        },
        render = { self =>
          val deleteSvgPath =
            "M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"

          val deleteIcon: ReactElement =
            <.SvgIcon()(React.createElement("path", js.Dictionary("d" -> deleteSvgPath)))

          def onDeletePersonality: SyntheticEvent => Unit = {
            event =>
              event.preventDefault()

              QuestionPersonalityService.deletePersonality(personalityId = self.state.personalityId).onComplete {
                case Success(_) =>
                  self.setState(
                    _.copy(
                      snackbarOpen = true,
                      snackbarMessage = "Personality successfully deleted",
                      deletePersonalityModalOpen = false
                    )
                  )
                  self.props.wrapped.reloadComponent()
                case Failure(_) =>
                  self.setState(_.copy(snackbarOpen = true, snackbarMessage = s"Error while deleting personality"))
              }
          }

          def handleOpenModal: SyntheticEvent => Unit = { _ =>
            self.setState(_.copy(deletePersonalityModalOpen = true))
          }

          def handleCloseModal: SyntheticEvent => Unit = { _ =>
            self.setState(_.copy(deletePersonalityModalOpen = false))
          }

          <.div()(
            <.FlatButton(
              ^.label := "Delete",
              ^.buttonIcon := deleteIcon,
              ^.secondary := true,
              ^.onClick := handleOpenModal
            )(),
            <.Dialog(
              ^.title := "Delete Personality",
              ^.open := self.state.deletePersonalityModalOpen,
              ^.actionsModal := Seq(
                <.FlatButton(^.label := "Cancel", ^.onClick := handleCloseModal)(),
                <.FlatButton(^.label := "Delete personality", ^.primary := true, ^.onClick := onDeletePersonality)()
              )
            )(s"Delete personality ?"),
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

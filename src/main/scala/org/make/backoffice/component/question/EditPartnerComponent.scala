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
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.Partner
import org.make.backoffice.service.partner.{PartnerService, UpdatePartnerRequest}
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.util.{Failure, Success}

object EditPartnerComponent {

  @js.native
  trait PartnerRecord extends js.Object {
    val id: String
    val name: String
    val logo: js.UndefOr[String]
    val link: js.UndefOr[String]
    val partnerKind: String
    val weight: Double
  }

  object PartnerRecord {

    def apply(id: String,
              name: String,
              logo: Option[String],
              link: Option[String],
              partnerKind: String,
              weight: Double): PartnerRecord = {
      js.Dynamic
        .literal(
          id = id,
          name = name,
          logo = logo.orUndefined,
          link = link.orUndefined,
          partnerKind = partnerKind,
          weight = weight
        )
        .asInstanceOf[PartnerRecord]
    }

  }

  case class EditPartnerComponentProps(reloadComponent: () => Unit)
  case class EditPartnerComponentState(partnerId: String,
                                       name: String,
                                       logo: Option[String],
                                       link: Option[String],
                                       partnerKind: String,
                                       weight: Double,
                                       editPartnerModalOpen: Boolean,
                                       snackbarOpen: Boolean,
                                       snackbarMessage: String)

  lazy val reactClass: ReactClass =
    React.createClass[EditPartnerComponentProps, EditPartnerComponentState](
      displayName = "EditPartnerComponent",
      getInitialState = { self =>
        val partner = self.props.native.record.asInstanceOf[PartnerRecord]
        EditPartnerComponentState(
          partnerId = partner.id,
          name = partner.name,
          logo = partner.logo.toOption,
          link = partner.link.toOption,
          partnerKind = partner.partnerKind,
          weight = partner.weight,
          editPartnerModalOpen = false,
          snackbarOpen = false,
          snackbarMessage = ""
        )
      },
      render = { self =>
        val editSvgPath =
          "M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"

        val editIcon: ReactElement =
          <.SvgIcon()(React.createElement("path", js.Dictionary("d" -> editSvgPath)))

        def handleNameEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
          val newName: String = event.target.value
          self.setState(_.copy(name = newName))
        }

        def handleLogoEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
          val newLogo: String = event.target.value
          self.setState(_.copy(logo = Some(newLogo)))
        }

        def handleLinkEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
          val newLink: String = event.target.value
          self.setState(_.copy(link = Some(newLink)))
        }

        def handleWeightEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
          val newWeight: String = event.target.value
          if (newWeight.nonEmpty) {
            self.setState(_.copy(weight = newWeight.toDouble))
          } else {
            self.setState(_.copy(weight = 0))
          }
        }

        def onChangePartnerKind: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
          self.setState(_.copy(partnerKind = value))
        }

        def onEditPartner: SyntheticEvent => Unit = {
          event =>
            event.preventDefault()

            val request = UpdatePartnerRequest(
              name = self.state.name,
              logo = self.state.logo,
              link = self.state.link,
              partnerKind = self.state.partnerKind,
              weight = self.state.weight
            )

            PartnerService.editPartner(request = request, partnerId = self.state.partnerId).onComplete {
              case Success(_) =>
                self.setState(
                  _.copy(
                    name = "",
                    logo = None,
                    link = None,
                    partnerKind = "",
                    weight = 0,
                    snackbarOpen = true,
                    snackbarMessage = "Partner successfully updated",
                    editPartnerModalOpen = false
                  )
                )
                self.props.wrapped.reloadComponent()
              case Failure(_) =>
                self.setState(_.copy(snackbarOpen = true, snackbarMessage = s"Error while updating partner"))
            }
        }

        def handleOpenModal: SyntheticEvent => Unit = { _ =>
          self.setState(_.copy(editPartnerModalOpen = true))
        }

        def handleCloseModal: SyntheticEvent => Unit = { _ =>
          self.setState(_.copy(editPartnerModalOpen = false))
        }

        <.div()(
          <.CardActions()(
            <.FlatButton(^.label := "Edit", ^.buttonIcon := editIcon, ^.primary := true, ^.onClick := handleOpenModal)()
          ),
          <.Dialog(
            ^.title := "Edit partner",
            ^.open := self.state.editPartnerModalOpen,
            ^.autoScrollBodyContent := true,
            ^.actionsModal := Seq(
              <.FlatButton(^.label := "Cancel", ^.onClick := handleCloseModal)(),
              <.FlatButton(^.label := "Edit partner", ^.primary := true, ^.onClick := onEditPartner)()
            )
          )(
            <.TextFieldMaterialUi(
              ^.floatingLabelText := "Name",
              ^.value := self.state.name,
              ^.onChange := handleNameEdition,
              ^.fullWidth := true
            )(),
            <.SelectField(
              ^.floatingLabelText := "Kind",
              ^.value := self.state.partnerKind,
              ^.onChangeSelect := onChangePartnerKind
            )(Partner.partnerKindMap.map {
              case (key, value) =>
                <.MenuItem(^.key := key, ^.value := key, ^.primaryText := value)()
            }),
            <.TextFieldMaterialUi(
              ^.floatingLabelText := "Logo",
              ^.value := self.state.logo.getOrElse(""),
              ^.onChange := handleLogoEdition,
              ^.fullWidth := true
            )(),
            <.TextFieldMaterialUi(
              ^.floatingLabelText := "Link",
              ^.value := self.state.link.getOrElse(""),
              ^.onChange := handleLinkEdition,
              ^.fullWidth := true
            )(),
            <.TextFieldMaterialUi(
              ^.floatingLabelText := "Weight",
              ^.value := self.state.weight,
              ^.`type` := "number",
              ^.onChange := handleWeightEdition,
              ^.fullWidth := true
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

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
import org.make.backoffice.facade.DataSourceConfig
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.{Organisation, Partner, Question}
import org.make.backoffice.service.organisation.OrganisationService
import org.make.backoffice.service.partner.{CreatePartnerRequest, PartnerService}
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.scalajs.js

object CreatePartnerComponent {

  case class CreatePartnerComponentProps(reloadComponent: () => Unit)
  case class CreatePartnerComponentState(name: String,
                                         logo: Option[String],
                                         link: Option[String],
                                         partnerKind: String,
                                         weight: Double,
                                         organisationId: Option[String],
                                         organisationSearchList: Seq[Organisation],
                                         searchOrganisationContent: String,
                                         createPartnerModalOpen: Boolean,
                                         snackbarOpen: Boolean,
                                         snackbarMessage: String)

  lazy val reactClass: ReactClass =
    React.createClass[CreatePartnerComponentProps, CreatePartnerComponentState](
      displayName = "CreatePartnerComponent",
      getInitialState = { _ =>
        CreatePartnerComponentState(
          name = "",
          logo = None,
          link = None,
          partnerKind = "",
          weight = 0,
          organisationId = None,
          organisationSearchList = Seq.empty,
          searchOrganisationContent = "",
          createPartnerModalOpen = false,
          snackbarOpen = false,
          snackbarMessage = ""
        )
      },
      componentDidMount = { self =>
        val nullOrganisation = Organisation(id = None, organisationName = "", profile = None)

        OrganisationService.organisations.onComplete {
          case Success(organisations) =>
            self.setState(_.copy(organisationSearchList = organisations.+:(nullOrganisation)))
          case Failure(_) =>
        }
      },
      render = { self =>
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

        def handleUpdateOrganisationInput: (String, js.Array[js.Object], js.Object) => Unit = (searchText, _, _) => {
          self.setState(_.copy(searchOrganisationContent = searchText))
        }

        def handleNewOrganisationRequest: (js.Object, Int) => Unit = (chosenRequest, _) => {
          val organisation = chosenRequest.asInstanceOf[Organisation]
          self.setState(
            _.copy(
              name = organisation.organisationName,
              logo = organisation.profile.flatMap(_.avatarUrl).toOption,
              organisationId = organisation.id.toOption
            )
          )
        }

        def filterAutoComplete: (String, String) => Boolean = (searchText, key) => {
          key.indexOf(searchText) != -1
        }

        def onCreatePartner: SyntheticEvent => Unit = {
          event =>
            event.preventDefault()

            val questionId = self.props.native.record.asInstanceOf[Question].id

            val request = CreatePartnerRequest(
              name = self.state.name,
              logo = self.state.logo,
              link = self.state.link,
              organisationId = self.state.organisationId,
              partnerKind = self.state.partnerKind,
              questionId = questionId,
              weight = self.state.weight
            )

            PartnerService.createPartner(request = request).onComplete {
              case Success(_) =>
                self.setState(
                  _.copy(
                    name = "",
                    logo = None,
                    link = None,
                    partnerKind = "",
                    weight = 0,
                    snackbarOpen = true,
                    snackbarMessage = "Partner successfully created",
                    createPartnerModalOpen = false
                  )
                )
                self.props.wrapped.reloadComponent()
              case Failure(_) =>
                self.setState(_.copy(snackbarOpen = true, snackbarMessage = s"Error while creating partner"))
            }
        }

        def handleOpenModal: SyntheticEvent => Unit = { _ =>
          self.setState(_.copy(createPartnerModalOpen = true))
        }

        def handleCloseModal: SyntheticEvent => Unit = { _ =>
          self.setState(_.copy(createPartnerModalOpen = false))
        }

        <.div()(
          <.CardActions()(<.FlatButton(^.label := "Create partner", ^.primary := true, ^.onClick := handleOpenModal)()),
          <.Dialog(
            ^.title := "Create partner",
            ^.open := self.state.createPartnerModalOpen,
            ^.autoScrollBodyContent := true,
            ^.actionsModal := Seq(
              <.FlatButton(^.label := "Cancel", ^.onClick := handleCloseModal)(),
              <.FlatButton(^.label := "Create partner", ^.primary := true, ^.onClick := onCreatePartner)()
            )
          )(
            <.AutoComplete(
              ^.id := "search-organisation",
              ^.hintText := "Search organisation",
              ^.dataSource := self.state.organisationSearchList,
              ^.dataSourceConfig := DataSourceConfig("organisationName", "id"),
              ^.searchText := self.state.searchOrganisationContent,
              ^.onUpdateInput := handleUpdateOrganisationInput,
              ^.onNewRequest := handleNewOrganisationRequest,
              ^.fullWidth := true,
              ^.popoverProps := Map("canAutoPosition" -> false),
              ^.openOnFocus := true,
              ^.filterAutoComplete := filterAutoComplete,
              ^.menuProps := Map("maxHeight" -> 400)
            )(),
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

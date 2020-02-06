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
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.autoComplete.AutoComplete.AutoCompleteProps
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
                                         errorName: String,
                                         logo: Option[String],
                                         errorLogo: String,
                                         link: Option[String],
                                         errorLink: String,
                                         partnerKind: String,
                                         errorPartnerKind: String,
                                         weight: Double,
                                         errorWeight: String,
                                         organisationId: Option[String],
                                         searchOrganisationContent: String,
                                         partnersList: Seq[Partner],
                                         createPartnerModalOpen: Boolean,
                                         snackbarOpen: Boolean,
                                         snackbarMessage: String)

  lazy val reactClass: ReactClass =
    React.createClass[CreatePartnerComponentProps, CreatePartnerComponentState](
      displayName = "CreatePartnerComponent",
      getInitialState = { _ =>
        CreatePartnerComponentState(
          name = "",
          errorName = "",
          logo = None,
          errorLogo = "",
          link = None,
          errorLink = "",
          partnerKind = "",
          errorPartnerKind = "",
          weight = 0,
          errorWeight = "",
          organisationId = None,
          searchOrganisationContent = "",
          partnersList = Seq.empty,
          createPartnerModalOpen = false,
          snackbarOpen = false,
          snackbarMessage = ""
        )
      },
      componentDidMount = { self =>
        val questionId = self.props.native.record.asInstanceOf[Question].id

        PartnerService.partners(questionId, 50).onComplete {
          case Success(partners) => self.setState(_.copy(partnersList = partners))
          case Failure(_)        =>
        }
      },
      render = { self =>
        def handleNameEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
          val newName: String = event.target.value
          var errorName = ""
          if (newName.isEmpty) {
            errorName = "Name is required"
          }
          self.setState(_.copy(name = newName, errorName = errorName))
        }

        def handleLogoEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
          val value: String = event.target.value
          val newLogo = if (value.isEmpty) None else Some(value)
          var errorLogo = ""
          if (newLogo.isEmpty && self.state.organisationId.isEmpty) {
            errorLogo = "Logo is required"
          }
          self.setState(_.copy(logo = newLogo, errorLogo = errorLogo))
        }

        def handleLinkEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
          val value: String = event.target.value
          val newLink = if (value.isEmpty) None else Some(value)
          var errorLink = ""
          if (newLink.isEmpty && self.state.partnerKind == "FOUNDER") {
            errorLink = "Link is required"
          }
          self.setState(_.copy(link = newLink, errorLink = errorLink))
        }

        def handleWeightEdition: FormSyntheticEvent[HTMLInputElement] => Unit = {
          event =>
            val newWeight: String = event.target.value
            if (newWeight.nonEmpty) {
              if (self.state.partnersList.map(_.weight).contains(newWeight.toDouble)) {
                self.setState(
                  _.copy(
                    weight = newWeight.toDouble,
                    errorWeight = "Another partner is already defined with this weight"
                  )
                )
              } else {
                self.setState(_.copy(weight = newWeight.toDouble, errorWeight = ""))
              }
            } else {
              self.setState(_.copy(weight = 0))
            }
        }

        def onChangePartnerKind: (js.Object, js.UndefOr[Int], String) => Unit = { (_, _, value) =>
          self.setState(_.copy(partnerKind = value, errorPartnerKind = ""))
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

        def onCreatePartner: SyntheticEvent => Unit = {
          _ =>
            var error = false
            var errorName = ""
            var errorLogo = ""
            var errorLink = ""
            var errorPartnerKind = ""
            var errorWeight = ""

            if (self.state.name.isEmpty) {
              errorName = "Name is required"
              error = true
            }
            if (self.state.logo.isEmpty && self.state.organisationId.isEmpty) {
              errorLogo = "Logo is required"
              error = true
            }
            if (self.state.link.isEmpty && self.state.partnerKind == "FOUNDER") {
              errorLink = "Link is required"
              error = true
            }
            if (self.state.partnerKind.isEmpty) {
              errorPartnerKind = "Partner kind is required"
              error = true
            }

            if (!error) {

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
            } else {
              self.setState(
                _.copy(
                  errorName = errorName,
                  errorLogo = errorLogo,
                  errorLink = errorLink,
                  errorPartnerKind = errorPartnerKind,
                  errorWeight = errorWeight
                )
              )
            }
        }

        def handleOpenModal: SyntheticEvent => Unit = { _ =>
          self.setState(_.copy(createPartnerModalOpen = true))
        }

        def handleCloseModal: SyntheticEvent => Unit = { _ =>
          self.setState(_.copy(createPartnerModalOpen = false))
        }

        val logoLabelText = {
          self.state.organisationId match {
            case Some(_) => "Logo"
            case _       => "Logo *"
          }
        }

        val linkLabelText = {
          self.state.partnerKind match {
            case "FOUNDER" => "Link *"
            case _         => "Link"
          }
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
            <.AutoCompleteComponent(
              ^.wrapped := AutoCompleteProps(
                searchRequest = OrganisationService.organisations,
                handleNewRequest = handleNewOrganisationRequest,
                dataSourceConfig = DataSourceConfig("organisationName", "id")
              )
            )(),
            <.TextFieldMaterialUi(
              ^.floatingLabelText := "Name *",
              ^.value := self.state.name,
              ^.onChange := handleNameEdition,
              ^.fullWidth := true,
              ^.errorText := self.state.errorName
            )(),
            <.SelectField(
              ^.floatingLabelText := "Kind *",
              ^.value := self.state.partnerKind,
              ^.onChangeSelect := onChangePartnerKind,
              ^.errorText := self.state.errorPartnerKind
            )(Partner.partnerKindMap.map {
              case (key, value) =>
                <.MenuItem(^.key := key, ^.value := key, ^.primaryText := value)()
            }),
            <.TextFieldMaterialUi(
              ^.floatingLabelText := logoLabelText,
              ^.value := self.state.logo.getOrElse(""),
              ^.onChange := handleLogoEdition,
              ^.fullWidth := true,
              ^.errorText := self.state.errorLogo
            )(),
            <.TextFieldMaterialUi(
              ^.floatingLabelText := linkLabelText,
              ^.value := self.state.link.getOrElse(""),
              ^.onChange := handleLinkEdition,
              ^.fullWidth := true,
              ^.errorText := self.state.errorLink
            )(),
            <.TextFieldMaterialUi(
              ^.floatingLabelText := "Weight *",
              ^.value := self.state.weight,
              ^.`type` := "number",
              ^.onChange := handleWeightEdition,
              ^.fullWidth := true,
              ^.errorText := self.state.errorWeight
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

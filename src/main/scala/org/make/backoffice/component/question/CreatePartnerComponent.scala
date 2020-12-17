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
import org.make.backoffice.component.images.ImageUploadField.{imageDropzone, ImageUploadProps}
import org.make.backoffice.component.images.{ImageFile, ImageUploadFieldStyle}
import org.make.backoffice.facade.DataSourceConfig
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.{Organisation, Partner, Question}
import org.make.backoffice.service.organisation.OrganisationService
import org.make.backoffice.service.partner.{CreatePartnerRequest, PartnerService}
import org.make.backoffice.service.question.QuestionService
import org.scalajs.dom.FormData
import org.scalajs.dom.raw.HTMLInputElement
import scalacss.DevDefaults._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.scalajs.js

object CreatePartnerComponent {

  case class CreatePartnerComponentProps(reloadComponent: () => Unit)
  case class CreatePartnerComponentState(name: String,
                                         errorName: String,
                                         logo: Option[String],
                                         link: Option[String],
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
          link = None,
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
        val questionId = self.props.native.record.asInstanceOf[Question].id

        def handleNameEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
          val newName: String = event.target.value
          var errorName = ""
          if (newName.isEmpty) {
            errorName = "Name is required"
          }
          self.setState(_.copy(name = newName, errorName = errorName))
        }

        def handleLinkEdition: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
          val value: String = event.target.value
          val newLink = if (value.isEmpty) None else Some(value)
          self.setState(_.copy(link = newLink))
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

        def uploadImage: js.Array[ImageFile] => Unit = { files =>
          val file = files.head
          val formData: FormData = new FormData()
          formData.append("data", file)
          QuestionService.uploadImage(questionId, formData).onComplete {
            case Success(url) => self.setState(_.copy(logo = Some(url.path)))
            case Failure(e) =>
              self
                .setState(
                  _.copy(snackbarOpen = true, snackbarMessage = s"Error while uploading image: ${e.getMessage}")
                )
          }
        }

        val updatePictureUrl = { event: FormSyntheticEvent[HTMLInputElement] =>
          val value = event.target.value
          self.setState(_.copy(logo = Some(value)))
        }

        def onCreatePartner: SyntheticEvent => Unit = {
          _ =>
            var error = false
            var errorName = ""
            var errorPartnerKind = ""

            if (self.state.name.isEmpty) {
              errorName = "Name is required"
              error = true
            }
            if (self.state.partnerKind.isEmpty) {
              errorPartnerKind = "Partner kind is required"
              error = true
            }

            if (!error) {

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
                  errorPartnerKind = errorPartnerKind
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
              ^.errorText := self.state.errorPartnerKind,
              ^.fullWidth := true
            )(Partner.partnerKindMap.map {
              case (key, value) =>
                <.MenuItem(^.key := key, ^.value := key, ^.primaryText := value)()
            }),
            <(imageDropzone)(
              ^.wrapped := ImageUploadProps(
                label = "Logo",
                imageUrl = self.state.logo.getOrElse(""),
                uploadImage = uploadImage,
                onChangeImageUrl = updatePictureUrl
              )
            )(),
            <.TextFieldMaterialUi(
              ^.floatingLabelText := "Link",
              ^.value := self.state.link.getOrElse(""),
              ^.onChange := handleLinkEdition,
              ^.fullWidth := true
            )(),
            <.TextFieldMaterialUi(
              ^.floatingLabelText := "Weight *",
              ^.value := self.state.weight,
              ^.`type` := "number",
              ^.onChange := handleWeightEdition,
              ^.fullWidth := true,
              ^.errorText := self.state.errorWeight
            )(),
            <.style()(ImageUploadFieldStyle.render[String])
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

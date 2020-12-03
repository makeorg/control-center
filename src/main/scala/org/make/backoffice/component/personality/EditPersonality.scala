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

package org.make.backoffice.component.personality

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.images.ImageUploadField.ImageUploadFieldProps
import org.make.backoffice.component.images.ImageUploadFieldStyle
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.facade.AdminOnRest.required
import org.make.backoffice.facade.Choice
import org.make.backoffice.service.user.UserService
import org.make.backoffice.util.Configuration
import org.scalajs.dom.raw.FormData
import scalacss.DevDefaults._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object EditPersonality {

  case class EditPersonalityProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private def uploadAvatar: FormData => Future[String] = { data =>
    UserService.adminUploadAvatar(data, "PERSONALITY").map(_.path)
  }

  private lazy val reactClass =
    React
      .createClass[EditPersonalityProps, Unit](
        displayName = "EditPersonality",
        render = self => {

          val genderChoice: js.Array[Choice] =
            js.Array(Choice(id = "M", name = "M"), Choice(id = "F", name = "F"), Choice(id = "Other", name = "Other"))

          <.Edit(
            ^.resource := Resource.personalities,
            ^.location := self.props.location,
            ^.`match` := self.props.`match`,
            ^.hasList := true
          )(
            <.SimpleForm()(
              <.TextInput(
                ^.label := "Email",
                ^.source := "email",
                ^.validate := required,
                ^.allowEmpty := false,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.TextInput(
                ^.label := "First Name",
                ^.source := "firstName",
                ^.validate := required,
                ^.allowEmpty := false,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.TextInput(
                ^.label := "Last Name",
                ^.source := "lastName",
                ^.validate := required,
                ^.allowEmpty := false,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.SelectInput(
                ^.source := "country",
                ^.choices := Configuration.choicesCountry,
                ^.allowEmpty := false,
                ^.validate := required,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.UploadImageComponent(
                ^.wrapped := ImageUploadFieldProps(
                  "avatarUrl",
                  "Avatar URL",
                  ImageUploadFieldStyle.dropzone.htmlClass,
                  ImageUploadFieldStyle.preview.htmlClass,
                  uploadAvatar
                )
              )(),
              <.TextInput(^.label := "Description", ^.source := "description", ^.options := Map("fullWidth" -> true))(),
              <.SelectInput(^.source := "gender", ^.choices := genderChoice, ^.options := Map("fullWidth" -> true))(),
              <.DependentInput(^.dependsOn := "gender", ^.dependsValue := "Other")(
                <.TextInput(^.label := "Gender name", ^.source := "genderName", ^.options := Map("fullWidth" -> true))()
              ),
              <.TextInput(
                ^.label := "Political party",
                ^.source := "politicalParty",
                ^.options := Map("fullWidth" -> true)
              )(),
              <.TextInput(^.label := "Website", ^.source := "website", ^.options := Map("fullWidth" -> true))(),
              <.style()(ImageUploadFieldStyle.render[String])
            )
          )
        }
      )
}

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

package org.make.backoffice.component.organisation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Create._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SaveButton._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.facade.AdminOnRest.Toolbar._
import org.make.backoffice.facade.AdminOnRest.required
import org.make.backoffice.util.Configuration

object CreateOrganisation {
  case class CreateOrganisationProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val toolbar: ReactElement =
    <.Toolbar()(
      <.SaveButton(^.label := "Save", ^.redirect := "list", ^.submitOnEnter := true)(),
      <.SaveButton(^.label := "Save and Add", ^.redirect := false, ^.submitOnEnter := false, ^.raised := false)()
    )

  private lazy val reactClass =
    React
      .createClass[CreateOrganisationProps, Unit](
        displayName = "CreateOrganisation",
        render = self => {

          <.Create(^.resource := Resource.organisations, ^.location := self.props.location, ^.hasList := true)(
            <.SimpleForm(^.toolbar := toolbar)(
              <.TextInput(
                ^.label := "organisation name",
                ^.source := "organisationName",
                ^.validate := required,
                ^.allowEmpty := false,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.TextInput(
                ^.source := "email",
                ^.validate := required,
                ^.allowEmpty := false,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.TextInput(^.source := "password", ^.options := Map("fullWidth" -> true))(),
              <.TextInput(^.label := "avatar url", ^.source := "avatarUrl", ^.options := Map("fullWidth" -> true))(),
              <.TextInput(^.label := "Description", ^.source := "description", ^.options := Map("fullWidth" -> true))(),
              <.SelectInput(
                ^.source := "country",
                ^.choices := Configuration.choicesCountry,
                ^.allowEmpty := false,
                ^.validate := required
              )(),
              Configuration.choiceLanguage.map {
                case (country, languages) =>
                  <.DependentInput(^.dependsOn := "country", ^.dependsValue := country)(
                    <.SelectInput(
                      ^.source := "language",
                      ^.choices := languages,
                      ^.allowEmpty := false,
                      ^.validate := required
                    )()
                  )
              },
              <.TextInput(^.source := "website", ^.options := Map("fullWidth" -> true))()
            )
          )
        }
      )
}

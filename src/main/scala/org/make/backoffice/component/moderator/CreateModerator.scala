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

package org.make.backoffice.component.moderator

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Create._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.facade.AdminOnRest.required
import org.make.backoffice.facade.Choice
import org.make.backoffice.model.Role
import org.make.backoffice.util.Configuration

object CreateModerator {

  case class CreateModeratorProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[CreateModeratorProps, Unit](
        displayName = "CreateModerator",
        render = self => {

          val rolesChoice: Seq[Choice] =
            Role.roles.values.map(role => Choice(id = role.shortName, name = role.shortName)).toSeq

          <.Create(^.resource := Resource.moderators, ^.location := self.props.location, ^.hasList := true)(
            <.SimpleForm()(
              <.TextInput(
                ^.label := "email",
                ^.source := "email",
                ^.validate := required,
                ^.allowEmpty := false,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.TextInput(
                ^.label := "firtsname",
                ^.source := "firstName",
                ^.validate := required,
                ^.allowEmpty := false,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.TextInput(^.label := "lastname", ^.source := "lastName", ^.options := Map("fullWidth" -> true))(),
              <.SelectArrayInput(
                ^.label := "roles",
                ^.source := "roles",
                ^.choices := rolesChoice,
                ^.options := Map("fullWidth" -> true)
              )(),
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
              }
            )
          )
        }
      )
}

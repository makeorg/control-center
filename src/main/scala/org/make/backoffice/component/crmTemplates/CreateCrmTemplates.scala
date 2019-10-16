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

package org.make.backoffice.component.crmTemplates

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Create._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.util.Configuration

object CreateCrmTemplates {

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[RouterProps, Unit](
        displayName = "CreateCrmTemplates",
        render = { self =>
          <.Create(^.resource := Resource.crmTemplates, ^.location := self.props.location, ^.hasList := true)(
            <.SimpleForm()(
              <.ReferenceInput(
                ^.source := "questionId",
                ^.label := "question",
                ^.translateLabel := ((label: String) => label),
                ^.reference := Resource.questions,
                ^.allowEmpty := true,
                ^.sort := Map("field" -> "slug", "order" -> "ASC"),
                ^.perPage := 100
              )(<.SelectInput(^.optionText := "slug", ^.options := Map("fullWidth" -> true))()),
              <.SelectInput(
                ^.source := "country",
                ^.choices := Configuration.choicesCountry,
                ^.allowEmpty := true,
                ^.options := Map("fullWidth" -> true)
              )(),
              Configuration.choiceLanguage.map {
                case (country, languages) =>
                  <.DependentInput(^.dependsOn := "country", ^.dependsValue := country)(
                    <.SelectInput(
                      ^.source := "language",
                      ^.choices := languages,
                      ^.allowEmpty := true,
                      ^.options := Map("fullWidth" -> true)
                    )()
                  )
              },
              <.TextInput(^.source := "registration", ^.options := Map("fullWidth" -> true))(),
              <.TextInput(^.source := "welcome", ^.options := Map("fullWidth" -> true), ^.label := "Welcome")(),
              <.TextInput(
                ^.source := "proposalAccepted",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Proposal Accepted"
              )(),
              <.TextInput(
                ^.source := "proposalRefused",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Proposal Refused"
              )(),
              <.TextInput(
                ^.source := "forgottenPassword",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Forgotten Password"
              )(),
              <.TextInput(
                ^.source := "proposalAcceptedOrganisation",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Proposal Accepted Organisation"
              )(),
              <.TextInput(
                ^.source := "proposalRefusedOrganisation",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Proposal Refused Organisation"
              )(),
              <.TextInput(
                ^.source := "forgottenPasswordOrganisation",
                ^.options := Map("fullWidth" -> true),
                ^.label := "Forgotten Password Organisation"
              )()
            )
          )
        }
      )

}

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

package org.make.backoffice.component.idea

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Create._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.facade.Choice
import org.make.backoffice.facades.AdminOnRest.required
import org.make.backoffice.util.Configuration

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object CreateIdea {

  case class CreateProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[CreateProps, Unit](
        displayName = "CreateIdea",
        render = (self) => {

          // toDo: get from configuration when available
          val countryChoices: js.Array[Choice] = Seq(
            Choice(id = "FR", name = "France"),
            Choice(id = "IT", name = "Italy"),
            Choice(id = "GB", name = "United Kingdom")
          ).toJSArray
          val languagesByCountry: Map[String, js.Array[Choice]] = Map(
            "FR" -> Seq(Choice(id = "fr", name = "French")).toJSArray,
            "IT" -> Seq(Choice(id = "it", name = "Italian")).toJSArray,
            "GB" -> Seq(Choice(id = "en", name = "English")).toJSArray
          )

          <.Create(^.resource := Resource.ideas, ^.location := self.props.location)(
            <.SimpleForm()(
              <.TextInput(
                ^.source := "name",
                ^.allowEmpty := false,
                ^.validate := required,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.SelectInput(
                ^.source := "country",
                ^.choices := countryChoices,
                ^.allowEmpty := false,
                ^.validate := required
              )(),
              languagesByCountry.map {
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
              <.SelectInput(
                ^.label := "Theme",
                ^.source := "themeId",
                ^.allowEmpty := true,
                ^.choices := Configuration.choicesThemeFilter
              )(),
              <.ReferenceInput(
                ^.label := "Operation",
                ^.source := "operationId",
                ^.reference := Resource.operations,
                ^.allowEmpty := true
              )(<.SelectInput(^.optionText := "slug")()),
              <.TextInput(^.source := "question", ^.options := Map("fullWidth" -> true))()
            )
          )
        }
      )
}

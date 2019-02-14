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
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Create._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.facade.AdminOnRest.required
import org.make.backoffice.util.{Configuration, DateParser}

import scala.scalajs.js

object CreateQuestion {

  case class CreateProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[CreateProps, Unit](
        displayName = "CreateQuestion",
        render = self => {

          <.Create(^.resource := Resource.operationsOfQuestions, ^.location := self.props.location)(
            <.SimpleForm()(
              <.DateInput(
                ^.label := "Start Date",
                ^.translateLabel := ((label: String) => label),
                ^.source := "startDate",
                ^.parse := ((date: js.UndefOr[String]) => date.map(DateParser.parseDate))
              )(),
              <.DateInput(
                ^.label := "End Date",
                ^.translateLabel := ((label: String) => label),
                ^.source := "endDate",
                ^.parse := ((date: js.UndefOr[String]) => date.map(DateParser.parseDate))
              )(),
              <.ReferenceInput(
                ^.label := "Operation",
                ^.translateLabel := ((label: String) => label),
                ^.source := "operationId",
                ^.reference := Resource.operations,
                ^.sort := Map("field" -> "slug", "order" -> "ASC"),
                ^.perPage := 100,
                ^.allowEmpty := false,
                ^.validate := required
              )(<.SelectInput(^.optionText := "slug")()),
              <.TextInput(
                ^.label := "Operation Title",
                ^.translateLabel := ((label: String) => label),
                ^.source := "operationTitle",
                ^.allowEmpty := false,
                ^.validate := required,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.SelectInput(
                ^.source := "country",
                ^.choices := Configuration.choicesCountry,
                ^.allowEmpty := false,
                ^.validate := required,
                ^.options := Map("fullWidth" -> true)
              )(),
              Configuration.choiceLanguage.map {
                case (country, languages) =>
                  <.DependentInput(^.dependsOn := "country", ^.dependsValue := country)(
                    <.SelectInput(
                      ^.source := "language",
                      ^.choices := languages,
                      ^.allowEmpty := false,
                      ^.validate := required,
                      ^.options := Map("fullWidth" -> true)
                    )()
                  )
              },
              <.TextInput(
                ^.source := "question",
                ^.allowEmpty := false,
                ^.validate := required,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.TextInput(
                ^.label := "Slug",
                ^.translateLabel := ((label: String) => label),
                ^.source := "questionSlug",
                ^.allowEmpty := false,
                ^.validate := required,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.BooleanInput(^.label := "Can propose", ^.source := "canPropose")()
            )
          )
        }
      )
}

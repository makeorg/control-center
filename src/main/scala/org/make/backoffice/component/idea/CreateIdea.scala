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
import org.make.backoffice.facade.AdminOnRest.required

object CreateIdea {

  case class CreateProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[CreateProps, Unit](
        displayName = "CreateIdea",
        render = self => {

          <.Create(^.resource := Resource.ideas, ^.location := self.props.location)(
            <.SimpleForm()(
              <.TextInput(
                ^.source := "name",
                ^.allowEmpty := false,
                ^.validate := required,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.ReferenceInput(
                ^.label := "Question",
                ^.translateLabel := ((label: String) => label),
                ^.source := "questionId",
                ^.reference := Resource.questions,
                ^.sort := Map("field" -> "slug", "order" -> "ASC"),
                ^.perPage := 100,
                ^.allowEmpty := true
              )(<.SelectInput(^.optionText := "slug")())
            )
          )
        }
      )
}

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

package org.make.backoffice.component.tag

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
import org.make.backoffice.facade.Choice

import scala.scalajs.js

object CreateTag {
  case class CreateTagProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val toolbar: ReactElement =
    <.Toolbar()(
      <.SaveButton(^.label := "Save", ^.redirect := "list", ^.submitOnEnter := true)(),
      <.SaveButton(^.label := "Save and Add", ^.redirect := false, ^.submitOnEnter := false, ^.raised := false)()
    )

  private lazy val reactClass =
    React
      .createClass[CreateTagProps, Unit](
        displayName = "CreateTag",
        render = self => {

          def tagDisplayChoice: js.Array[Choice] = {
            js.Array(Choice("INHERIT", "Inherit"), Choice("HIDDEN", "Hidden"), Choice("DISPLAYED", "Displayed"))
          }

          <.Create(^.resource := Resource.tags, ^.location := self.props.location, ^.hasList := true)(
            <.SimpleForm(^.toolbar := toolbar)(
              <.TextInput(
                ^.source := "label",
                ^.validate := required,
                ^.allowEmpty := false,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.ReferenceInput(
                ^.label := "Tag Type",
                ^.translateLabel := ((label: String) => label),
                ^.source := "tagTypeId",
                ^.reference := Resource.tagType,
                ^.allowEmpty := false
              )(<.SelectInput(^.optionText := "label")()),
              <.ReferenceInput(
                ^.label := "Question",
                ^.translateLabel := ((label: String) => label),
                ^.source := "questionId",
                ^.reference := Resource.questions,
                ^.perPage := 100,
                ^.sort := Map("field" -> "slug", "order" -> "ASC"),
                ^.allowEmpty := true
              )(<.SelectInput(^.optionText := "slug")()),
              <.SelectInput(
                ^.label := "Display",
                ^.source := "display",
                ^.allowEmpty := true,
                ^.choices := tagDisplayChoice
              )(),
              <.NumberInput(^.source := "weight", ^.allowEmpty := true)()
            )
          )
        }
      )
}

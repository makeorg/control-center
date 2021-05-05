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
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Filter._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.List._

object IdeaList {

  case class ListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[ListProps, Unit](
        displayName = "IdeaList",
        render = self => {
          <.List(
            ^.perPage := 200,
            ^.title := "Ideas",
            ^.location := self.props.location,
            ^.resource := Resource.ideas,
            ^.hasCreate := true,
            ^.filters := ideaFilters(),
            ^.sortList := Map("field" -> "name", "order" -> "ASC")
          )(
            <.Datagrid()(
              <.EditButton()(),
              <.TextField(^.source := "name", ^.sortable := true)(),
              <.ReferenceField(
                ^.source := "questionId",
                ^.label := "question",
                ^.reference := Resource.questions,
                ^.linkType := false,
                ^.allowEmpty := true,
                ^.sortable := false
              )(<.TextField(^.source := "slug")())
            )
          )
        }
      )

  def ideaFilters(): ReactElement = {
    <.Filter(^.resource := Resource.ideas)(
      Seq(
        <.TextInput(^.label := "Name", ^.source := "name", ^.alwaysOn := true)(),
        <.ReferenceInput(
          ^.label := "Question",
          ^.translateLabel := ((label: String) => label),
          ^.source := "questionId",
          ^.reference := Resource.questions,
          ^.sortList := Map("field" -> "slug", "order" -> "ASC"),
          ^.perPage := 500,
          ^.alwaysOn := true
        )(<.SelectInput(^.optionText := "slug")()),
      )
    )
  }

}

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
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
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

object TagList {

  case class TagListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[TagListProps, Unit](
        displayName = "TagList",
        render = self => {
          <.List(
            ^.perPage := 50,
            ^.title := "Tags",
            ^.sortList := Map("field" -> "label", "order" -> "ASC"),
            ^.location := self.props.location,
            ^.resource := Resource.tags,
            ^.hasCreate := true,
            ^.filters := tagFilters(),
            ^.filter := Map("language" -> "fr", "country" -> "FR")
          )(
            <.Datagrid()(
              <.EditButton()(),
              <.TextField(^.source := "label")(),
              <.ReferenceField(
                ^.source := "questionId",
                ^.label := "question",
                ^.reference := Resource.questions,
                ^.linkType := false,
                ^.allowEmpty := true,
                ^.sortable := false
              )(<.TextField(^.source := "slug")()),
              <.TextField(^.source := "country", ^.sortable := false)(),
              <.ReferenceField(
                ^.source := "tagTypeId",
                ^.label := "Tag Type",
                ^.reference := Resource.tagType,
                ^.linkType := false,
                ^.allowEmpty := true,
                ^.sortable := false
              )(<.TextField(^.source := "label")()),
              <.NumberField(^.source := "weight")()
            )
          )
        }
      )

  def tagFilters(): ReactElement = {
    <.Filter(^.resource := Resource.tags)(
      <.TextInput(^.label := "Label", ^.source := "label", ^.alwaysOn := true)(),
      <.ReferenceInput(
        ^.label := "Tag Type",
        ^.translateLabel := ((label: String) => label),
        ^.source := "tagTypeId",
        ^.reference := Resource.tagType,
        ^.alwaysOn := true
      )(<.SelectInput(^.optionText := "label")()),
      <.ReferenceInput(
        ^.label := "Question",
        ^.translateLabel := ((label: String) => label),
        ^.source := "questionId",
        ^.sortList := Map("field" -> "slug", "order" -> "ASC"),
        ^.reference := Resource.questions,
        ^.perPage := 100,
        ^.alwaysOn := true,
        ^.allowEmpty := true
      )(<.SelectInput(^.optionText := "slug")())
    )
  }
}

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
import org.make.backoffice.util.DateParser

import scala.scalajs.js

object QuestionsList {

  case class ListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[ListProps, Unit](
        displayName = "QuestionsList",
        render = self => {
          <.List(
            ^.perPage := 20,
            ^.title := "Questions",
            ^.location := self.props.location,
            ^.resource := Resource.operationsOfQuestions,
            ^.hasCreate := true,
            ^.filters := questionFilters(),
            ^.sort := Map("field" -> "operation_title", "order" -> "ASC")
          )(
            <.Datagrid()(
              <.EditButton()(),
              <.TextField(
                ^.label := "Operation",
                ^.translateLabel := ((label: String) => label),
                ^.source := "operationTitle",
                ^.sortable := true
              )(),
              <.TextField(^.source := "question", ^.sortable := true)(),
              <.TextField(^.source := "country", ^.sortable := true)(),
              <.TextField(^.source := "language", ^.sortable := true)()
            )
          )
        }
      )

  def questionFilters(): ReactElement = {
    <.Filter(^.resource := Resource.operationsOfQuestions)(
      Seq(
        <.DateInput(
          ^.label := "Open at",
          ^.source := "openAt",
          ^.parse := ((date: js.UndefOr[String]) => date.map(DateParser.parseDate)),
          ^.alwaysOn := true
        )(),
        <.ReferenceInput(
          ^.label := "Question",
          ^.translateLabel := ((label: String) => label),
          ^.source := "id",
          ^.reference := Resource.questions,
          ^.sort := Map("field" -> "slug", "order" -> "ASC"),
          ^.perPage := 100,
          ^.alwaysOn := true
        )(<.SelectInput(^.optionText := "slug")()),
        <.ReferenceInput(
          ^.label := "Operation",
          ^.translateLabel := ((label: String) => label),
          ^.source := "operationId",
          ^.reference := Resource.operations,
          ^.sort := Map("field" -> "slug", "order" -> "ASC"),
          ^.perPage := 100,
          ^.alwaysOn := true
        )(<.SelectInput(^.optionText := "slug")())
      )
    )
  }

}

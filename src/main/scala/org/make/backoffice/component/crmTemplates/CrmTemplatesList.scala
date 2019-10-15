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
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Filter._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.List._

object CrmTemplatesList {

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[RouterProps, Unit](
        displayName = "crmTemplates",
        render = { self =>
          <.List(
            ^.perPage := 50,
            ^.title := "Crm Templates",
            ^.location := self.props.location,
            ^.resource := Resource.crmTemplates,
            ^.hasCreate := true,
            ^.filters := crmTemplatesFilters()
          )(
            <.Datagrid()(
              <.EditButton()(),
              <.ReferenceField(
                ^.source := "questionId",
                ^.label := "question",
                ^.translateLabel := ((label: String) => label),
                ^.reference := Resource.questions,
                ^.linkType := false,
                ^.allowEmpty := true,
                ^.sortable := false
              )(<.TextField(^.source := "slug")()),
              <.TextField(^.source := "locale", ^.sortable := false)(),
              <.TextField(^.source := "registration", ^.sortable := false)(),
              <.TextField(^.source := "welcome", ^.sortable := false, ^.label := "Welcome")(),
              <.TextField(^.source := "proposalAccepted", ^.sortable := false, ^.label := "Proposal Accepted")(),
              <.TextField(^.source := "proposalRefused", ^.sortable := false, ^.label := "Proposal Refused")(),
              <.TextField(^.source := "forgottenPassword", ^.sortable := false, ^.label := "Forgotten Password")(),
              <.TextField(
                ^.source := "proposalAcceptedOrganisation",
                ^.sortable := false,
                ^.label := "Proposal Accepted Orga"
              )(),
              <.TextField(
                ^.source := "proposalRefusedOrganisation",
                ^.sortable := false,
                ^.label := "Proposal Refused Orga"
              )(),
              <.TextField(
                ^.source := "forgottenPasswordOrganisation",
                ^.sortable := false,
                ^.label := "Forgotten Password Orga"
              )()
            )
          )
        }
      )

  def crmTemplatesFilters(): ReactElement = {
    <.Filter(^.resource := Resource.crmTemplates)(
      Seq(
        <.ReferenceInput(
          ^.label := "Question",
          ^.translateLabel := ((label: String) => label),
          ^.source := "questionId",
          ^.reference := Resource.questions,
          ^.sortList := Map("field" -> "slug", "order" -> "ASC"),
          ^.perPage := 100,
          ^.alwaysOn := true
        )(<.SelectInput(^.optionText := "slug")())
      )
    )
  }

}

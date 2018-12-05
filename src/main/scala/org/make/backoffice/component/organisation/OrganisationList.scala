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

package org.make.backoffice.component.organisation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.List._

object OrganisationList {

  case class OrganisationListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[OrganisationListProps, Unit](
        displayName = "OrganisationList",
        render = self => {
          <.List(
            ^.perPage := 50,
            ^.title := "Organisations",
            ^.location := self.props.location,
            ^.sort := Map("field" -> "organisation_name", "order" -> "ASC"),
            ^.resource := Resource.organisations,
            ^.hasCreate := true
          )(
            <.Datagrid()(
              <.EditButton()(),
              <.TextField(^.source := "organisationName", ^.label := "organisation name")(),
              <.TextField(^.source := "email")(),
              <.TextField(^.source := "country", ^.sortable := false)()
            )
          )
        }
      )
}

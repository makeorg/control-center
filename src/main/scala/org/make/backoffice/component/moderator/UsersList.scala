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

package org.make.backoffice.component.moderator

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.Filter._
import org.make.backoffice.facade.AdminOnRest.List._
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.facade.Choice
import org.make.backoffice.model.{Moderator, Role}

object UsersList {

  case class UsersListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[UsersListProps, Unit](
        displayName = "UsersList",
        render = self => {
          <.List(
            ^.perPage := 50,
            ^.title := "Users",
            ^.location := self.props.location,
            ^.sortList := Map("field" -> "first_name", "order" -> "ASC"),
            ^.resource := Resource.users,
            ^.filters := usersFilters(),
            ^.hasCreate := true
          )(
            <.Datagrid()(
              <.EditButton()(),
              <.TextField(^.source := "firstName", ^.label := "firstname")(),
              <.TextField(^.source := "lastName", ^.label := "lastname")(),
              <.TextField(^.source := "email")(),
              <.FunctionField(^.label := "roles", ^.sortable := false, ^.render := { record =>
                val moderator = record.asInstanceOf[Moderator]
                moderator.roles.map(role => <.Chip(^.style := Map("marginTop" -> "5px"))(role))
              })(),
              <.TextField(^.source := "country", ^.sortable := false)()
            )
          )
        }
      )

  def usersFilters(): ReactElement = {
    val rolesChoice: Seq[Choice] =
      Role.roles.values.map(role => Choice(id = role.shortName, name = role.shortName)).toSeq

    <.Filter(^.resource := Resource.users)(
      Seq(
        <.TextInput(^.label := "Email", ^.source := "email", ^.alwaysOn := true)(),
        <.SelectInput(
          ^.label := "roles",
          ^.source := "role",
          ^.choices := rolesChoice,
          ^.alwaysOn := true,
          ^.allowEmpty := true
        )()
      )
    )
  }
}

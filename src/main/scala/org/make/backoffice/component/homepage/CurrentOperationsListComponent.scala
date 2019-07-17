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

package org.make.backoffice.component.homepage

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.Location
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.List._
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.AdminOnRest.DeleteButton._
import org.make.backoffice.facade.AdminOnRest.Fields._

object CurrentOperationsListComponent {

  case class CurrentOperationsProps(location: Location)

  lazy val reactClass: ReactClass =
    React
      .createClass[CurrentOperationsProps, Unit](
        displayName = "CurrentOperationsList",
        render = self => {
          <.List(
            ^.title := "Current operations",
            ^.resource := Resource.homepage,
            ^.hasCreate := true,
            ^.pagination := null,
            ^.location := self.props.wrapped.location
          )(
            <.Datagrid()(
              <.EditButton.empty,
              <.ReferenceField(
                ^.source := "questionId",
                ^.label := "question",
                ^.reference := Resource.questions,
                ^.linkType := false,
                ^.allowEmpty := true
              )(<.TextField(^.source := "slug")()),
              <.TextField(^.source := "label")(),
              <.TextField(^.source := "description")(),
              <.DeleteButton.empty
            )
          )
        }
      )

}

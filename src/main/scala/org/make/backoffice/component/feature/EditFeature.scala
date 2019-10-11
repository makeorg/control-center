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

package org.make.backoffice.component.feature

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SimpleForm._
import org.make.backoffice.facade.AdminOnRest.required

object EditFeature {

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[RouterProps, Unit](
        displayName = "CreateFeature",
        render = { self =>
          <.Edit(
            ^.hasList := true,
            ^.resource := Resource.features,
            ^.location := self.props.location,
            ^.`match` := self.props.`match`
          )(
            <.SimpleForm()(
              <.TextInput(
                ^.source := "name",
                ^.allowEmpty := false,
                ^.validate := required,
                ^.options := Map("fullWidth" -> true)
              )(),
              <.TextInput(
                ^.source := "slug",
                ^.allowEmpty := false,
                ^.validate := required,
                ^.options := Map("fullWidth" -> true)
              )()
            )
          )
        }
      )

}

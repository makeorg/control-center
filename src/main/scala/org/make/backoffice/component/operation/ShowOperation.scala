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

package org.make.backoffice.component.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.Show._
import org.make.backoffice.facade.AdminOnRest.SimpleShowLayout._
import org.make.backoffice.model.Operation

object ShowOperation {

  case class ShowOperationProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[ShowOperationProps, Unit](
        displayName = "ShowOperation",
        render = self => {
          <.Show(
            ^.resource := Resource.operations,
            ^.location := self.props.location,
            ^.`match` := self.props.`match`,
            ^.hasList := true
          )(
            <.SimpleShowLayout()(
              <.TextField(^.source := "slug")(),
              <.TextField(
                ^.label := "Default language",
                ^.translateLabel := ((label: String) => label),
                ^.source := "defaultLanguage"
              )(),
              <.FunctionField(^.label := "Operation Kind", ^.translateLabel := ((label: String) => label), ^.render := {
                record =>
                  val operation = record.asInstanceOf[Operation]
                  Operation.kindMap(operation.operationKind)
              })(),
              <.TextField(^.source := "allowedSources")()
            )
          )
        }
      )
}

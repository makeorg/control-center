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

package org.make.backoffice.component

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.facade.ViewTitle._
import org.make.backoffice.service.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Dashboard {

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React.createClass[RouterProps, Unit](
      displayName = "Dashboard",
      componentDidMount = self =>
        UserService.me.onComplete {
          case Success(_) =>
          case Failure(_) => self.props.history.push("/login")
      },
      render = _ => {
        <.div()(
          <.Card()(<.ViewTitle(^.title := "Dashboard")()),
          <.br.empty,
          <.StartModeration.empty,
          <.br.empty,
          <.StartEnrich.empty
        )
      }
    )
}

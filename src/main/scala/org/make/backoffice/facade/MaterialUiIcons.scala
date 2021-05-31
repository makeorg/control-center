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

package org.make.backoffice.facade

import io.github.shogowada.scalajs.reactjs
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.backoffice.facade.MaterialUi._

import scala.scalajs.js

object MaterialUiIcons {

  val proposalSvgPath =
    "M20 2H4c-1.1 0-1.99.9-1.99 2L2 22l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zM6 9h12v2H6V9zm8 5H6v-2h8v2zm4-6H6V6h12v2z"
  val tagSvgPath =
    "M17.63 5.84C17.27 5.33 16.67 5 16 5L5 5.01C3.9 5.01 3 5.9 3 7v10c0 1.1.9 1.99 2 1.99L16 19c.67 0 1.27-.33 1.63-.84L22 12l-4.37-6.16z"
  val ideaSvgPath =
    "M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96z"
  val ideaMappingSvgPath =
    "M4 14h4v-4H4v4zm0 5h4v-4H4v4zM4 9h4V5H4v4zm5 5h12v-4H9v4zm0 5h12v-4H9v4zM9 5v4h12V5H9z"
  val saveSvgPath =
    "M17 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V7l-4-4zm-5 16c-1.66 0-3-1.34-3-3s1.34-3 3-3 3 1.34 3 3-1.34 3-3 3zm3-10H5V5h10v4z"
  val personalitiesSvgPath =
    "M15.89,8.11C15.5,7.72,14.83,7,13.53,7c-0.21,0-1.42,0-2.54,0C8.24,6.99,6,4.75,6,2H4c0,3.16,2.11,5.84,5,6.71V22h2v-6h2 v6h2V10.05L18.95,14l1.41-1.41L15.89,8.11z"
  val topIdeasSvgPath =
    "M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z"
  val checkBoxSvgPath =
    "M19 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.11 0 2-.9 2-2V5c0-1.1-.89-2-2-2zm-9 14l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"

  val proposalIcon: ReactElement =
    <.SvgIcon()(reactjs.React.createElement("path", js.Dictionary("d" -> proposalSvgPath)))

  val tagIcon: ReactElement = <.SvgIcon()(reactjs.React.createElement("path", js.Dictionary("d" -> tagSvgPath)))

  val ideaIcon: ReactElement = <.SvgIcon()(reactjs.React.createElement("path", js.Dictionary("d" -> ideaSvgPath)))

  val ideaMappingIcon: ReactElement =
    <.SvgIcon()(reactjs.React.createElement("path", js.Dictionary("d" -> ideaMappingSvgPath)))

  val personalitiesIcon: ReactElement =
    <.SvgIcon()(
      reactjs.React.createElement("circle", js.Dictionary("cx" -> "12", "cy" -> "4", "r" -> "2")),
      reactjs.React.createElement("path", js.Dictionary("d" -> personalitiesSvgPath))
    )

  val topIdeasIcon: ReactElement =
    <.SvgIcon()(reactjs.React.createElement("path", js.Dictionary("d" -> topIdeasSvgPath)))

  def checkBoxWithColorIcon(color: String): ReactElement =
    <.SvgIcon(^.style := Map("fill" -> color))(
      reactjs.React.createElement("path", js.Dictionary("d" -> checkBoxSvgPath))
    )

}

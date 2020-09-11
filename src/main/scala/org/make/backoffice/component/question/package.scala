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

import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.backoffice.facade.AdminOnRest.Fields.{FieldsVirtualDOMAttributes, FieldsVirtualDOMElements}
import org.make.backoffice.facade.MaterialUi.MaterialUiVirtualDOMElements
import org.make.backoffice.model.{Country, Question}

package object question {

  val DisplayCountries: ReactElement = <.FunctionField(
    ^.label := "countries",
    ^.render := { record =>
      record
        .asInstanceOf[Question]
        .countries
        .map(
          country =>
            <.Chip(^.key := country, ^.style := Map("display" -> "inline-flex", "margin" -> "5px"))(
              Country.getCountryNameByCountryCode(country).getOrElse(country)
          )
        )
    }
  )()

}

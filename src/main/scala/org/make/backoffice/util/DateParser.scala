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

package org.make.backoffice.util
import java.time.ZonedDateTime

import scala.scalajs.js

object DateParser {

  def parseDate(date: String): String = {
    val offset = new js.Date().getTimezoneOffset()
    val localDate = ZonedDateTime.parse(date).toLocalDate

    // negative offset means that the date got converted to previous day
    if (offset < 0) {
      localDate.plusDays(1).toString
    } else {
      localDate.toString
    }
  }

}

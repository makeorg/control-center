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

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZonedDateTime}

import scala.scalajs.js.Date

object JSConverters {

  implicit class JSRichZonedDateTime(val zonedDateTime: ZonedDateTime) extends AnyVal {
    @inline final def toJSDate: Double =
      Date.parse(zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
  }

  implicit class JSRichLocalDate(val localDate: LocalDate) extends AnyVal {
    @inline final def toJSDate: Double =
      Date.parse(localDate.toString)
  }
}

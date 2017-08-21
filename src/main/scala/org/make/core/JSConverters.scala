package org.make.core

import java.time.{LocalDate, ZonedDateTime}
import java.time.format.DateTimeFormatter

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

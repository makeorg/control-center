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

package org.make.backoffice.model

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

@js.native
trait CountryConfiguration extends js.Object {
  val countryCode: String
  val defaultLanguage: String
  val supportedLanguages: js.Array[String]
}

object CountryConfiguration {
  def apply(countryCode: String, defaultLanguage: String, supportedLanguages: Seq[String]): CountryConfiguration =
    js.Dynamic
      .literal(
        countryCode = countryCode,
        defaultLanguage = defaultLanguage,
        supportedLanguages = supportedLanguages.toJSArray
      )
      .asInstanceOf[CountryConfiguration]
}

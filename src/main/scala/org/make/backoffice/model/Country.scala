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

object Country {
  private val mapping = Map(
    "FR" -> "France",
    "IT" -> "Italy",
    "GB" -> "United Kingdom",
    "DE" -> "Germany",
    "AT" -> "Austria",
    "BE" -> "Belgium",
    "BR" -> "Brazil",
    "BG" -> "Bulgaria",
    "CA" -> "Canada",
    "CH" -> "China",
    "HR" -> "Croatia",
    "CY" -> "Cyprus",
    "CZ" -> "Czechia",
    "DK" -> "Denmark",
    "EE" -> "Estonia",
    "ES" -> "Spain",
    "FI" -> "Finland",
    "GR" -> "Greece",
    "HU" -> "Hungary",
    "IN" -> "India",
    "IE" -> "Ireland",
    "JP" -> "Japan",
    "LT" -> "Lithuania",
    "LU" -> "Luxembourg",
    "LV" -> "Latvia",
    "MT" -> "Malta",
    "NL" -> "Netherlands",
    "NO" -> "Norway",
    "PL" -> "Poland",
    "PT" -> "Portugal",
    "RO" -> "Romania",
    "RU" -> "Russia",
    "SE" -> "Sweden",
    "SI" -> "Slovakia",
    "SK" -> "Slovenia",
    "CH" -> "Switzerland",
    "US" -> "United States"
  )

  def getCountryNameByCountryCode(countryCode: String): Option[String] = {
    mapping.get(countryCode)
  }
}

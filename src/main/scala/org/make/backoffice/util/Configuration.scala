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

import org.make.backoffice.facade.Choice
import org.make.backoffice.model.{Country, Language, _}

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object Configuration extends CirceClassFormatters {

  val toEnrichMinScore = "1.3"
  val toEnrichMinVotesCount = "200"

  val proposalMaxLength: Int = 140
  val defaultLanguage: String = "fr"
  val supportedCountries: Seq[CountryConfiguration] = Seq(
    CountryConfiguration(countryCode = "FR", defaultLanguage = "fr", supportedLanguages = Seq("fr")),
    CountryConfiguration(countryCode = "IT", defaultLanguage = "it", supportedLanguages = Seq("it")),
    CountryConfiguration(countryCode = "GB", defaultLanguage = "en", supportedLanguages = Seq("en")),
    CountryConfiguration(countryCode = "DE", defaultLanguage = "de", supportedLanguages = Seq("de"))
  )
  val reasonsForRefusal: Seq[String] =
    Seq(
      "Incomprehensible",
      "Off-topic",
      "Partisan",
      "Legal",
      "Advertising",
      "MultipleIdeas",
      "InvalidLanguage",
      "Rudeness",
      "Test",
      "Other"
    )

  def choicesCountry: js.Array[Choice] = {
    supportedCountries.map { supportedCountry =>
      Choice(
        supportedCountry.countryCode,
        Country
          .getCountryNameByCountryCode(supportedCountry.countryCode)
          .getOrElse(supportedCountry.countryCode)
      )
    }.toJSArray
  }

  def choiceLanguage: Map[String, js.Array[Choice]] = {
    supportedCountries.map { supportedCountry =>
      supportedCountry.countryCode -> supportedCountry.supportedLanguages.map(
        supportedLanguage =>
          Choice(
            supportedLanguage,
            Language.getLanguageNameFromLanguageCode(supportedLanguage).getOrElse(supportedLanguage)
        )
      )
    }.toMap
  }
}

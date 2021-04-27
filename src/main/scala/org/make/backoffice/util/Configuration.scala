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

  val toEnrichMinScore = "0"
  val toEnrichMinVotesCount = "0"

  val proposalMaxLength: Int = 140
  val proposalMinLength: Int = 12
  val defaultLanguage: String = "fr"
  val supportedCountries: Seq[CountryConfiguration] = Seq(
    CountryConfiguration(countryCode = "AT", defaultLanguage = "de", supportedLanguages = Seq("de")),
    CountryConfiguration(countryCode = "BE", defaultLanguage = "nl", supportedLanguages = Seq("fr", "nl")),
    CountryConfiguration(countryCode = "BG", defaultLanguage = "bg", supportedLanguages = Seq("bg")),
    CountryConfiguration(countryCode = "BR", defaultLanguage = "pt", supportedLanguages = Seq("pt")),
    CountryConfiguration(countryCode = "CA", defaultLanguage = "en", supportedLanguages = Seq("fr", "en")),
    CountryConfiguration(countryCode = "CN", defaultLanguage = "en", supportedLanguages = Seq("en")),
    CountryConfiguration(countryCode = "HR", defaultLanguage = "hr", supportedLanguages = Seq("hr")),
    CountryConfiguration(countryCode = "CY", defaultLanguage = "el", supportedLanguages = Seq("el")),
    CountryConfiguration(countryCode = "CZ", defaultLanguage = "cs", supportedLanguages = Seq("cs")),
    CountryConfiguration(countryCode = "DK", defaultLanguage = "da", supportedLanguages = Seq("da")),
    CountryConfiguration(countryCode = "EE", defaultLanguage = "et", supportedLanguages = Seq("et")),
    CountryConfiguration(countryCode = "FI", defaultLanguage = "fi", supportedLanguages = Seq("fi")),
    CountryConfiguration(countryCode = "FR", defaultLanguage = "fr", supportedLanguages = Seq("fr")),
    CountryConfiguration(countryCode = "DE", defaultLanguage = "de", supportedLanguages = Seq("de")),
    CountryConfiguration(countryCode = "GR", defaultLanguage = "el", supportedLanguages = Seq("el")),
    CountryConfiguration(countryCode = "HU", defaultLanguage = "hu", supportedLanguages = Seq("hu")),
    CountryConfiguration(countryCode = "IE", defaultLanguage = "en", supportedLanguages = Seq("en")),
    CountryConfiguration(countryCode = "IN", defaultLanguage = "en", supportedLanguages = Seq("en")),
    CountryConfiguration(countryCode = "IT", defaultLanguage = "it", supportedLanguages = Seq("it")),
    CountryConfiguration(countryCode = "JP", defaultLanguage = "en", supportedLanguages = Seq("en")),
    CountryConfiguration(countryCode = "LV", defaultLanguage = "lv", supportedLanguages = Seq("lv")),
    CountryConfiguration(countryCode = "LT", defaultLanguage = "lt", supportedLanguages = Seq("lt")),
    CountryConfiguration(countryCode = "LU", defaultLanguage = "fr", supportedLanguages = Seq("fr")),
    CountryConfiguration(countryCode = "MT", defaultLanguage = "mt", supportedLanguages = Seq("mt")),
    CountryConfiguration(countryCode = "NL", defaultLanguage = "nl", supportedLanguages = Seq("nl")),
    CountryConfiguration(countryCode = "NO", defaultLanguage = "en", supportedLanguages = Seq("en")),
    CountryConfiguration(countryCode = "PL", defaultLanguage = "pl", supportedLanguages = Seq("pl")),
    CountryConfiguration(countryCode = "PT", defaultLanguage = "pt", supportedLanguages = Seq("pt")),
    CountryConfiguration(countryCode = "RO", defaultLanguage = "ro", supportedLanguages = Seq("ro")),
    CountryConfiguration(countryCode = "RU", defaultLanguage = "en", supportedLanguages = Seq("en")),
    CountryConfiguration(countryCode = "ES", defaultLanguage = "es", supportedLanguages = Seq("es")),
    CountryConfiguration(countryCode = "SE", defaultLanguage = "sv", supportedLanguages = Seq("sv")),
    CountryConfiguration(countryCode = "SI", defaultLanguage = "sl", supportedLanguages = Seq("sl")),
    CountryConfiguration(countryCode = "SK", defaultLanguage = "sk", supportedLanguages = Seq("sk")),
    CountryConfiguration(countryCode = "GB", defaultLanguage = "en", supportedLanguages = Seq("en")),
    CountryConfiguration(countryCode = "CH", defaultLanguage = "de", supportedLanguages = Seq("de", "fr", "it")),
    CountryConfiguration(countryCode = "US", defaultLanguage = "en", supportedLanguages = Seq("en"))
  )
  val supportedLanguages
    : js.Array[Choice] = Language.mapping.map { case (code, label) => Choice(code, label) }.toJSArray
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
      "Out-of-political-scope",
      "Already-exists",
      "Test",
      "Other"
    )

  val choicesCountry: js.Array[Choice] = choicesCountry(supportedCountries.map(_.countryCode))

  def choicesCountry(countryCodes: Iterable[String]): js.Array[Choice] = {
    countryCodes.map { countryCode =>
      Choice(
        countryCode,
        Country
          .getCountryNameByCountryCode(countryCode)
          .getOrElse(countryCode)
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

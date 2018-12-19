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

import io.circe.parser.parse
import org.make.backoffice.facade.Choice
import org.make.backoffice.model.{BusinessConfig, Country, Language}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}
import scala.scalajs.js.JSConverters._

object Configuration extends CirceClassFormatters {

  val toEnrichMinScore = "1.3"
  val toEnrichMinVotesCount = "200"

  val defaultProposalMaxLength: Int = 256
  val defaultLanguage: String = "fr"

  def businessConfig: Option[BusinessConfig] =
    parse(dom.window.localStorage.getItem("Configuration")).flatMap(_.as[BusinessConfig]) match {
      case Right(bc) =>
        Some(bc)
      case Left(error) =>
        g.console.log(error.getMessage)
        None
    }

  def getReasonsForRefusal: Seq[String] = {
    businessConfig.map { bc =>
      bc.reasonsForRefusal.toSeq
    }.getOrElse(Seq.empty)
  }

  def choicesCountryFilter: js.Array[Choice] = {
    businessConfig
      .map(_.supportedCountries.map { supportedCountry =>
        Choice(
          supportedCountry.countryCode,
          Country
            .getCountryNameByCountryCode(supportedCountry.countryCode)
            .getOrElse(supportedCountry.countryCode)
        )
      }.toSeq)
      .getOrElse(Seq.empty)
      .toJSArray
  }

  def choiceLanguageFilter: Map[String, js.Array[Choice]] = {
    businessConfig
      .map(_.supportedCountries.map { supportedCountry =>
        supportedCountry.countryCode -> supportedCountry.supportedLanguages.map(
          supportedLanguage =>
            Choice(
              supportedLanguage,
              Language.getLanguageNameFromLanguageCode(supportedLanguage).getOrElse(supportedLanguage)
          )
        )
      }.toMap)
      .getOrElse(Map.empty)
  }
}

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

object Language {
  val mapping =
    Map(
      "fr" -> "French",
      "it" -> "Italian",
      "en" -> "English",
      "de" -> "German",
      "bg" -> "Bulgarian",
      "el" -> "Greek",
      "cs" -> "Czech",
      "da" -> "Danish",
      "et" -> "Estonian",
      "es" -> "Spanish",
      "fi" -> "Finnish",
      "hr" -> "Croatian",
      "hu" -> "Hungarian",
      "lt" -> "Lithuanian",
      "lv" -> "Latvian",
      "mt" -> "Maltese",
      "nl" -> "Dutch",
      "pl" -> "Polish",
      "pt" -> "Portuguese",
      "ro" -> "Romanian",
      "sl" -> "Slovene",
      "sk" -> "Slovak",
      "sv" -> "Swedish"
    )

  def getLanguageNameFromLanguageCode(languageCode: String): Option[String] = {
    mapping.get(languageCode)
  }
}

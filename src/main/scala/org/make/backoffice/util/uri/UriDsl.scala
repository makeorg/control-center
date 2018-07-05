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

package org.make.backoffice.util.uri

import scala.scalajs.js.URIUtils

// Inspired by https://github.com/lemonlabsuk/scala-uri/blob/master/src/main/scala/com/netaporter/uri/dsl/UriDsl.scala
class UriDsl(val uri: String) extends AnyVal {

  private def urlify(unEncodedUrl: String): String = URIUtils.encodeURIComponent(unEncodedUrl)

  private def hasParams(uri: String) = uri.contains("?")

  def paramToString(param: (String, Any)): String = param match {
    case (key, Some(value)) => s"${urlify(key)}=${urlify(value.toString)}"
    case (key, value)       => s"${urlify(key)}=${urlify(value.toString)}"
  }

  def paramsToString(params: Seq[(String, Any)], separator: String = "&"): String =
    params.map(paramToString).mkString(separator)

  def ?(param: (String, Any)): String = param match {
    case (_, None) => s"$uri"
    case _         => s"$uri?${paramToString(param)}"
  }

  def &(param: (String, Any)): String = param match {
    case (_, None) => s"$uri"
    case _ =>
      if (hasParams(uri))
        s"$uri&${paramToString(param)}"
      else
        s"$uri?${paramToString(param)}"
  }

  def `#`(fragment: String) = s"$uri#$fragment"

  def /(path: String) = s"$uri/$path"

  def addParams(params: Seq[(String, Any)]): String =
    if (params.isEmpty) {
      uri
    } else if (hasParams(uri)) {
      s"$uri&${paramsToString(params)}"
    } else {
      s"$uri?${paramsToString(params)}"
    }

}

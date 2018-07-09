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

package org.make.backoffice.service

import io.circe.Printer
import org.make.backoffice.client.DefaultMakeApiHttpClientComponent
import org.make.backoffice.client.DefaultMakeApiHttpClientComponent.DefaultMakeApiHttpClient
import org.make.backoffice.client.request.Filter

trait ApiService {
  val resourceName: String
  lazy val client: DefaultMakeApiHttpClient = DefaultMakeApiHttpClientComponent.client
}

object ApiService {
  val printer: Printer = Printer.noSpaces

  def getFieldValueFromFilters(field: String, maybeFilters: Option[Seq[Filter]]): Option[String] = {
    maybeFilters.flatMap(_.find(_.field == field).map(_.value.toString))
  }

}

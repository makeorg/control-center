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

package org.make.backoffice.service.personality

import org.make.backoffice.model.PersonalityRole
import org.make.backoffice.service.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object PersonalityRoleService extends ApiService {
  override val resourceName: String = "admin/personality-roles"

  def personalitieRoles: Future[Seq[PersonalityRole]] = {
    client.get[Seq[PersonalityRole]](resourceName).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of getting personality roles: failed cursor $e")
        throw e
    }
  }
}

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

package org.make.backoffice.service.tag

import org.make.backoffice.model.Tag
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object TagService extends ApiService with CirceClassFormatters {
  override val resourceName: String = "moderation/tags"

  def tags(questionId: Option[String] = None, label: Option[String]): Future[Seq[Tag]] =
    client
      .get[Seq[Tag]](resourceName ? ("questionId", questionId) & ("label", label))
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to Tag: failed cursor $e")
          throw e
      }

  def getTag(tagId: String): Future[Tag] = {
    client.get[Tag](resourceName / tagId).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of converting to Tag: failed cursor $e")
        throw e
    }
  }
}

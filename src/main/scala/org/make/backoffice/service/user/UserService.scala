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

package org.make.backoffice.service.user

import org.make.backoffice.model.{CurrentUser, Role, SimpleUser, User}
import org.make.backoffice.service.ApiService
import org.make.backoffice.service.homepage.UploadResponse
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._
import org.scalajs.dom.ext.Ajax.InputData
import org.scalajs.dom.raw.FormData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

case class NoTokenException(message: String = "No token provided") extends Exception(message)

object UserService extends ApiService with CirceClassFormatters {

  override val resourceName: String = "user"

  def currentUser: Future[CurrentUser] =
    client.get[CurrentUser](resourceName / "current").recoverWith { case e => Future.failed(e) }

  def getUserById(id: String): Future[Option[User]] =
    client.get[User](resourceName / id).map(Option.apply).recover { case _: Exception => None }

  def getUsers: Future[Seq[SimpleUser]] = {
    client.get[Seq[SimpleUser]]("admin" / "users" ? ("role", Role.roleCitizen.shortName)).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of getting users: failed cursor $e")
        throw e
    }
  }

  def adminUploadAvatar(formData: FormData, userType: String): Future[UploadResponse] = {
    val data: InputData = InputData.formdata2ajax(formData)
    client
      .post[UploadResponse]("admin" / "users" / "upload-avatar" / userType, data = data, includeContentType = false)
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to UploadResponse: failed cursor $e")
          throw e

      }
  }

  def loginGoogle(token: String): Future[CurrentUser] = {
    client.authenticateSocial("google", token).flatMap {
      case true  => client.get[CurrentUser](resourceName / "current")
      case false => throw NoTokenException()
    }
  }

  def login(username: String, password: String): Future[CurrentUser] = {
    client.authenticate(username, password).flatMap {
      case true  => client.get[CurrentUser](resourceName / "current")
      case false => throw NoTokenException()
    }
  }
}

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

import org.make.backoffice.model.User
import org.make.backoffice.client.SingleResponse
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._
import org.make.backoffice.service.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class NoTokenException(message: String = "No token provided") extends Exception(message)

object UserService extends ApiService with CirceClassFormatters {

  override val resourceName: String = "user"

  def me: Future[Option[User]] =
    client.get[User](resourceName / "me").map(Option.apply).recoverWith { case e => Future.failed(e) }

  def getUserById(id: String): Future[Option[User]] =
    client.get[User](resourceName / id).map(Option.apply).recover { case _: Exception => None }

  def loginGoogle(token: String): Future[SingleResponse[User]] = {
    client.authenticateSocial("google", token).flatMap {
      case true  => client.get[User](resourceName / "me").map(SingleResponse.apply)
      case false => throw NoTokenException()
    }
  }
}

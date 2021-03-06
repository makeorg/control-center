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

package org.make.backoffice.client

import org.make.backoffice.model.Role
import org.make.backoffice.service.user.UserService
import org.make.backoffice.util.CirceClassFormatters

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise

object AuthClient extends CirceClassFormatters {

  val AUTH_LOGIN = "AUTH_LOGIN"
  val AUTH_LOGOUT = "AUTH_LOGOUT"
  val AUTH_ERROR = "AUTH_ERROR"
  val AUTH_CHECK = "AUTH_CHECK"

  def auth(`type`: String, parameters: Any): Promise[String] = {
    futureAuth(`type`, parameters).toJSPromise
  }

  def futureAuth(`type`: String, parameters: Any): Future[String] = {
    `type` match {
      case AUTH_LOGIN =>
        val loginParameters = parameters.asInstanceOf[LoginParameters]
        UserService.login(loginParameters.username, loginParameters.password).flatMap { user =>
          if (user.roles.contains(Role.roleAdmin) || user.roles.contains(Role.roleModerator)) {
            Future.successful("auth_login")
          } else {
            MakeApiClientHttp.removeToken()
            Future.failed(new Error("Failed to connect: you don't have the right role"))
          }
        }
      case AUTH_LOGOUT =>
        MakeApiClientHttp.removeToken()
        Future.successful("auth_logout")
      case AUTH_ERROR =>
        val errorParameters = parameters.asInstanceOf[ErrorParameters]
        errorParameters.message match {
          case "Unauthorized" =>
            MakeApiClientHttp.removeToken()
            Future.failed(new Error("Rejected AUTH_ERROR for ForbiddenHttpException"))
          case _ => Future.successful("auth_error")
        }
      case AUTH_CHECK =>
        Future.successful("auth_check")
      case _ =>
        MakeApiClientHttp.removeToken()
        Future.failed(new Error("unknown type"))
    }
  }
}

@js.native
trait LoginParameters extends js.Object {
  val username: String
  val password: String
}

@js.native
trait ErrorParameters extends js.Object {
  val message: String
}

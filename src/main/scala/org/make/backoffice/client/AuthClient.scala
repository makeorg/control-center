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

import io.circe.syntax._
import org.make.backoffice.facade.Configuration
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.model.User
import org.make.backoffice.service.technical.ConfigurationsServiceComponent
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise
import scala.util.{Failure, Success}

object AuthClient extends CirceClassFormatters with ConfigurationsServiceComponent {
  override def apiBaseUrl: String = Configuration.apiUrl

  val AUTH_LOGIN = "AUTH_LOGIN"
  val AUTH_LOGOUT = "AUTH_LOGOUT"
  val AUTH_ERROR = "AUTH_ERROR"
  val AUTH_CHECK = "AUTH_CHECK"

  val AUTHENTICATION_KEY = "AUTHENTICATION_KEY"

  def auth(`type`: String, parameters: Any): Promise[String] = {
    futureAuth(`type`, parameters).toJSPromise
  }

  def futureAuth(`type`: String, parameters: Any): Future[String] = {
    `type` match {
      case AUTH_LOGIN =>
        val loginParameters = parameters.asInstanceOf[LoginParameters]
        loginParameters.user match {
          case Some(_) =>
            dom.window.localStorage.setItem(AUTHENTICATION_KEY, "true")
            configurationService.getConfigurations.onComplete {
              case Success(businessConfig) =>
                dom.window.localStorage.setItem("Configuration", businessConfig.asJson.toString)
              case Failure(_) =>
                g.alert("Failed to load configuration. Please refresh the page to avoid unexpected behaviours.")
            }
            Future.successful("auth_login")
          case None =>
            dom.window.localStorage.setItem(AUTHENTICATION_KEY, "false")
            Future.failed(new Error("auth_login"))
        }
      case AUTH_LOGOUT =>
        dom.window.localStorage.setItem(AUTHENTICATION_KEY, "false")
        Future.successful("auth_logout")
      case AUTH_ERROR =>
        parameters match {
          case UnauthorizedHttpException =>
            Future.failed(new Error("Rejected AUTH_ERROR for UnauthorizedHttpException"))
          case ForbiddenHttpException =>
            Future.failed(new Error("Rejected AUTH_ERROR for ForbiddenHttpException"))
          case _ => Future.successful("auth_error")
        }
      case AUTH_CHECK =>
        dom.window.localStorage.getItem(AUTHENTICATION_KEY) match {
          case "true" => Future.successful("auth_check")
          case _      => Future.failed(new Error("auth_check"))
        }

      case _ =>
        dom.window.localStorage.getItem(AUTHENTICATION_KEY) match {
          case "true" => Future.successful("auth_check")
          case _      => Future.failed(new Error("auth_check"))
        }
    }
  }
}

@js.native
trait LoginParameters extends js.Object {
  val user: Option[User]
}

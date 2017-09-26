package org.make.client

import org.make.backoffice.models.User
import org.make.core.CirceClassFormatters
import org.scalajs.dom
import io.circe.syntax._
import org.make.backoffice.facades.Configuration
import org.make.services.technical.BusinessConfigServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise
import scala.util.{Failure, Success}

import scala.scalajs.js.Dynamic.{global => g}

object AuthClient extends CirceClassFormatters with BusinessConfigServiceComponent {
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
            businessConfigService.getBusinessConfig.onComplete {
              case Success(businessConfig) =>
                dom.window.localStorage.setItem("businessConfig", businessConfig.asJson.toString)
              case Failure(e) => g.console.log(e.getMessage)
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

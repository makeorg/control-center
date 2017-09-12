package org.make.client

import org.make.backoffice.models.User
import org.make.core.CirceClassFormatters

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise
import scala.concurrent.ExecutionContext.Implicits.global

object AuthClient extends CirceClassFormatters {
  val AUTH_LOGIN = "AUTH_LOGIN"
  val AUTH_LOGOUT = "AUTH_LOGOUT"
  val AUTH_ERROR = "AUTH_ERROR"
  val AUTH_CHECK = "AUTH_CHECK"
// parameters = {source: toto, url: tata}
  def auth(`type`: String, parameters: Any): Promise[String] = {
    `type` match {
      case AUTH_LOGIN =>
        val loginParameters = parameters.asInstanceOf[LoginParameters]
        js.Dynamic.global.console.log(s"Login parameters $loginParameters")

        loginParameters match {
          case _ => Future.successful("logged in").toJSPromise
        }
      case AUTH_LOGOUT => Future.successful("logged out").toJSPromise
      case AUTH_ERROR =>
        parameters match {
          case UnauthorizedHttpException => Promise.reject("Rejected AUTH_ERROR for UnauthorizedHttpException")
          case ForbiddenHttpException => Promise.reject("Rejected AUTH_ERROR for ForbiddenHttpException")
          case _ => Future.successful("Auth error").toJSPromise
        }
      case AUTH_CHECK =>
        val authParameters = parameters.asInstanceOf[AuthentificationParameters]
        Future.successful(s"authParameters AUTH_CHECK in authClient $authParameters").toJSPromise
      case _ => Promise.reject("Unknown type in authClient")
    }
  }
}

@js.native
trait LoginParameters extends js.Object {
  val user: Option[User]
}

@js.native
trait AuthentificationParameters extends js.Object {
  val source: String
  val url: String
}
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

import io.circe.java8.time.TimeInstances
import io.circe.parser.parse
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Decoder, Printer}
import io.github.shogowada.statictags.MediaTypes
import org.make.backoffice.facade.Configuration
import org.make.backoffice.model.Token
import org.make.backoffice.client.MakeApiClientHttp.RequestData
import org.make.backoffice.util.uri._
import org.scalajs.dom
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax.InputData
import org.scalajs.dom.ext.{Ajax, AjaxException}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

trait MakeApiHttpClientComponent {
  def apiBaseUrl: String
  def client: HttpClient
  def maxTimeout: Int = 9000
  def defaultHeaders: Map[String, String] = Map.empty
  def withCredentials: Boolean = true
}

//TODO: add error handler. throw custom exception in the promise.future
trait DefaultMakeApiHttpClientComponent extends MakeApiHttpClientComponent with TimeInstances {
  override def client: DefaultMakeApiHttpClient = new DefaultMakeApiHttpClient()

  override def defaultHeaders: Map[String, String] = {
    Map(
      //TODO: X-Forwarded-For Header is set for dev only. Remove in prod.
      "X-Forwarded-For" -> "0.0.0.0",
      "Accept" -> MediaTypes.`application/json`,
      "Content-Type" -> "application/json;charset=UTF-8"
    ) ++
      MakeApiClientHttp.getToken.map { token =>
        Map("Authorization" -> s"${token.token_type} ${token.access_token}")
      }.getOrElse(Map("Authorization" -> dom.window.localStorage.getItem(AuthClient.AUTHENTICATION_KEY)))
  }

  final class DefaultMakeApiHttpClient extends HttpClient {

    val retryAfterTimeout: Int = 4

    override def baseUrl: String = apiBaseUrl

    private def retryOnFailure[T](fn: => Future[T], retries: Int)(implicit decoder: Decoder[T]): Future[T] = {
      fn.recoverWith {
        case ajaxException: AjaxException if ajaxException.isTimeout && retries > 0 => retryOnFailure(fn, retries - 1)
        case AjaxException(response: XMLHttpRequest) =>
          response.status match {
            case 400 =>
              parse(response.responseText).flatMap(_.as[js.Array[ValidationError]]) match {
                case Left(error)     => Future.failed(error)
                case Right(messages) => Future.failed(BadRequestHttpException(messages))
              }
            case 401 => Future.failed(UnauthorizedHttpException)
            case 403 => Future.failed(ForbiddenHttpException)
            case 404 => Future.failed(NotFoundHttpException)
            case 500 => Future.failed(InternalServerHttpException)
            case 502 => Future.failed(BadGatewayHttpException)
            case _   => Future.failed(NotImplementedHttpException)
          }
        case other => Future.failed(other)
      }
    }

    private def urlFrom(apiEndpoint: String, urlParams: Seq[(String, Any)] = Seq.empty): String =
      (baseUrl / apiEndpoint).addParams(urlParams)

    override def get[ENTITY](
      apiEndpoint: String,
      urlParams: Seq[(String, Any)] = Seq.empty,
      headers: Map[String, String] = Map.empty
    )(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
      val requestData = RequestData(
        method = "GET",
        url = urlFrom(apiEndpoint, urlParams),
        timeout = maxTimeout,
        withCredentials = withCredentials,
        headers = defaultHeaders ++ headers
      )

      retryOnFailure[ENTITY](MakeApiClientHttp[ENTITY](requestData), retryAfterTimeout)
    }

    override def post[ENTITY](
      apiEndpoint: String,
      urlParams: Seq[(String, Any)] = Seq.empty,
      data: InputData = "",
      headers: Map[String, String] = Map.empty
    )(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
      val requestData = RequestData(
        method = "POST",
        url = urlFrom(apiEndpoint, urlParams),
        data = data,
        timeout = maxTimeout,
        withCredentials = withCredentials,
        headers = defaultHeaders ++ headers
      )

      retryOnFailure[ENTITY](MakeApiClientHttp[ENTITY](requestData), retryAfterTimeout)
    }

    override def put[ENTITY](
      apiEndpoint: String,
      urlParams: Seq[(String, Any)] = Seq.empty,
      data: InputData = "",
      headers: Map[String, String] = Map.empty
    )(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
      val requestData = RequestData(
        method = "PUT",
        url = urlFrom(apiEndpoint, urlParams),
        data = data,
        timeout = maxTimeout,
        withCredentials = withCredentials,
        headers = defaultHeaders ++ headers
      )

      retryOnFailure[ENTITY](MakeApiClientHttp[ENTITY](requestData), retryAfterTimeout)
    }

    override def patch[ENTITY](apiEndpoint: String,
                               urlParams: Seq[(String, Any)],
                               data: InputData,
                               headers: Map[String, String])(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
      val requestData = RequestData(
        method = "PATCH",
        url = urlFrom(apiEndpoint, urlParams),
        data = data,
        timeout = maxTimeout,
        headers = defaultHeaders ++ headers,
        withCredentials = withCredentials
      )

      retryOnFailure[ENTITY](MakeApiClientHttp[ENTITY](requestData), retryAfterTimeout)
    }

    override def delete[ENTITY](apiEndpoint: String,
                                urlParams: Seq[(String, Any)],
                                data: InputData,
                                headers: Map[String, String])(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
      val requestData = RequestData(
        method = "DELETE",
        url = urlFrom(apiEndpoint, urlParams),
        data = data,
        timeout = maxTimeout,
        headers = defaultHeaders ++ headers,
        withCredentials = withCredentials
      )

      retryOnFailure[ENTITY](MakeApiClientHttp[ENTITY](requestData), retryAfterTimeout)
    }

    def authenticateSocial(provider: String, token: String)(implicit decoder: Decoder[Token]): Future[Boolean] = {
      if (MakeApiClientHttp.isAuthenticated) {
        Future.successful(true)
      } else {
        askForAccessTokenSocial(provider, token)
      }
    }

    def authenticate(username: String, password: String): Future[Boolean] = {
      if (MakeApiClientHttp.isAuthenticated) {
        Future.successful(true)
      } else {
        askForAccessToken(username, password)
      }
    }

    private def askForAccessToken(username: String, password: String): Future[Boolean] = {
      post[Token](
        "oauth" / "make_access_token",
        data = "".paramsToString(js.Array("username" -> username, "password" -> password, "grant_type" -> "password")),
        headers = Map("Content-Type" -> MediaTypes.`application/x-www-form-urlencoded`)
      ).map { newToken =>
        MakeApiClientHttp.setToken(Option(newToken))
        MakeApiClientHttp.isAuthenticated
      }
    }

    private def askForAccessTokenSocial(provider: String,
                                        token: String)(implicit decoder: Decoder[Token]): Future[Boolean] = {
      post[Token](
        "user" / "login" / "social",
        data = Map("provider" -> provider, "token" -> token, "country" -> "FR", "language" -> "fr").asJson
          .pretty(MakeApiClientHttp.printer)
      ).map { newToken =>
        MakeApiClientHttp.setToken(Option(newToken))
        MakeApiClientHttp.isAuthenticated
      }
    }

    def logout(): Future[Unit] = {
      Ajax
        .post(
          url = urlFrom("logout"),
          timeout = maxTimeout,
          headers = defaultHeaders,
          withCredentials = withCredentials
        )
        .map(_ => MakeApiClientHttp.removeToken())
    }

  }
}

object MakeApiClientHttp {
  private var token: Option[Token] = None
  val printer: Printer = Printer.noSpaces

  def getToken: Option[Token] = token
  def setToken(newToken: Option[Token]): Unit = { token = newToken }
  def removeToken(): Unit = { token = None }
  def isAuthenticated: Boolean = { token.isDefined }

  case class RequestData(method: String,
                         url: String,
                         data: InputData = null,
                         timeout: Int = 0,
                         headers: Map[String, String] = Map.empty,
                         withCredentials: Boolean = false,
                         responseType: String = "")

  def apply[ENTITY](requestData: RequestData)(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
    Ajax(
      method = requestData.method,
      url = requestData.url,
      data = requestData.data,
      timeout = requestData.timeout,
      headers = requestData.headers,
      withCredentials = requestData.withCredentials,
      responseType = requestData.responseType
    ).map(
      response =>
        if (response.responseText.nonEmpty) {
          parse(response.responseText).flatMap(_.as[ENTITY]) match {
            case Left(error)           => throw error
            case Right(parsedResponse) => parsedResponse
          }
        } else {
          response.response.asInstanceOf[ENTITY]
      }
    )
  }

}

object DefaultMakeApiHttpClientComponent extends DefaultMakeApiHttpClientComponent {
  override def apiBaseUrl: String = Configuration.apiUrl
}

package org.make.client

import io.circe.java8.time.TimeInstances
import io.circe.parser.parse
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Decoder, Printer}
import io.github.shogowada.statictags.MediaTypes
import org.make.backoffice.facades.Configuration
import org.make.backoffice.models.Token
import org.make.client.MakeApiClientHttp.RequestData
import org.make.core.URI._
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
      client.customHeaders ++
      MakeApiClientHttp.getToken.map { token =>
        Map("Authorization" -> s"${token.token_type} ${token.access_token}")
      }.getOrElse(Map.empty)
  }

  final class DefaultMakeApiHttpClient extends HttpClient {

    var customHeaders: Map[String, String] = Map.empty

    val themeIdHeader: String = "x-make-theme-id"
    val operationHeader: String = "x-make-operation"
    val sourceHeader: String = "x-make-source"
    val locationHeader: String = "x-make-location"
    val questionHeader: String = "x-make-question"
    val languageHeader: String = "x-make-language"
    val countryHeader: String = "x-make-country"
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

    private def askForAccessTokenSocial(provider: String,
                                        token: String)(implicit decoder: Decoder[Token]): Future[Boolean] = {
      post[Token](
        "user" / "login" / "social",
        data = Map("provider" -> provider, "token" -> token).asJson.pretty(MakeApiClientHttp.printer)
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

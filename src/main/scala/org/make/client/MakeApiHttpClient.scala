package org.make.client

import io.circe.generic.auto._
import io.circe.java8.time.TimeInstances
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Printer}
import io.github.shogowada.statictags.MediaTypes
import org.make.backoffice.facades.Configuration
import org.make.backoffice.models.Token
import org.make.core.URI._
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax.InputData
import org.scalajs.dom.ext.{Ajax, AjaxException}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

trait MakeApiHttpClientComponent {
  def apiBaseUrl: String
  def client: HttpClient
  def maxTimeout: Int = 5000
  def defaultHeaders: Map[String, String] = Map.empty
  def withCredentials: Boolean = false
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
    ) ++ MakeApiClientHttp.getToken.map { token =>
      Map("Authorization" -> s"${token.token_type} ${token.access_token}")
    }.getOrElse(Map.empty)
  }

  final class DefaultMakeApiHttpClient extends HttpClient {

    override def baseUrl: String = apiBaseUrl

    private def XHRResponseTo[ENTITY](responseTry: Try[XMLHttpRequest], promise: Promise[Option[ENTITY]])(
      implicit decoder: Decoder[ENTITY]
    ): Promise[Option[ENTITY]] = {
      responseTry match {
        case Success(response) if response.status == 204 => promise.success(None)
        case Success(response) =>
          parse(response.responseText).flatMap(_.as[ENTITY]) match {
            case Left(error)           => promise.failure(error)
            case Right(parsedResponse) => promise.success(Some(parsedResponse))
          }
        case Failure(AjaxException(response: XMLHttpRequest)) =>
          response.status match {
            case 400 =>
              parse(response.responseText).flatMap(_.as[Seq[ValidationError]]) match {
                case Left(error)     => promise.failure(error)
                case Right(messages) => promise.failure(BadRequestHttpException(messages))
              }
            case 401 => promise.failure(UnauthorizedHttpException)
            case 403 => promise.failure(ForbiddenHttpException)
            case 404 => promise.failure(NotFoundHttpException)
            case 500 => promise.failure(InternalServerHttpException)
            case 502 => promise.failure(BadGatewayHttpException)
            case _   => promise.failure(NotImplementedHttpException)
          }

      }
    }

    private def urlFrom(apiEndpoint: String, urlParams: Seq[(String, Any)] = Seq.empty): String =
      (baseUrl / apiEndpoint).addParams(urlParams)

    override def get[ENTITY](
      apiEndpoint: String,
      urlParams: Seq[(String, Any)] = Seq.empty,
      headers: Map[String, String] = Map.empty
    )(implicit decoder: Decoder[ENTITY]): Future[Option[ENTITY]] = {
      val promiseReturn = Promise[Option[ENTITY]]()
      Ajax
        .get(
          url = urlFrom(apiEndpoint, urlParams),
          timeout = maxTimeout,
          headers = defaultHeaders ++ headers,
          withCredentials = withCredentials
        )
        .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
      promiseReturn.future
    }

    override def post[ENTITY](
      apiEndpoint: String,
      urlParams: Seq[(String, Any)] = Seq.empty,
      data: InputData = "",
      headers: Map[String, String] = Map.empty
    )(implicit decoder: Decoder[ENTITY]): Future[Option[ENTITY]] = {
      val promiseReturn = Promise[Option[ENTITY]]()
      Ajax
        .post(
          url = urlFrom(apiEndpoint, urlParams),
          data = data,
          timeout = maxTimeout,
          headers = defaultHeaders ++ headers,
          withCredentials = withCredentials
        )
        .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
      promiseReturn.future
    }

    override def put[ENTITY](
      apiEndpoint: String,
      urlParams: Seq[(String, Any)],
      data: InputData,
      headers: Map[String, String]
    )(implicit decoder: Decoder[ENTITY]): Future[Option[ENTITY]] = {
      val promiseReturn = Promise[Option[ENTITY]]()
      Ajax
        .put(
          url = urlFrom(apiEndpoint, urlParams),
          data = data,
          timeout = maxTimeout,
          headers = defaultHeaders ++ headers,
          withCredentials = withCredentials
        )
        .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
      promiseReturn.future
    }

    override def patch[ENTITY](
      apiEndpoint: String,
      urlParams: Seq[(String, Any)],
      data: InputData,
      headers: Map[String, String]
    )(implicit decoder: Decoder[ENTITY]): Future[Option[ENTITY]] = {
      val promiseReturn = Promise[Option[ENTITY]]()
      Ajax
        .apply(
          method = "PATCH",
          url = urlFrom(apiEndpoint, urlParams),
          data = data,
          timeout = maxTimeout,
          headers = defaultHeaders ++ headers,
          withCredentials = withCredentials,
          responseType = ""
        )
        .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
      promiseReturn.future
    }

    override def delete[ENTITY](
      apiEndpoint: String,
      urlParams: Seq[(String, Any)],
      data: InputData,
      headers: Map[String, String]
    )(implicit decoder: Decoder[ENTITY]): Future[Option[ENTITY]] = {
      val promiseReturn = Promise[Option[ENTITY]]()
      Ajax
        .delete(
          url = urlFrom(apiEndpoint, urlParams),
          data = data,
          timeout = maxTimeout,
          headers = defaultHeaders ++ headers,
          withCredentials = withCredentials
        )
        .onComplete(responseTry => XHRResponseTo(responseTry, promiseReturn))
      promiseReturn.future
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
        MakeApiClientHttp.setToken(newToken.get)
        MakeApiClientHttp.isAuthenticated
      }
    }

    def logout(): Future[Unit] = {
      post[Unit]("logout").map {
        case Some(_) => MakeApiClientHttp.removeToken()
        case _       =>
      }
    }

  }
}

object MakeApiClientHttp {
  private var token: Option[Token] = None
  val printer: Printer = Printer.noSpaces

  def getToken: Option[Token] = token
  def setToken(newToken: Token): Unit = { token = Some(newToken) }
  def removeToken(): Unit = { token = None }
  def isAuthenticated: Boolean = { token.isDefined }
}

object DefaultMakeApiHttpClientComponent extends DefaultMakeApiHttpClientComponent {
  override def apiBaseUrl: String = Configuration.apiUrl
}

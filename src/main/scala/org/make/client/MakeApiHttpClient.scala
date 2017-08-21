package org.make.client

import io.circe.{Decoder, Printer}
import io.circe.java8.time.TimeInstances
import io.circe.parser._
import io.circe.syntax._
import io.github.shogowada.statictags.MediaTypes
import org.make.core.URI._
import org.make.backoffice.models.Token
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.ext.Ajax.InputData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

trait MakeApiHttpClientComponent {
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

    //TODO: load these from configuration file
    override def baseUrl: String = "http://localhost:9000"

    private def XHRResponseTo[ENTITY](responseTry: Try[XMLHttpRequest],
                                      promise: Promise[ENTITY])(implicit decoder: Decoder[ENTITY]): Promise[ENTITY] = {
      responseTry match {
        case Success(response) =>
          parse(response.responseText).flatMap(_.as[ENTITY]) match {
            case Left(error)           => promise.failure(error)
            case Right(parsedResponse) => promise.success(parsedResponse)
          }
        case Failure(error) => promise.failure(error)
      }
    }

    private def urlFrom(apiEndpoint: String, urlParams: Seq[(String, Any)] = Seq.empty): String =
      (baseUrl / apiEndpoint).addParams(urlParams)

    override def get[ENTITY](
      apiEndpoint: String,
      urlParams: Seq[(String, Any)] = Seq.empty,
      headers: Map[String, String] = Map.empty
    )(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
      val promiseReturn = Promise[ENTITY]()
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
    )(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
      val promiseReturn = Promise[ENTITY]()
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

    override def put[ENTITY](apiEndpoint: String,
                             urlParams: Seq[(String, Any)],
                             data: InputData,
                             headers: Map[String, String])(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
      val promiseReturn = Promise[ENTITY]()
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

    override def patch[ENTITY](apiEndpoint: String,
                               urlParams: Seq[(String, Any)],
                               data: InputData,
                               headers: Map[String, String])(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
      val promiseReturn = Promise[ENTITY]()
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

    override def delete[ENTITY](apiEndpoint: String,
                                urlParams: Seq[(String, Any)],
                                data: InputData,
                                headers: Map[String, String])(implicit decoder: Decoder[ENTITY]): Future[ENTITY] = {
      val promiseReturn = Promise[ENTITY]()
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
        MakeApiClientHttp.setToken(newToken)
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
        .map { _ =>
          MakeApiClientHttp.removeToken()
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

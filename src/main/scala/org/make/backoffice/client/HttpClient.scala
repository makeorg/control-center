package org.make.backoffice.client

import io.circe.Decoder
import org.scalajs.dom.ext.Ajax.InputData

import scala.concurrent.Future

trait HttpClient {
  def baseUrl: String

  def get[ENTITY](apiEndpoint: String, urlParams: Seq[(String, Any)], headers: Map[String, String])(
    implicit decoder: Decoder[ENTITY]
  ): Future[ENTITY]

  def post[ENTITY](apiEndpoint: String, urlParams: Seq[(String, Any)], data: InputData, headers: Map[String, String])(
    implicit decoder: Decoder[ENTITY]
  ): Future[ENTITY]

  def put[ENTITY](apiEndpoint: String,
                  urlParams: Seq[(String, Any)],
                  data: InputData,
                  headers: Map[String, String] = Map.empty)(implicit decoder: Decoder[ENTITY]): Future[ENTITY]

  def patch[ENTITY](apiEndpoint: String,
                    urlParams: Seq[(String, Any)],
                    data: InputData,
                    headers: Map[String, String] = Map.empty)(implicit decoder: Decoder[ENTITY]): Future[ENTITY]

  def delete[ENTITY](apiEndpoint: String,
                     urlParams: Seq[(String, Any)],
                     data: InputData,
                     headers: Map[String, String] = Map.empty)(implicit decoder: Decoder[ENTITY]): Future[ENTITY]
}

trait HttpException extends Exception

case class ValidationError(field: String, message: Option[String])

trait ValidationFailedHttpException extends HttpException {
  val errors: Seq[ValidationError]
  override def getMessage: String = { errors.toString }
}

case class BadRequestHttpException(override val errors: Seq[ValidationError]) extends ValidationFailedHttpException
case object UnauthorizedHttpException extends HttpException
case object ForbiddenHttpException extends HttpException
case object NotFoundHttpException extends HttpException
case object InternalServerHttpException extends HttpException
case object BadGatewayHttpException extends HttpException
case object NotImplementedHttpException extends HttpException

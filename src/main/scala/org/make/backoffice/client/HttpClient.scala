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
  override def getMessage: String = { errors.map(_.message.getOrElse("")).toString() }
}

case class BadRequestHttpException(override val errors: Seq[ValidationError]) extends ValidationFailedHttpException
case object UnauthorizedHttpException extends HttpException
case object ForbiddenHttpException extends HttpException
case object NotFoundHttpException extends HttpException
case object InternalServerHttpException extends HttpException
case object BadGatewayHttpException extends HttpException
case object NotImplementedHttpException extends HttpException

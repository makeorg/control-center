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

package org.make.backoffice.service.operation

import io.circe.Encoder
import io.circe.syntax._
import org.make.backoffice.client.BadRequestHttpException
import org.make.backoffice.model.{CurrentOperation, CurrentOperationIdResult}
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object CurrentOperationService extends ApiService with CirceClassFormatters {
  override val resourceName = "admin/views/home/current-operations"

  def getCurrentOperation(currentOperationId: String): Future[CurrentOperation] =
    client.get[CurrentOperation](resourceName / currentOperationId).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of getting a current operation: failed cursor $e")
        throw e
    }

  def postCurrentOperation(request: CreateCurrentOperationRequest): Future[CurrentOperationIdResult] =
    client
      .post[CurrentOperationIdResult](resourceName, data = request.asJson.pretty(ApiService.printer))
      .recoverWith {
        case e: BadRequestHttpException =>
          Future.failed(js.JavaScriptException(js.Error(e.errors.headOption.flatMap(_.message).getOrElse(""))))
        case e =>
          js.Dynamic.global.console.log(s"instead of creating a current operation: failed cursor $e")
          Future.failed(e)
      }

  def putCurrentOperation(currentOperationId: String,
                          request: UpdateCurrentOperationRequest): Future[CurrentOperationIdResult] =
    client
      .put[CurrentOperationIdResult](
        resourceName / currentOperationId,
        data = request.asJson.pretty(ApiService.printer)
      )
      .recoverWith {
        case e: BadRequestHttpException =>
          Future.failed(js.JavaScriptException(js.Error(e.errors.headOption.flatMap(_.message).getOrElse(""))))
        case e =>
          js.Dynamic.global.console.log(s"instead of updating a current operation: failed cursor $e")
          Future.failed(e)
      }
}

case class CreateCurrentOperationRequest(questionId: String,
                                         description: String,
                                         label: String,
                                         picture: String,
                                         altPicture: String,
                                         linkLabel: String,
                                         internalLink: Option[String],
                                         externalLink: Option[String])

object CreateCurrentOperationRequest {
  implicit lazy val encoder: Encoder[CreateCurrentOperationRequest] = Encoder.forProduct8(
    "questionId",
    "description",
    "label",
    "picture",
    "altPicture",
    "linkLabel",
    "internalLink",
    "externalLink"
  )(
    createCurrentOperationRequest =>
      (
        createCurrentOperationRequest.questionId,
        createCurrentOperationRequest.description,
        createCurrentOperationRequest.label,
        createCurrentOperationRequest.picture,
        createCurrentOperationRequest.altPicture,
        createCurrentOperationRequest.linkLabel,
        createCurrentOperationRequest.internalLink,
        createCurrentOperationRequest.externalLink
    )
  )
}

case class UpdateCurrentOperationRequest(questionId: String,
                                         description: String,
                                         label: String,
                                         picture: String,
                                         altPicture: String,
                                         linkLabel: String,
                                         internalLink: Option[String],
                                         externalLink: Option[String])

object UpdateCurrentOperationRequest {
  implicit lazy val encoder: Encoder[UpdateCurrentOperationRequest] = Encoder.forProduct8(
    "questionId",
    "description",
    "label",
    "picture",
    "altPicture",
    "linkLabel",
    "internalLink",
    "externalLink"
  )(
    updateCurrentOperationRequest =>
      (
        updateCurrentOperationRequest.questionId,
        updateCurrentOperationRequest.description,
        updateCurrentOperationRequest.label,
        updateCurrentOperationRequest.picture,
        updateCurrentOperationRequest.altPicture,
        updateCurrentOperationRequest.linkLabel,
        updateCurrentOperationRequest.internalLink,
        updateCurrentOperationRequest.externalLink
    )
  )
}

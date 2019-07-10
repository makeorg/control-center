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

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.backoffice.client.BadRequestHttpException
import org.make.backoffice.model.{FeaturedOperation, FeaturedOperationId}
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object OperationService extends ApiService with CirceClassFormatters {
  override val resourceName = "admin/views/home/featured-operations"

  def featuredOperations: Future[Seq[FeaturedOperation]] =
    client.get[Seq[FeaturedOperation]](resourceName).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of getting featured operations: failed cursor $e")
        throw e
    }

  def postFeaturedOperation(request: CreateFeaturedOperationRequest): Future[FeaturedOperationId] =
    client.post[FeaturedOperationId](resourceName, data = request.asJson.pretty(ApiService.printer)).recoverWith {
      case e: BadRequestHttpException =>
        Future.failed(js.JavaScriptException(js.Error(e.errors.headOption.flatMap(_.message).getOrElse(""))))
      case e =>
        Future.failed(e)
    }

  def putFeaturedOperation(featuredOperationId: String,
                           request: UpdateFeaturedOperationRequest): Future[FeaturedOperationId] =
    client
      .put[FeaturedOperationId](resourceName / featuredOperationId, data = request.asJson.pretty(ApiService.printer))
      .recoverWith {
        case e: BadRequestHttpException =>
          Future.failed(js.JavaScriptException(js.Error(e.errors.headOption.flatMap(_.message).getOrElse(""))))
        case e =>
          Future.failed(e)
      }

  def deleteFeaturedOperation(featuredOperationId: String): Future[Unit] =
    client.delete[Unit](resourceName / featuredOperationId).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of deleting a featured operation: failed cursor $e")
        throw e
    }
}

case class CreateFeaturedOperationRequest(questionId: Option[String],
                                          title: String,
                                          description: Option[String],
                                          landscapePicture: String,
                                          portraitPicture: String,
                                          altPicture: String,
                                          label: String,
                                          buttonLabel: String,
                                          internalLink: Option[String],
                                          externalLink: Option[String],
                                          slot: Int)

case class UpdateFeaturedOperationRequest(questionId: Option[String],
                                          title: String,
                                          description: Option[String],
                                          landscapePicture: String,
                                          portraitPicture: String,
                                          altPicture: String,
                                          label: String,
                                          buttonLabel: String,
                                          internalLink: Option[String],
                                          externalLink: Option[String],
                                          slot: Int)

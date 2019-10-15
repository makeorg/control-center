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

package org.make.backoffice.service.feature

import io.circe.Encoder
import io.circe.syntax._
import org.make.backoffice.model.ActiveFeature
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object ActiveFeatureService extends ApiService with CirceClassFormatters {
  override val resourceName = "admin/active-features"

  def listActiveFeatures: Future[Seq[ActiveFeature]] = {
    client.get[Seq[ActiveFeature]](resourceName).recoverWith {
      case e =>
        js.Dynamic.global.console.log(s"instead of converting to ActiveFeature: failed cursor $e")
        Future.failed(e)
    }
  }

  def createActiveFeature(featureId: String, questionId: String): Future[ActiveFeature] = {
    val request = CreateActiveFeatureRequest(featureId, questionId)
    client.post[ActiveFeature](resourceName, data = request.asJson.pretty(ApiService.printer)).recoverWith {
      case e =>
        js.Dynamic.global.console.log(s"instead of converting to ActiveFeature: failed cursor $e")
        Future.failed(e)
    }
  }

  def deleteActiveFeature(activeFeatureId: String): Future[Unit] = {
    client.delete[Unit](resourceName / activeFeatureId).recoverWith {
      case e =>
        js.Dynamic.global.console.log(s"instead of converting to ActiveFeature: failed cursor $e")
        Future.failed(e)
    }
  }

}

case class CreateActiveFeatureRequest(featureId: String, questionId: String)

object CreateActiveFeatureRequest {
  implicit lazy val encoder: Encoder[CreateActiveFeatureRequest] =
    Encoder.forProduct2("featureId", "maybeQuestionId")(
      createActiveFeature => (createActiveFeature.featureId, createActiveFeature.questionId)
    )
}

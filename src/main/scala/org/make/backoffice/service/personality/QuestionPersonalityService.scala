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

package org.make.backoffice.service.personality

import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.make.backoffice.model.PersonalityId
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object QuestionPersonalityService extends ApiService with CirceClassFormatters {
  override val resourceName: String = "admin/question-personalities"

  def createPersonality(request: CreatePersonalityRequest): Future[PersonalityIdResponse] = {
    client.post[PersonalityIdResponse](resourceName, data = request.asJson.pretty(ApiService.printer)).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of creating personality: failed cursor $e")
        throw e
    }
  }

  def editPersonality(request: UpdatePersonalityRequest, personalityId: String): Future[PersonalityIdResponse] = {
    client
      .put[PersonalityIdResponse](resourceName / personalityId, data = request.asJson.pretty(ApiService.printer))
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of creating personality: failed cursor $e")
          throw e
      }
  }

  def deletePersonality(personalityId: String): Future[Unit] = {
    client.delete[Unit](resourceName / personalityId).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of creating personality: failed cursor $e")
        throw e
    }
  }
}

final case class CreatePersonalityRequest(userId: Option[String], questionId: String, personalityRoleId: String)

object CreatePersonalityRequest {
  implicit lazy val encoder: Encoder[CreatePersonalityRequest] =
    Encoder.forProduct3("userId", "questionId", "personalityRoleId")(
      createPersonalityRequest =>
        (
          createPersonalityRequest.userId,
          createPersonalityRequest.questionId,
          createPersonalityRequest.personalityRoleId
      )
    )
}

final case class UpdatePersonalityRequest(userId: String, personalityRoleId: String)

object UpdatePersonalityRequest {
  implicit lazy val encoder: Encoder[UpdatePersonalityRequest] =
    Encoder.forProduct2("userId", "personalityRoleId")(
      updatePersonalityRequest => (updatePersonalityRequest.userId, updatePersonalityRequest.personalityRoleId)
    )
}

final case class PersonalityIdResponse(personalityId: PersonalityId)

object PersonalityIdResponse {
  implicit lazy val decoder: Decoder[PersonalityIdResponse] =
    Decoder.forProduct1("personalityId")(PersonalityIdResponse.apply)
}

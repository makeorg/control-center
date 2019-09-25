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

package org.make.backoffice.service.idea

import io.circe.Encoder

final case class CreateIdeaRequest(name: String, questionId: Option[String])

object CreateIdeaRequest {
  implicit lazy val encoder: Encoder[CreateIdeaRequest] = Encoder.forProduct2("name", "questionId")(
    createIdeaRequest => (createIdeaRequest.name, createIdeaRequest.questionId)
  )
}

final case class UpdateIdeaRequest(name: String)

object UpdateIdeaRequest {
  implicit lazy val encoder: Encoder[UpdateIdeaRequest] =
    Encoder.forProduct1("name")(createIdeaRequest => createIdeaRequest.name)
}

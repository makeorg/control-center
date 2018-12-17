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

package org.make.backoffice.model
import io.circe.{Decoder, Encoder, Json}

import scala.scalajs.js

@js.native
trait QuestionId extends js.Object with StringValue {
  override val value: String
}

object QuestionId {
  def apply(value: String): QuestionId = js.Dynamic.literal(value = value).asInstanceOf[QuestionId]

  implicit lazy val questionIdEncoder: Encoder[QuestionId] = (a: QuestionId) => Json.fromString(a.value)
  implicit lazy val questionIdDecoder: Decoder[QuestionId] = Decoder.decodeString.map(QuestionId(_))
}

@js.native
trait Question extends js.Object {
  val id: String
  val question: String
  val slug: String
}

object Question {
  def apply(questionId: QuestionId, question: String, slug: String): Question =
    js.Dynamic
      .literal(id = questionId.value, question = question, slug = slug)
      .asInstanceOf[Question]
}

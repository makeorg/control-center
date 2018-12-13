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

package org.make.backoffice.service.question
import java.time.LocalDate

import org.make.backoffice.model.{OperationId, Question}
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object OperationOfQuestionService extends ApiService with CirceClassFormatters {

  override val resourceName: String = "moderation/operations-of-questions"

  def operationsOfQuestions(questionId: Option[String],
                            operationId: Option[OperationId],
                            openAt: Option[LocalDate]): Future[Seq[Question]] = {
    client
      .get[Seq[Question]](resourceName ? ("questionId", questionId) & ("operationId", operationId) & ("openAt", openAt))
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to Question: failed cursor $e")
          throw e
      }
  }

}

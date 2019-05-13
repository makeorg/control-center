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

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.backoffice.client.ListTotalResponse
import org.make.backoffice.client.request.{Filter, Pagination, Sort}
import org.make.backoffice.model.Question.DataConfiguration
import org.make.backoffice.model.{ProposalIdResult, Question}
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object QuestionService extends ApiService with CirceClassFormatters {

  override val resourceName: String = "moderation/operations-of-questions"

  def questions(pagination: Option[Pagination] = None,
                sort: Option[Sort] = None,
                filters: Option[Seq[Filter]] = None): Future[ListTotalResponse[Question]] = {
    client
      .get[js.Array[Question]](
        resourceName ?
          ("limit", pagination.map(_.perPage)) &
          ("skip", pagination.map(page => page.page * page.perPage - page.perPage)) &
          ("questionId", ApiService.getFieldValueFromFilters("questionId", filters)) &
          ("operationId", ApiService.getFieldValueFromFilters("operationId", filters)) &
          ("openAt", ApiService.getFieldValueFromFilters("openAt", filters))
      )
      .map(questions => ListTotalResponse.apply(total = questions.size, data = questions))
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to Question: failed cursor $e")
          throw e
      }
  }

  def addInitialProposal(questionId: String, request: InitialProposalRequest): Future[Unit] = {
    client
      .post[ProposalIdResult](
        apiEndpoint = s"moderation/questions/$questionId/initial-proposals",
        data = request.asJson.pretty(ApiService.printer)
      )
      .map(_ => ())
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to Question: failed cursor $e")
          throw e
      }
  }

  def refuseInitialProposals(questionId: String): Future[Unit] = {
    client
      .post[ProposalIdResult](apiEndpoint = s"moderation/questions/$questionId/initial-proposals/refuse")
      .map(_ => ())
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to Question: failed cursor $e")
          throw e
      }
  }

  def getDataConfiguration(sequenceId: String): Future[DataConfiguration] = {
    client.get[DataConfiguration](apiEndpoint = s"moderation/sequences/$sequenceId/configuration").recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of converting to DataConfiguration: failed cursor $e")
        throw e
    }
  }

  def putDataConfiguration(sequenceId: String, questionId: String, request: DataConfiguration): Future[Boolean] = {
    client
      .put[Boolean](
        apiEndpoint = s"moderation/sequences/$sequenceId/$questionId/configuration",
        data = request.asJson.pretty(ApiService.printer)
      )
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to DataConfiguration: failed cursor $e")
          throw e
      }
  }

  case class InitialProposalRequest(content: String, author: AuthorRequest, tags: Array[String] = Array())

  case class AuthorRequest(age: Option[String], firstName: Option[String], lastName: Option[String])
}

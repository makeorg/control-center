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

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.backoffice.client.request.{Filter, Pagination, Sort}
import org.make.backoffice.client.{BadRequestHttpException, SingleResponse}
import org.make.backoffice.model.Idea
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object IdeaService extends ApiService with CirceClassFormatters {
  override val resourceName: String = "moderation/ideas"

  def listIdeas(pagination: Option[Pagination] = None,
                sort: Option[Sort] = None,
                filters: Option[Seq[Filter]] = None): Future[Seq[Idea]] = {

    var getIdeaUri: String = resourceName ?
      ("limit", pagination.map(_.perPage)) &
      ("skip", pagination.map(page => page.page * page.perPage - page.perPage)) &
      ("questionId", ApiService.getFieldValueFromFilters("questionId", filters))

    // search with keywords (=name) should not use order param to get results by relevance
    ApiService.getFieldValueFromFilters("name", filters) match {
      case Some(content) if content.nonEmpty => getIdeaUri = getIdeaUri & ("name", Some(content))
      case _                                 => getIdeaUri = getIdeaUri & ("sort", sort.map(_.field)) & ("order", sort.map(_.order))
    }

    client
      .get[Seq[Idea]](getIdeaUri)
      .recoverWith {
        case e: BadRequestHttpException =>
          js.Dynamic.global.console
            .log(s"instead of listing idea: failed cursor ${e.getMessage}")
          Future.failed(js.JavaScriptException(js.Error(e.errors.headOption.flatMap(_.message).getOrElse(""))))
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to ListTotalResponse: failed cursor $e")
          Future.failed(e)
      }

  }

  def getIdea(ideaId: String): Future[SingleResponse[Idea]] =
    client.get[Idea](resourceName / ideaId).map(SingleResponse.apply).recoverWith {
      case e: BadRequestHttpException =>
        js.Dynamic.global.console
          .log(s"instead of getting idea: failed cursor ${e.getMessage}")
        Future.failed(js.JavaScriptException(js.Error(e.errors.headOption.flatMap(_.message).getOrElse(""))))
      case e =>
        js.Dynamic.global.console.log(s"instead of converting to SingleResponse: failed cursor $e")
        Future.failed(e)
    }

  def createIdea(name: String, questionId: Option[String] = None): Future[SingleResponse[Idea]] = {
    val request: CreateIdeaRequest = CreateIdeaRequest(name = name, questionId = questionId)
    client
      .post[Idea](resourceName, data = request.asJson.pretty(ApiService.printer))
      .map(SingleResponse.apply)
      .recoverWith {
        case e: BadRequestHttpException =>
          js.Dynamic.global.console
            .log(s"instead of creating idea: failed cursor ${e.getMessage}")
          Future.failed(js.JavaScriptException(js.Error(e.errors.headOption.flatMap(_.message).getOrElse(""))))
        case e =>
          js.Dynamic.global.console.log(s"instead of creating idea: failed cursor $e")
          Future.failed(e)
      }
  }

  def updateIdea(ideaId: String, idea: Idea): Future[SingleResponse[Idea]] = {
    val request: UpdateIdeaRequest = UpdateIdeaRequest(name = idea.name)
    client
      .put[String](resourceName / ideaId, data = request.asJson.pretty(ApiService.printer))
      .map(_ => SingleResponse(idea))
      .recoverWith {
        case e: BadRequestHttpException =>
          js.Dynamic.global.console
            .log(s"instead of updating idea: failed cursor ${e.getMessage}")
          Future.failed(js.JavaScriptException(js.Error(e.errors.headOption.flatMap(_.message).getOrElse(""))))
        case e =>
          js.Dynamic.global.console.log(s"instead of updating idea: failed cursor $e")
          Future.failed(e)
      }
  }
}

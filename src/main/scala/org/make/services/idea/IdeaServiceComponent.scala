package org.make.services.idea

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.backoffice.models.{Idea, IdeasResult}
import org.make.client.request.{Filter, Pagination, Sort}
import org.make.client.{BadRequestHttpException, ListTotalResponse, SingleResponse}
import org.make.core.CirceClassFormatters
import org.make.core.URI._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

trait IdeaServiceComponent {
  def ideaService: IdeaService = new IdeaService

  class IdeaService extends ApiService with CirceClassFormatters {
    override val resourceName: String = "moderation/ideas"

    def listIdeas(pagination: Option[Pagination] = None,
                  sort: Option[Sort] = None,
                  filters: Option[Seq[Filter]] = None): Future[ListTotalResponse[Idea]] = {

      var getIdeaUri: String = resourceName ?
        ("limit", pagination.map(_.perPage)) &
        ("skip", pagination.map(page => page.page * page.perPage - page.perPage)) &
        ("language", ApiService.getFieldValueFromFilters("language", filters)) &
        ("country", ApiService.getFieldValueFromFilters("country", filters)) &
        ("operationId", ApiService.getFieldValueFromFilters("operationId", filters)) &
        ("themeId", ApiService.getFieldValueFromFilters("themeId", filters)) &
        ("question", ApiService.getFieldValueFromFilters("question", filters))

      // search with keywords (=name) should not use order param to get results by relevance
      ApiService.getFieldValueFromFilters("name", filters) match {
        case Some(content) if content.nonEmpty => getIdeaUri = getIdeaUri & ("name", Some(content))
        case _                                 => getIdeaUri = getIdeaUri & ("sort", sort.map(_.field)) & ("order", sort.map(_.order))
      }

      client
        .get[IdeasResult](getIdeaUri)
        .map { ideaResult =>
          ListTotalResponse.apply(total = ideaResult.total, data = ideaResult.results)
        }
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

    def createIdea(name: String,
                   language: Option[String] = None,
                   country: Option[String] = None,
                   operation: Option[String] = None,
                   theme: Option[String] = None,
                   question: Option[String] = None): Future[SingleResponse[Idea]] = {
      val request: CreateIdeaRequest = CreateIdeaRequest(
        name = name,
        language = language,
        country = country,
        operation = operation,
        theme = theme,
        question = question
      )
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
}

object IdeaServiceComponent extends IdeaServiceComponent

package org.make.services.idea

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.backoffice.models.{Idea, IdeasResult}
import org.make.client.request.{Filter, Pagination, Sort}
import org.make.client.{ListTotalResponse, SingleResponse}
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
                  filters: Option[Seq[Filter]] = None): Future[ListTotalResponse[Idea]] =
      client
        .get[IdeasResult](
          resourceName ?
            ("limit", pagination.map(_.perPage)) &
            ("skip", pagination.map(page => page.page * page.perPage - page.perPage)) &
            ("name", ApiService.getFieldValueFromFilters("name", filters)) &
            ("language", ApiService.getFieldValueFromFilters("language", filters)) &
            ("country", ApiService.getFieldValueFromFilters("country", filters)) &
            ("operationId", ApiService.getFieldValueFromFilters("operationId", filters)) &
            ("question", ApiService.getFieldValueFromFilters("question", filters))
        )
        .map { ideaResult =>
          ListTotalResponse.apply(total = ideaResult.total, data = ideaResult.results)
        }
        .recover {
          case e =>
            js.Dynamic.global.console.log(s"instead of converting to ListTotalResponse: failed cursor $e")
            throw e
        }

    def getIdea(ideaId: String): Future[SingleResponse[Idea]] =
      client.get[Idea](resourceName / ideaId).map(SingleResponse.apply).recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to SingleResponse: failed cursor $e")
          throw e
      }

    def createIdea(name: String,
                   language: Option[String] = None,
                   country: Option[String] = None,
                   operation: Option[String] = None,
                   question: Option[String] = None): Future[Idea] = {
      val request: CreateIdeaRequest = CreateIdeaRequest(
        name = name,
        language = language,
        country = country,
        operation = operation,
        question = question
      )
      client.post[Idea](resourceName, data = request.asJson.pretty(ApiService.printer)).recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of creating idea: failed cursor $e")
          throw e
      }
    }
  }
}

object IdeaServiceComponent extends IdeaServiceComponent

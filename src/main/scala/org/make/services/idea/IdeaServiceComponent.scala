package org.make.services.idea

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.backoffice.models.Idea
import org.make.client.SingleResponse
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

    def listIdeas(language: Option[String],
                  country: Option[String],
                  operation: Option[String],
                  question: Option[String]): Future[Seq[Idea]] =
      client
        .get[js.Array[Idea]](
          resourceName ? ("language", language) & ("country", country) & ("operation", operation) & ("question", question)
        )
        .map(ideaResult => ideaResult.toSeq)
        .recover {
          case e =>
            js.Dynamic.global.console.log(s"instead of converting to ListDataResponse: failed cursor $e")
            throw e
        }

    def getIdea(ideaId: String): Future[SingleResponse[Idea]] =
      client.get[Idea](resourceName / ideaId).map(SingleResponse.apply).recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to SingleResponse: failed cursor $e")
          throw e
      }

    def createIdea(name: String): Future[Idea] = {
      val request: CreateIdeaRequest = CreateIdeaRequest(name = name)
      client.post[Idea](resourceName, data = request.asJson.pretty(ApiService.printer)).recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of creating idea: failed cursor $e")
          throw e
      }
    }
  }
}

object IdeaServiceComponent extends IdeaServiceComponent

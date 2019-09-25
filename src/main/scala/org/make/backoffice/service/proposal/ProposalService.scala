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

package org.make.backoffice.service.proposal

import io.circe.syntax._
import org.make.backoffice.client.request.{Filter, Pagination, Sort}
import org.make.backoffice.client.{BadRequestHttpException, ListTotalResponse, SingleResponse}
import org.make.backoffice.model._
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object ProposalService extends ApiService with CirceClassFormatters {
  override val resourceName: String = "moderation/proposals"

  def getProposalById(id: String): Future[SingleResponse[SingleProposal]] =
    client.get[SingleProposal](resourceName / id).map(SingleResponse.apply).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of converting to SingleResponse: failed cursor $e")
        throw e
    }

  def proposals(pagination: Option[Pagination],
                sort: Option[Sort],
                filters: Option[Seq[Filter]]): Future[ListTotalResponse[Proposal]] = {

    var getProposalUri: String = resourceName ?
      ("limit", pagination.map(_.perPage)) &
      ("skip", pagination.map(page => page.page * page.perPage - page.perPage)) &
      ("proposalIds", ApiService.getFieldValueFromFilters("proposalIds", filters)) &
      ("themesIds", ApiService.getFieldValueFromFilters("themeId", filters)) &
      ("tagsIds", ApiService.getFieldValueFromFilters("tagsIds", filters)) &
      ("operationId", ApiService.getFieldValueFromFilters("operationId", filters)) &
      ("source", ApiService.getFieldValueFromFilters("source", filters)) &
      ("location", ApiService.getFieldValueFromFilters("location", filters)) &
      ("questionId", ApiService.getFieldValueFromFilters("questionId", filters)) &
      ("status", ApiService
        .getFieldValueFromFilters("status", filters)
        .orElse(Some(s"${Pending.shortName},${Postponed.shortName}"))) &
      ("country", ApiService.getFieldValueFromFilters("country", filters)) &
      ("language", ApiService.getFieldValueFromFilters("language", filters)) &
      ("toEnrich", ApiService.getFieldValueFromFilters("toEnrich", filters)) &
      ("minVotesCount", ApiService.getFieldValueFromFilters("minVotesCount", filters)) &
      ("minScore", ApiService.getFieldValueFromFilters("minScore", filters)) &
      ("initialProposal", ApiService.getFieldValueFromFilters("initialProposal", filters))

    // search with keywords (=content) should not use order param to get results by relevance
    ApiService.getFieldValueFromFilters("content", filters) match {
      case Some(content) if content.nonEmpty => getProposalUri = getProposalUri & ("content", Some(content))
      case _                                 => getProposalUri = getProposalUri & ("sort", sort.map(_.field)) & ("order", sort.map(_.order))
    }

    client
      .get[ProposalsResult](getProposalUri)
      .map { proposalsResult =>
        ListTotalResponse.apply(total = proposalsResult.total, data = proposalsResult.results)
      }
      .recoverWith {
        case e: BadRequestHttpException =>
          js.Dynamic.global.console
            .log(s"instead of listing proposals: failed cursor ${e.getMessage}")
          Future.failed(js.JavaScriptException(js.Error(e.errors.headOption.flatMap(_.message).getOrElse(""))))
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to ListTotalResponse: failed cursor $e")
          Future.failed(e)
      }
  }

  def updateProposal(proposalId: String,
                     newContent: Option[String],
                     tags: Seq[TagId] = Seq(TagId("default-tag")),
                     similarProposals: Seq[ProposalId] = Seq.empty,
                     ideaId: Option[IdeaId] = None,
                     questionId: Option[QuestionId] = None,
                     predictedTags: Option[Seq[TagId]],
                     modelName: Option[String]): Future[SingleProposal] = {
    val request: UpdateProposalRequest = UpdateProposalRequest(
      newContent = newContent,
      tags = tags,
      similarProposals = similarProposals,
      idea = ideaId,
      questionId = questionId,
      labels = Seq.empty, // deprecated field
      predictedTags = predictedTags,
      predictedTagsModelName = modelName
    )
    client
      .put[SingleProposal](
        resourceName / proposalId,
        urlParams = Seq.empty,
        data = request.asJson.pretty(ApiService.printer)
      )
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of updating proposal: failed cursor $e")
          throw e
      }
  }

  def validateProposal(proposalId: String,
                       newContent: Option[String],
                       sendNotificationEmail: Boolean,
                       tags: Seq[TagId] = Seq.empty,
                       similarProposals: Seq[ProposalId] = Seq.empty,
                       ideaId: Option[IdeaId] = None,
                       questionId: Option[QuestionId] = None,
                       predictedTags: Option[Seq[TagId]],
                       modelName: Option[String]): Future[SingleProposal] = {
    val request: ValidateProposalRequest = ValidateProposalRequest(
      newContent = newContent,
      sendNotificationEmail = sendNotificationEmail,
      tags = tags,
      similarProposals = similarProposals,
      idea = ideaId,
      questionId = questionId,
      labels = Seq.empty, // deprecated field
      predictedTags = predictedTags,
      predictedTagsModelName = modelName
    )
    client
      .post[SingleProposal](resourceName / proposalId / "accept", data = request.asJson.pretty(ApiService.printer))
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of validating proposal: failed cursor $e")
          throw e
      }
  }

  def refuseProposal(proposalId: String, refusalReason: Option[String], notifyUser: Boolean): Future[Unit] = {
    val request: RefuseProposalRequest =
      RefuseProposalRequest(sendNotificationEmail = notifyUser, refusalReason = refusalReason)
    client
      .post[SingleProposal](resourceName / proposalId / "refuse", data = request.asJson.pretty(ApiService.printer))
      .map(_ => {})
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of refusing proposal: failed cursor $e")
          throw e
      }
  }

  def postponeProposal(proposalId: String): Future[Unit] = {
    client.post[SingleProposal](resourceName / proposalId / "postpone").map(_ => {}).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of postponing proposal: failed cursor $e")
        throw e
    }
  }

  def changeProposalsIdea(ideaId: IdeaId, proposalsIds: Seq[ProposalId]): Future[Unit] = {
    val request = PatchProposalsIdeaRequest(proposalsIds, ideaId)
    client.post[Unit](resourceName / "change-idea", data = request.asJson.pretty(ApiService.printer)).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of updating proposals: failed cursor $e")
        throw e
    }
  }

  def lock(proposalId: String): Future[Unit] = {
    client.post[Unit](resourceName / proposalId / "lock")
  }

  def removeFromCluster(proposalId: String): Future[Unit] = {
    client.delete[Unit](apiEndpoint = resourceName / "similars" / proposalId, urlParams = Seq.empty, data = "")
  }

  def nextProposalToModerate(questionId: Option[String],
                             toEnrich: Boolean,
                             minVotesCount: Option[String],
                             minScore: Option[String]): Future[SingleResponse[SingleProposal]] = {
    val request = NextProposalToModerateRequest(
      questionId = questionId.map(QuestionId(_)),
      toEnrich = toEnrich,
      minVotesCount = minVotesCount,
      minScore = minScore
    )
    client
      .post[SingleProposal](resourceName / "next", data = request.asJson.pretty(ApiService.printer))
      .map(SingleResponse.apply)
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to SingleResponse: failed cursor $e")
          throw e
      }
  }

  def getProposalTags(proposalId: String): Future[PredictedTagsWithModelResponse] = {
    client
      .get[PredictedTagsWithModelResponse](resourceName / proposalId / "predicted-tags")
      .recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to PredictedTagsWithModelResponse: failed cursor $e")
          throw e
      }
  }
}

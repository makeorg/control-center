package org.make.services.proposal

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.backoffice.models._
import org.make.client.request.{Filter, Pagination, Sort}
import org.make.client.{ListTotalResponse, SingleResponse}
import org.make.core.CirceClassFormatters
import org.make.core.URI._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

trait ProposalServiceComponent {
  def proposalService: ProposalService = new ProposalService

  class ProposalService extends ApiService with CirceClassFormatters {
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
      val request: ExhaustiveSearchRequest =
        ExhaustiveSearchRequest.buildExhaustiveSearchRequest(pagination, sort, filters)
      client
        .post[ProposalsResult](resourceName / "search", data = request.asJson.pretty(ApiService.printer))
        .map(proposalsResult => ListTotalResponse.apply(proposalsResult.total, proposalsResult.results))
        .recover {
          case e =>
            js.Dynamic.global.console.log(s"instead of converting to ListTotalResponse: failed cursor $e")
            throw e
        }
    }

    def proposalsByIdea(ideaId: String): Future[ListTotalResponse[Proposal]] = {
      val request: ExhaustiveSearchRequest =
        ExhaustiveSearchRequest.buildExhaustiveSearchRequest(
          Some(Pagination(page = 1, perPage = 5000)),
          None,
          Some(Seq(Filter("status", js.Array(Accepted))))
        )
      client
        .post[ProposalsResult](resourceName / "search", data = request.asJson.pretty(ApiService.printer))
        .map(
          proposalsResult =>
            ListTotalResponse(
              total = proposalsResult.total,
              data = proposalsResult.results.filter(proposal => proposal.ideaId.exists(id => id == ideaId))
          )
        )
    }

    def updateProposal(proposalId: String,
                       newContent: Option[String],
                       theme: Option[ThemeId] = None,
                       labels: Seq[String] = Seq.empty,
                       tags: Seq[TagId] = Seq(TagId("default-tag")),
                       similarProposals: Seq[ProposalId] = Seq.empty,
                       ideaId: Option[IdeaId] = None): Future[SingleProposal] = {
      val request: UpdateProposalRequest = UpdateProposalRequest(
        newContent = newContent,
        theme = theme,
        labels = labels,
        tags = tags,
        similarProposals = similarProposals,
        ideaId = ideaId
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
                         theme: Option[ThemeId] = None,
                         labels: Seq[String] = Seq.empty,
                         tags: Seq[TagId] = Seq(TagId("default-tag")),
                         similarProposals: Seq[ProposalId] = Seq.empty,
                         ideaId: Option[IdeaId] = None): Future[SingleProposal] = {
      val request: ValidateProposalRequest = ValidateProposalRequest(
        newContent = newContent,
        sendNotificationEmail = sendNotificationEmail,
        theme = theme,
        labels = labels,
        tags = tags,
        similarProposals = similarProposals,
        ideaId = ideaId
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

    def getDuplicates(proposalId: String,
                      themeId: Option[ThemeId],
                      operation: Option[String]): Future[Seq[SimilarResult]] = {
      var headers: Map[String, String] = Map.empty
      themeId.foreach(themeId => headers += client.themeIdHeader -> themeId.value)
      operation.foreach(op    => headers += client.operationHeader -> op)
      client
        .get[js.Array[SimilarResult]](resourceName / proposalId / "duplicates", headers = headers)
        .map(similarResult => similarResult.toSeq)
        .recover {
          case e =>
            js.Dynamic.global.console.log(s"instead of converting to SimilarResult: failed cursor $e")
            throw e
        }
    }

    def lock(proposalId: String): Future[Unit] = {
      client.post[Unit](resourceName / proposalId / "lock")
    }

    def removeFromCluster(proposalId: String): Future[Unit] = {
      client.delete[Unit](apiEndpoint = resourceName / "similars" / proposalId, urlParams = Seq.empty, data = "")
    }

  }
}

object ProposalServiceComponent extends ProposalServiceComponent

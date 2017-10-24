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
    override val resourceName: String = "proposals"

    def getProposalById(id: String): Future[SingleResponse[SingleProposal]] =
      client.get[SingleProposal](resourceName / "moderation" / id).map(_.get).map(SingleResponse.apply).recover {
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
        .post[ProposalsResult](resourceName / "search" / "all", data = request.asJson.pretty(ApiService.printer))
        .map(_.get)
        .map(proposalsResult => ListTotalResponse.apply(proposalsResult.total, proposalsResult.results))
        .recover {
          case e =>
            js.Dynamic.global.console.log(s"instead of converting to ListTotalResponse: failed cursor $e")
            throw e
        }
    }

    def updateProposal(proposalId: String,
                       newContent: Option[String],
                       theme: Option[ThemeId] = None,
                       labels: Seq[String] = Seq.empty,
                       tags: Seq[TagId] = Seq(TagId("default-tag")),
                       similarProposals: Seq[ProposalId] = Seq.empty): Future[SingleProposal] = {
      val request: UpdateProposalRequest = UpdateProposalRequest(
        newContent = newContent,
        theme = theme,
        labels = labels,
        tags = tags,
        similarProposals = similarProposals
      )
      client
        .put[SingleProposal](
          resourceName / proposalId,
          urlParams = Seq.empty,
          data = request.asJson.pretty(ApiService.printer)
        )
        .map(_.get)
        .recover {
          case e =>
            js.Dynamic.global.console.log(s"instead of getting Proposal: $e")
            throw e
        }
    }

    def validateProposal(proposalId: String,
                         newContent: Option[String],
                         sendNotificationEmail: Boolean,
                         theme: Option[ThemeId] = None,
                         labels: Seq[String] = Seq.empty,
                         tags: Seq[TagId] = Seq(TagId("default-tag")),
                         similarProposals: Seq[ProposalId] = Seq.empty): Future[SingleProposal] = {
      val request: ValidateProposalRequest = ValidateProposalRequest(
        newContent = newContent,
        sendNotificationEmail = sendNotificationEmail,
        theme = theme,
        labels = labels,
        tags = tags,
        similarProposals = similarProposals
      )
      client
        .post[SingleProposal](resourceName / proposalId / "accept", data = request.asJson.pretty(ApiService.printer))
        .map(_.get)
        .recover {
          case e =>
            js.Dynamic.global.console.log(s"instead of getting Proposal: $e")
            throw e
        }
    }

    def refuseProposal(proposalId: String, refusalReason: Option[String], notifyUser: Boolean): Future[Boolean] = {
      val request: RefuseProposalRequest =
        RefuseProposalRequest(sendNotificationEmail = notifyUser, refusalReason = refusalReason)
      client
        .post[SingleProposal](resourceName / proposalId / "refuse", data = request.asJson.pretty(ApiService.printer))
        .map(_ => true)
        .recover {
          case e =>
            js.Dynamic.global.console.log(s"instead of getting Proposal: $e")
            throw e
            false
        }
    }

    def getDuplicates(proposalId: ProposalId): Future[ProposalsResult] =
      client.get[ProposalsResult](resourceName / proposalId.value / "duplicates").map(_.get)

    def invalidateSimilarProposal(proposalId: ProposalId, similarProposalId: ProposalId): Future[Unit] =
      Future.successful()

  }
}

object ProposalServiceComponent extends ProposalServiceComponent

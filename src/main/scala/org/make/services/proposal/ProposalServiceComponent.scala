package org.make.services.proposal

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.backoffice.models.Proposal
import org.make.client.{Filter, _}
import org.make.core.CirceClassFormatters
import org.make.core.URI._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

trait ProposalServiceComponent extends CirceClassFormatters {
  def proposalService: ProposalService = new ProposalService()

  class ProposalService extends ApiService with CirceClassFormatters with DefaultMakeApiHttpClientComponent {
    override val resourceName: String = "proposal"

    def getProposalById(id: String): Future[SingleResponse[Proposal]] =
      client.get[Proposal](resourceName / id).map(SingleResponse.apply)

    def proposals(pagination: Option[Pagination],
                  sort: Option[Sort],
                  filters: Option[Seq[Filter]]): Future[ListTotalResponse[Proposal]] = {
      val request: ExhaustiveSearchRequest =
        ExhaustiveSearchRequest.buildExhaustiveSearchRequest(pagination, sort, filters)
      client
        .post[Seq[Proposal]](resourceName / "search" / "all", data = request.asJson.pretty(ApiService.printer))
        .map(ListTotalResponse.apply)
        .recover {
          case e =>
            js.Dynamic.global.console.log(s"instead of converting to ListTotalResponse: failed cursor $e")
            throw e
        }
    }

  }
}

object ProposalService extends ProposalServiceComponent

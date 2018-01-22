package org.make.services.operation

import org.make.backoffice.models._
import org.make.client.{ListDataResponse, ListTotalResponse}
import org.make.core.CirceClassFormatters
import org.make.core.URI._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object OperationServiceComponent {
  def operationService: OperationService = new OperationService

  private var cachedOperations: Seq[Operation] = Seq.empty

  class OperationService extends ApiService with CirceClassFormatters {
    override val resourceName: String = "moderation/operations"

    private def loadOperationList(forceReload: Boolean, retries: Int = 3): Future[Seq[Operation]] = {
      if (cachedOperations.nonEmpty && !forceReload) {
        Future.successful(cachedOperations)
      } else {
        client
          .get[OperationsResult](resourceName ? ("slug", None))
          .map {
            _.results.toSeq
          }
          .map { operations =>
            cachedOperations = operations
            operations
          }
      }
    }

    def getOperationById(id: OperationId, forceReload: Boolean = false): Future[Operation] = {
      loadOperationList(forceReload).map(_.filter(_.id == id).head)
    }

    def getOperationByIds(ids: js.Array[String]): Future[ListDataResponse[Operation]] = {
      if (ids.head.length != 0) {
        Future
          .traverse(ids.toSeq)(id => getOperationById(OperationId(id)))
          .map(ListDataResponse.apply)
      } else Future.successful(ListDataResponse(Seq.empty))
    }

    def operations(slug: Option[String] = None, forceReload: Boolean = false): Future[ListTotalResponse[Operation]] = {
      loadOperationList(forceReload).map { operations =>
        slug match {
          case Some(s) => operations.filter(_.slug == s)
          case _       => operations
        }
      }.map { operations =>
        ListTotalResponse(total = operations.length, data = operations.toJSArray)
      }
    }
  }
}

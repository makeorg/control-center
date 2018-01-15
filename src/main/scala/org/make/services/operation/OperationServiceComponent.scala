package org.make.services.operation

import org.make.backoffice.models._
import org.make.client.ListDataResponse
import org.make.core.CirceClassFormatters
import org.make.core.URI._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object OperationServiceComponent {
  def operationService: OperationService = new OperationService

  var operationsCache: Seq[Operation] = Seq.empty

  class OperationService extends ApiService with CirceClassFormatters {
    override val resourceName: String = "moderation/operations"

    def getOperationById(id: String, forceReload: Boolean = false): Future[Operation] =
      if (!forceReload && operationsCache.exists(_.id == id)) {
        Future.successful(operationsCache.find(_.id == id).get)
      } else {
        client
          .get[Operation](resourceName / id)
          .map { operation =>
            if (!operationsCache.exists(_.id == operation.id)) {
              operationsCache +:= operation
            }
            operation
          }
          .recover {
            case e =>
              js.Dynamic.global.console.log(s"instead of converting to Operation: failed cursor $e")
              throw e
          }
      }

    def getOperationByIds(ids: js.Array[String]): Future[ListDataResponse[Operation]] =
      Future
        .traverse(ids.toSeq)(id => getOperationById(id))
        .map(ListDataResponse.apply)

    def operations(slug: Option[String] = None, forceReload: Boolean = false): Future[Seq[Operation]] = {
      if (!forceReload && (slug.isEmpty || operationsCache.exists(_.slug == slug.getOrElse("")))) {
        slug match {
          case Some(s) => Future.successful(Seq(operationsCache.find(_.id == s).get))
          case None    => Future.successful(operationsCache)
        }
      } else {
        client
          .get[Seq[Operation]](resourceName ? ("slug", slug))
          .map { operations =>
            operationsCache = operations
            operationsCache
          }
          .recover {
            case e =>
              js.Dynamic.global.console.log(s"instead of converting to ListTotalResponse: failed cursor $e")
              throw e
          }
      }
    }
  }

}

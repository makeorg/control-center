package org.make.backoffice.service.operation

import org.make.backoffice.model._
import org.make.backoffice.client.{ListDataResponse, ListTotalResponse}
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._
import org.make.backoffice.service.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object OperationService extends ApiService with CirceClassFormatters {

  private var cachedOperations: Seq[Operation] = Seq.empty
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

  def getOperationById(id: OperationId, forceReload: Boolean = false): Future[Option[Operation]] = {
    loadOperationList(forceReload).map(_.find(_.id == id.value))
  }

  def getOperationByIds(ids: js.Array[String]): Future[ListDataResponse[Operation]] = {
    if (ids.head.length != 0) {
      Future
        .traverse(ids.toSeq)(id => getOperationById(OperationId(id)))
        .map(_.filter(_.isDefined).map(_.get))
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

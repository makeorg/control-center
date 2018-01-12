package org.make.services.operation

import org.make.backoffice.models._
import org.make.core.CirceClassFormatters
import org.make.core.URI._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

trait OperationServiceComponent {
  def operationService: OperationService = new OperationService

  class OperationService extends ApiService with CirceClassFormatters {
    override val resourceName: String = "moderation/operations"

    def getOperationById(id: String): Future[Operation] =
      client.get[Operation](resourceName / id).recover {
        case e =>
          js.Dynamic.global.console.log(s"instead of converting to Operation: failed cursor $e")
          throw e
      }

    def operations(slug: Option[String] = None): Future[Seq[Operation]] = {
      client
        .get[Seq[Operation]](resourceName ? ("slug", slug))
        .recover {
          case e =>
            js.Dynamic.global.console.log(s"instead of converting to ListTotalResponse: failed cursor $e")
            throw e
        }
    }
  }

}

object OperationServiceComponent extends OperationServiceComponent

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

package org.make.backoffice.service.partner

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.backoffice.model.PartnerId
import org.make.backoffice.service.ApiService
import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.util.uri._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object PartnerService extends ApiService with CirceClassFormatters {
  override val resourceName: String = "admin/partners"

  def createPartner(request: CreatePartnerRequest): Future[PartnerIdResponse] = {
    client.post[PartnerIdResponse](resourceName, data = request.asJson.pretty(ApiService.printer)).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of creating partner: failed cursor $e")
        throw e
    }
  }

  def editPartner(request: UpdatePartnerRequest, partnerId: String): Future[PartnerIdResponse] = {
    client.put[PartnerIdResponse](resourceName / partnerId, data = request.asJson.pretty(ApiService.printer)).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of creating partner: failed cursor $e")
        throw e
    }
  }

  def deletePartner(partnerId: String): Future[Unit] = {
    client.delete[Unit](resourceName / partnerId).recover {
      case e =>
        js.Dynamic.global.console.log(s"instead of creating partner: failed cursor $e")
        throw e
    }
  }
}

final case class CreatePartnerRequest(name: String,
                                      logo: Option[String],
                                      link: Option[String],
                                      organisationId: Option[String],
                                      partnerKind: String,
                                      questionId: String,
                                      weight: Double)

final case class UpdatePartnerRequest(name: String,
                                      logo: Option[String],
                                      link: Option[String],
                                      partnerKind: String,
                                      weight: Double)

final case class PartnerIdResponse(partnerId: PartnerId)

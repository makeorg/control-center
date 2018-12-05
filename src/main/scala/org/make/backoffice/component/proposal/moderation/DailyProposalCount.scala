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

package org.make.backoffice.component.proposal.moderation
import java.time.format.DateTimeFormatter
import java.time.{ZoneOffset, ZonedDateTime}

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.backoffice.client.request.Filter
import org.make.backoffice.service.operation.OperationService
import org.make.backoffice.service.proposal.{Pending, ProposalService, ProposalStatus}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}
import org.make.backoffice.facade.MaterialUi._

object DailyProposalCount {
  final case class DailyProposalCountProps(operationSlug: String, status: ProposalStatus = Pending)
  final case class DailyProposalCountState(count: Int)

  def loadProposalCount(operationSlug: String, status: ProposalStatus): Future[Int] = {
    val dateFormatter: DateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC)
    val thisMorning = ZonedDateTime.now().withHour(8).format(dateFormatter)
    OperationService
      .operations(Some(operationSlug))
      .flatMap(_.data.headOption match {
        case Some(operation) =>
          ProposalService
            .proposals(
              None,
              None,
              Some(
                Seq(
                  Filter("operationId", operation.id),
                  Filter("status", status.shortName),
                  Filter("createdBefore", thisMorning)
                )
              )
            )
            .map(_.total)
        case None => Future.successful(0)
      })
  }

  val reactClass: ReactClass =
    React.createClass[DailyProposalCountProps, DailyProposalCountState](
      displayName = "DailyProposalCount",
      getInitialState = _ => DailyProposalCountState(0),
      componentWillMount = { self =>
        loadProposalCount(self.props.wrapped.operationSlug, self.props.wrapped.status).onComplete {
          case Success(count) => self.setState(_.copy(count = count))
          case Failure(e)     => js.Dynamic.global.console.log(e.getMessage)
        }
      },
      render = { self =>
        <.Card(^.style := Map("text-align" -> "center"))(
          <.CardTitle(
            ^.title := s"${self.props.wrapped.status.shortName} proposals for ${self.props.wrapped.operationSlug}"
          )(),
          <.CardText()(<.h1()(self.state.count.toString))
        )
      }
    )
}

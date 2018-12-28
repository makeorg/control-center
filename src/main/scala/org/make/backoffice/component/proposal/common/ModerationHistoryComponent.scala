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

package org.make.backoffice.component.proposal.common

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags.Element
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.SingleProposal

import scala.scalajs.js
import scala.scalajs.js.Date

object ModerationHistoryComponent {
  case class HistoryProps(proposal: SingleProposal)
  case class HistoryState(contentActions: ReactElement)

  def explicitAction(actionType: String, arguments: js.Dictionary[String], content: String): Element = {
    val actions: Map[String, Element] = Map(
      "propose" -> <.p()("Proposal creation"),
      "refuse" -> <.p()(s"Refused with reason: ${arguments.getOrElse("refusalReason", "no reason provided")}"),
      "accept" -> <.div()(
        "Accepted with:",
        <.ul()(
          <.li()(s"content: $content"),
          <.li()(s"tags: ${arguments.getOrElse("tags", "no tags provided")}"),
          <.li()(s"idea: ${arguments.getOrElse("idea", "no idea provided")}")
        )
      ),
      "update" -> <.div()(
        "Update with:",
        <.ul()(
          <.li()(s"content: $content"),
          <.li()(s"tags: ${arguments.getOrElse("tags", "no tags provided")}"),
          <.li()(s"idea: ${arguments.getOrElse("idea", "no idea provided")}")
        )
      ),
      "lock" -> <.p()("Start Moderation")
    )
    val defaultMessage: Element = if (arguments.isEmpty) {
      <.p()(actionType)
    } else {
      <.p()(s"[$actionType] with arguments:${arguments.mkString(",")}")
    }
    actions.getOrElse(actionType, defaultMessage)
  }

  def contentActions(proposal: SingleProposal): ReactElement =
    <.TableBody(^.displayRowCheckbox := false)(
      proposal.events
        .map(_.sortBy(-_.date.toString.toLong).map { event =>
          <.TableRow(^.key := s"${event.date}-${event.actionType}")(
            <.TableRowColumn()(new Date(event.date.toString.toLong).toUTCString()),
            <.TableRowColumn()(<.div()(explicitAction(event.actionType, event.arguments, proposal.content))),
            <.TableRowColumn()(event.user.map(user => user.firstName.orElse(user.organisationName)).getOrElse("-"))
          )
        })
        .getOrElse(
          <.TableRow(^.key := "empty")(<.TableRowColumn()(), <.TableRowColumn()("No content"), <.TableRowColumn()())
        )
    )

  lazy val reactClass: ReactClass = React.createClass[HistoryProps, HistoryState](
    getInitialState = { self =>
      HistoryState(contentActions = contentActions(self.props.wrapped.proposal))
    },
    componentWillReceiveProps = { (self, props) =>
      self.setState(HistoryState(contentActions = contentActions(props.wrapped.proposal)))
    },
    render = self =>
      <.Card(^.style := Map("marginTop" -> "1em"))(
        <.CardTitle(^.title := "Moderation history")(),
        <.Table(^.selectable := false)(
          <.TableHeader(^.displaySelectAll := false)(
            <.TableRow()(<.TableHeaderColumn()("Date"), <.TableHeaderColumn()("Action"), <.TableHeaderColumn()("Actor"))
          ),
          self.state.contentActions
        )
    )
  )
}

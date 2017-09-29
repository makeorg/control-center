package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.backoffice.facades.Table._
import org.make.backoffice.facades.TableBody._
import org.make.backoffice.facades.TableHeader._
import org.make.backoffice.facades.TableHeaderColumn.TableHeaderColumnVirtualDOMElements
import org.make.backoffice.facades.TableRow.TableRowVirtualDOMElements
import org.make.backoffice.facades.TableRowColumn.TableRowColumnVirtualDOMElements
import org.make.backoffice.models.{ProposalAction, SingleProposal}

import scala.scalajs.js
import scala.scalajs.js.Date

object ModerationHistoryComponent {
  case class HistoryProps(proposal: SingleProposal)
  case class HistoryState(contentActions: ReactElement)

  def explicitAction(actionType: String, arguments: js.Dictionary[String]): String = {
    val actions = Map(
      "propose" -> "Proposal creation",
      "accept" -> "Accepted",
      "refuse" -> s"Refused with reason: ${arguments.getOrElse("reason", "no reason provided")}"
    )
    val defaultMessage: String = if (arguments.isEmpty) {
      actionType
    } else {
      s"[$actionType] with arguments:${arguments.mkString(",")}"
    }
    actions.getOrElse(actionType, defaultMessage)
  }

  def contentActions(events: js.UndefOr[js.Array[ProposalAction]]): ReactElement =
    <.TableBody(^.displayRowCheckbox := false)(
      events
        .map(_.sortBy(-_.date.toString.toLong).map { event =>
          <.TableRow(^.key := s"${event.date}-${event.actionType}")(
            <.TableRowColumn()(new Date(event.date.toString.toLong).toUTCString()),
            <.TableRowColumn()(explicitAction(event.actionType, event.arguments)),
            <.TableRowColumn()(event.user.map(_.firstName).getOrElse("-"))
          )
        })
        .getOrElse(
          <.TableRow(^.key := "empty")(<.TableRowColumn()(), <.TableRowColumn()("No content"), <.TableRowColumn()())
        )
    )

  lazy val reactClass: ReactClass = React.createClass[HistoryProps, HistoryState](
    getInitialState = self => {
      HistoryState(contentActions = contentActions(self.props.wrapped.proposal.events))
    },
    componentWillReceiveProps = (self, props) => {
      self.setState(HistoryState(contentActions = contentActions(props.wrapped.proposal.events)))
    },
    render = self =>
      <.div()(
        <.Table(^.selectable := false)(
          <.TableHeader(^.displaySelectAll := false)(
            <.TableRow()(<.TableHeaderColumn()("Date"), <.TableHeaderColumn()("Action"), <.TableHeaderColumn()("Actor"))
          ),
          self.state.contentActions
        )
    )
  )
}

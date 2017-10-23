package org.make.backoffice.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags.Element
import org.make.backoffice.facades.MaterialUi._
import org.make.backoffice.helpers.Configuration
import org.make.backoffice.models.{ProposalAction, SingleProposal, ThemeId}

import scala.scalajs.js
import scala.scalajs.js.Date

object ModerationHistoryComponent {
  case class HistoryProps(proposal: SingleProposal)
  case class HistoryState(contentActions: ReactElement)

  def explicitAction(actionType: String, arguments: js.Dictionary[String]): Element = {
    val actions: Map[String, Element] = Map(
      "propose" -> <.p()("Proposal creation"),
      "refuse" -> <.p()(s"Refused with reason: ${arguments.getOrElse("refusalReason", "no reason provided")}"),
      "accept" -> <.div()(
        "Accepted with:",
        <.ul()(
          <.li()({
            val theme: String =
              arguments
                .get("theme")
                .map(themeId => Configuration.getThemeFromThemeId(ThemeId(themeId)))
                .getOrElse("no theme provided")
            s"theme: $theme"
          }),
          <.li()(s"tags: ${arguments.getOrElse("tags", "no tags provided")}"),
          <.li()(s"labels: ${arguments.getOrElse("labels", "no labels provided")}")
        )
      )
    )
    val defaultMessage: Element = if (arguments.isEmpty) {
      <.p()(actionType)
    } else {
      <.p()(s"[$actionType] with arguments:${arguments.mkString(",")}")
    }
    actions.getOrElse(actionType, defaultMessage)
  }

  def contentActions(events: js.UndefOr[js.Array[ProposalAction]]): ReactElement =
    <.TableBody(^.displayRowCheckbox := false)(
      events
        .map(_.sortBy(-_.date.toString.toLong).map { event =>
          <.TableRow(^.key := s"${event.date}-${event.actionType}")(
            <.TableRowColumn()(new Date(event.date.toString.toLong).toUTCString()),
            <.TableRowColumn()(<.div()(explicitAction(event.actionType, event.arguments))),
            <.TableRowColumn()(event.user.map(_.firstName).getOrElse("-"))
          )
        })
        .getOrElse(
          <.TableRow(^.key := "empty")(<.TableRowColumn()(), <.TableRowColumn()("No content"), <.TableRowColumn()())
        )
    )

  lazy val reactClass: ReactClass = React.createClass[HistoryProps, HistoryState](
    getInitialState = { self =>
      HistoryState(contentActions = contentActions(self.props.wrapped.proposal.events))
    },
    componentWillReceiveProps = { (self, props) =>
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

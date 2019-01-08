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

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Filter._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.List._
import org.make.backoffice.facade.AdminOnRest.ShowButton._
import org.make.backoffice.facade.Choice
import org.make.backoffice.model.Proposal
import org.make.backoffice.service.proposal._

import scala.scalajs.js

object ProposalList {

  case class ProposalListProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass = React.createClass[ProposalListProps, Unit](
    displayName = "ProposalList",
    render = self =>
      <.div()(
        <.List(
          ^.title := "Proposals",
          ^.location := self.props.location,
          ^.resource := Resource.proposals,
          ^.hasCreate := false,
          ^.filters := filterList(),
          ^.sort := Map("field" -> "createdAt", "order" -> "DESC")
        )(
          <.Datagrid(^.rowStyle := rowStyle)(
            <.ShowButton()(),
            <.TextField(^.source := "content", ^.sortable := false)(),
            <.TextField(^.source := "status", ^.sortable := false)(),
            <.ReferenceField(
              ^.source := "questionId",
              ^.label := "question",
              ^.reference := Resource.questions,
              ^.linkType := false,
              ^.allowEmpty := true,
              ^.sortable := false
            )(<.TextField(^.source := "slug")()),
            <.TextField(^.source := "context.source", ^.label := "source", ^.sortable := false)(),
            <.DateField(^.source := "createdAt", ^.label := "Date", ^.showTime := true)()
          )
        )
    )
  )

  def rowStyle: RowStyle = { (record, _) =>
    val proposal = record.asInstanceOf[Proposal]

    proposal.status match {
      case Postponed.shortName => js.Dictionary("backgroundColor" -> "#fda")
      case Refused.shortName   => js.Dictionary("backgroundColor" -> "#fdd")
      case _                   => js.Dictionary.empty
    }
  }

  def filterList(): ReactElement = {
    val statusChoices = Seq(
      Choice(Pending.shortName, "Pending"),
      Choice(Postponed.shortName, "Postponed"),
      Choice(Refused.shortName, "Refused"),
      Choice(Archived.shortName, "Archived")
    )

    <.Filter(^.resource := Resource.proposals)(
      Seq(
        //TODO: add the possibility to search by userId or proposalId
        <.TextInput(^.label := "Search", ^.source := "content", ^.alwaysOn := true)(),
        <.SelectArrayInput(^.label := "Status", ^.source := "status", ^.alwaysOn := true, ^.choices := statusChoices)(),
        <.ReferenceInput(
          ^.label := "Question",
          ^.translateLabel := ((label: String) => label),
          ^.source := "questionId",
          ^.reference := Resource.questions,
          ^.sort := Map("field" -> "slug", "order" -> "ASC"),
          ^.perPage := 100,
          ^.alwaysOn := true
        )(<.SelectInput(^.optionText := "slug")()),
        <.TextInput(^.label := "Source", ^.source := "source", ^.alwaysOn := false)(),
        //TODO: add filter on: "reason for refusal" and "moderator"
      )
    )
  }
}

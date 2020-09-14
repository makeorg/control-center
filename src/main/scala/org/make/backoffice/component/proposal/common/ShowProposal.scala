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
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.proposal.common.ShowProposalComponents.{Context, ShowProposalComponentsProps}
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Show._
import org.make.backoffice.facade.AdminOnRest.Tab._
import org.make.backoffice.facade.AdminOnRest.TabbedShowLayout._
import org.make.backoffice.model.SingleProposal

object ShowProposal {

  case class ShowProposalProps() extends RouterProps

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ShowProposalProps, Unit](
    displayName = "ShowProposal",
    render = self =>
      <.Show(
        ^.location := self.props.location,
        ^.resource := Resource.proposals,
        ^.`match` := self.props.`match`,
        ^.showTitle := <.ShowProposalTitle()()
      )(
        <.TabbedShowLayout()(
          <.Tab(^.label := "Actions")(
            <.ShowProposalComponents(
              ^.wrapped := ShowProposalComponentsProps(
                hash = org.scalajs.dom.window.location.hash,
                proposal = None,
                context = Context.List,
                minVotesCount = None,
                toEnrichMinScore = None,
                withTags = true
              )
            )()
          ),
          <.Tab(^.label := "Infos", ^.disabled := false)(
            <.TextField(^.source := "id")(),
            <.FunctionField(^.label := "User name", ^.render := { record =>
              val proposal = record.asInstanceOf[SingleProposal]
              proposal.author.displayName.getOrElse("").toString
            })(),
            <.TextField(^.source := "author.profile.age", ^.label := "User age")(),
            <.TextField(^.source := "author.profile.postalCode", ^.label := "User location")(),
            <.DateField(
              ^.source := "createdAt",
              ^.label := "date",
              ^.options := Map("weekday" -> "long", "year" -> "numeric", "month" -> "long", "day" -> "numeric"),
              ^.locales := "en-EN"
            )(),
            <.TextField(^.source := "status")(),
            <.ReferenceField(
              ^.source := "questionId",
              ^.label := "question",
              ^.reference := Resource.questions,
              ^.linkType := false,
              ^.allowEmpty := true
            )(<.TextField(^.source := "slug")()),
            <.TextField(^.source := "context.language", ^.label := "Language")(),
            <.TextField(^.source := "context.country", ^.label := "Country")(),
            <.TextField(^.source := "context.source", ^.label := "source")()
          ),
          <.Tab(^.label := "Stats", ^.disabled := false)(<.FunctionField(^.label := "Stats", ^.render := { record =>
            val proposal = record.asInstanceOf[SingleProposal]
            <.StatsValidatedProposal(^.wrapped := ValidatedProposalStats.ValidatedProposalStatsProps(proposal))()
          })()),
          <.Tab(^.label := "History", ^.disabled := false)(<.FunctionField(^.label := "History", ^.render := { record =>
            val proposal = record.asInstanceOf[SingleProposal]
            <.ModerationHistoryComponent(^.wrapped := ModerationHistoryComponent.HistoryProps(proposal))()
          })())
        )
    )
  )
}

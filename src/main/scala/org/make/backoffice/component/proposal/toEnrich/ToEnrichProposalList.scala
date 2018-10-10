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

package org.make.backoffice.component.proposal.toEnrich

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.{redux, React}
import org.make.backoffice.client.Resource
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.Filter._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.List._
import org.make.backoffice.facade.AdminOnRest.ShowButton._
import org.make.backoffice.model.{AppState, Proposal}
import org.make.backoffice.service.proposal._
import org.make.backoffice.util.{Configuration, QueryString}
import org.make.backoffice.util.uri._

object ToEnrichProposalList {

  //TODO: fix: should do 1 call instead of 3.
  def selectorFactory: Dispatch => (AppState, Props[Unit]) => Unit =
    (_: Dispatch) =>
      (_: AppState, props: Props[Unit]) => {
        val params = QueryString.parse(props.location.search)
        if (params.isEmpty) {
          props.history.push(
            s"""?filter={"minVotesCount"%3A"${Configuration.toEnrichMinVotesCount}"%2C"minScore"%3A"${Configuration.toEnrichMinScore}"}"""
              & ("page", 1)
              & ("perPage", 10)
              & ("sort", "createdAt")
              & ("order", "DESC")
          )
        }
    }

  def apply(): ReactClass = WithRouter(redux.ReactRedux.connectAdvanced(selectorFactory)(reactClass))

  private lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    displayName = "ToEnrichProposalList",
    render = self =>
      <.div()(
        <.List(
          ^.title := "Proposals to enrich",
          ^.location := self.props.location,
          ^.resource := Resource.toEnrichProposals,
          ^.hasCreate := false,
          ^.filters := filterList(),
          ^.filter := Map("status" -> Accepted.shortName, "toEnrich" -> true),
          ^.sort := Map("field" -> "createdAt", "order" -> "DESC")
        )(
          <.Datagrid()(
            <.ShowButton()(),
            <.TextField(^.source := "content", ^.sortable := false)(),
            <.TextField(^.source := "status", ^.sortable := false)(),
            <.FunctionField(^.label := "theme", ^.render := { record =>
              val proposal = record.asInstanceOf[Proposal]
              proposal.themeId.map { id =>
                Configuration.getThemeFromThemeId(id)
              }
            })(),
            <.ReferenceField(
              ^.source := "operationId",
              ^.label := "operation",
              ^.reference := Resource.operations,
              ^.linkType := false,
              ^.allowEmpty := true,
              ^.sortable := false
            )(<.TextField(^.source := "slug")()),
            <.TextField(^.source := "context.source", ^.label := "source", ^.sortable := false)(),
            <.RichTextField(^.source := "context.question", ^.label := "question", ^.sortable := false)(),
            <.DateField(^.source := "createdAt", ^.label := "Date", ^.showTime := true)()
          )
        )
    )
  )

  def filterList(): ReactElement = {
    <.Filter(^.resource := Resource.proposals)(
      Seq(
        <.TextInput(^.label := "Search", ^.source := "content", ^.alwaysOn := true)(),
        <.TextInput(
          ^.label := "Minimum votes count",
          ^.source := "minVotesCount",
          ^.alwaysOn := true,
          ^.defaultValue := s"${Configuration.toEnrichMinVotesCount}"
        )(),
        <.TextInput(
          ^.label := "Minimum score",
          ^.source := "minScore",
          ^.alwaysOn := true,
          ^.defaultValue := s"${Configuration.toEnrichMinScore}"
        )(),
        <.SelectInput(
          ^.label := "Country",
          ^.source := "country",
          ^.alwaysOn := false,
          ^.allowEmpty := true,
          ^.choices := Configuration.choicesCountryFilter
        )(),
        <.ReferenceInput(
          ^.label := "Operation",
          ^.source := "operationId",
          ^.reference := Resource.operations,
          ^.alwaysOn := true
        )(<.SelectInput(^.optionText := "slug")())
      )
    )
  }
}

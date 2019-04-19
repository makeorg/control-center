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

package org.make.backoffice.component.question

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.backoffice.client.Resource
import org.make.backoffice.component.RichVirtualDOMElements
import org.make.backoffice.component.question.InitialProposalComponent.InitialProposalComponentProps
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.FormTab._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.SaveButton._
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.AdminOnRest.TabbedForm._
import org.make.backoffice.facade.AdminOnRest.required
import org.make.backoffice.service.proposal.{Accepted, Refused}
import org.make.backoffice.util.DateParser

import scala.scalajs.js

object EditQuestion {

  case class EditQuestionProps() extends RouterProps
  case class EditQuestionState(reload: Boolean)

  def apply(): ReactClass = reactClass

  private lazy val reactClass =
    React
      .createClass[EditQuestionProps, EditQuestionState](
        displayName = "EditQuestion",
        getInitialState = _ => EditQuestionState(reload = false),
        render = self => {

          def reloadComponent = { () =>
            self.setState(state => state.copy(reload = true))
          }

          <.Edit(
            ^.resource := Resource.operationsOfQuestions,
            ^.location := self.props.location,
            ^.`match` := self.props.`match`,
            ^.hasList := true
          )(
            <.TabbedForm(^.submitOnEnter := false)(
              <.FormTab(^.label := "infos")(
                <.DateInput(
                  ^.label := "Start Date",
                  ^.translateLabel := ((label: String) => label),
                  ^.source := "startDate",
                  ^.parse := ((date: js.UndefOr[String]) => date.map(DateParser.parseDate))
                )(),
                <.DateInput(
                  ^.label := "End Date",
                  ^.translateLabel := ((label: String) => label),
                  ^.source := "endDate",
                  ^.parse := ((date: js.UndefOr[String]) => date.map(DateParser.parseDate))
                )(),
                <.ReferenceField(
                  ^.label := "Operation",
                  ^.translateLabel := ((label: String) => label),
                  ^.source := "operationId",
                  ^.reference := Resource.operations,
                  ^.linkType := false
                )(<.TextField(^.source := "slug")()),
                <.TextField(
                  ^.label := "Operation Title",
                  ^.translateLabel := ((label: String) => label),
                  ^.source := "operationTitle"
                )(),
                <.TextField(^.source := "country")(),
                <.TextField(^.source := "language")(),
                <.TextInput(
                  ^.source := "question",
                  ^.allowEmpty := false,
                  ^.validate := required,
                  ^.options := Map("fullWidth" -> true)
                )(),
                <.TextField(^.source := "slug")(),
                <.BooleanInput(
                  ^.label := "Can propose",
                  ^.translateLabel := ((label: String) => label),
                  ^.source := "canPropose"
                )()
              ),
              <.FormTab(^.label := "initials proposals")(if (!self.state.reload) {
                js.Array(
                    <.InitialProposal(^.wrapped := InitialProposalComponentProps(reloadComponent))(),
                    <.ReferenceManyField(
                      ^.reference := Resource.proposals,
                      ^.target := "questionId",
                      ^.addLabel := false,
                      ^.perPage := 50,
                      ^.filter := Map(
                        "status" -> s"${Accepted.shortName},${Refused.shortName}",
                        "initialProposal" -> true
                      )
                    )(
                      <.Datagrid()(
                        <.EditButton.empty,
                        <.TextField(^.source := "content")(),
                        <.TextField(^.label := "Author", ^.source := "author.firstName")(),
                        <.TextField(^.source := "status")()
                      )
                    )
                  )
                  .toSeq
              } else {
                self.setState(_.copy(reload = false))
              })
            )
          )
        }
      )
}

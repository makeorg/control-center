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
import org.make.backoffice.component.question.CreatePartnerComponent.CreatePartnerComponentProps
import org.make.backoffice.component.question.DeletePartnerComponent.DeletePartnerComponentProps
import org.make.backoffice.component.question.EditPartnerComponent.EditPartnerComponentProps
import org.make.backoffice.component.question.InitialProposalComponent.InitialProposalComponentProps
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.FormTab._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.TabbedForm._
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.service.proposal.{Accepted, Refused}

import scala.scalajs.js

object EditQuestionConfiguration {

  case class EditQuestionConfigurationProps() extends RouterProps
  case class EditQuestionConfigurationState(reload: Boolean)

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[EditQuestionConfigurationProps, EditQuestionConfigurationState](
        displayName = "EditQuestionConfiguration",
        getInitialState = _ => EditQuestionConfigurationState(reload = false),
        render = self => {

          def reloadComponent = { () =>
            self.setState(state => state.copy(reload = true))
          }

          <.Edit(
            ^.resource := Resource.questionsConfiguration,
            ^.location := self.props.location,
            ^.`match` := self.props.`match`,
            ^.hasList := true
          )(
            <.TabbedForm(^.toolbar := null)(
              <.FormTab(^.label := "Data configuration")(<.DataConfigurationComponent.empty),
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
              }),
              <.FormTab(^.label := "Partners")(if (!self.state.reload) {
                js.Array(
                    <.CreatePartnerComponent(^.wrapped := CreatePartnerComponentProps(reloadComponent))(),
                    <.ReferenceManyField(
                      ^.reference := Resource.partners,
                      ^.target := "questionId",
                      ^.addLabel := false,
                      ^.perPage := 50,
                      ^.sort := Map("field" -> "weight", "order" -> "ASC")
                    )(
                      <.Datagrid()(
                        <.FlatButton(
                          ^.label := "edit partner",
                          ^.containerElement := <.EditPartnerComponent(
                            ^.wrapped := EditPartnerComponentProps(reloadComponent)
                          )()
                        )(),
                        <.TextField(^.source := "name")(),
                        <.TextField(^.source := "link")(),
                        <.TextField(^.source := "partnerKind", ^.label := "kind")(),
                        <.TextField(^.source := "weight")(),
                        <.FlatButton(
                          ^.label := "delete partner",
                          ^.containerElement := <.DeletePartnerComponent(
                            ^.wrapped := DeletePartnerComponentProps(reloadComponent)
                          )()
                        )()
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

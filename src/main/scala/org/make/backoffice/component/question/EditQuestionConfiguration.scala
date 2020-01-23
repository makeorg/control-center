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
import org.make.backoffice.component.question.ActiveFeatureComponent.ActiveFeatureProps
import org.make.backoffice.component.question.CreatePartnerComponent.CreatePartnerComponentProps
import org.make.backoffice.component.question.CreatePersonalityComponent.CreatePersonalityComponentProps
import org.make.backoffice.component.question.DeletePartnerComponent.DeletePartnerComponentProps
import org.make.backoffice.component.question.DeletePersonalityComponent.DeletePersonalityComponentProps
import org.make.backoffice.component.question.EditPartnerComponent.EditPartnerComponentProps
import org.make.backoffice.component.question.EditPersonalityComponent.EditPersonalityComponentProps
import org.make.backoffice.component.question.InitialProposalComponent.InitialProposalComponentProps
import org.make.backoffice.facade.AdminOnRest.Datagrid._
import org.make.backoffice.facade.AdminOnRest.Edit._
import org.make.backoffice.facade.AdminOnRest.EditButton._
import org.make.backoffice.facade.AdminOnRest.Fields._
import org.make.backoffice.facade.AdminOnRest.FormTab._
import org.make.backoffice.facade.AdminOnRest.Inputs._
import org.make.backoffice.facade.AdminOnRest.TabbedForm._
import org.make.backoffice.facade.MaterialUi._
import org.make.backoffice.model.{ActiveFeature, Organisation}
import org.make.backoffice.service.feature.ActiveFeatureService
import org.make.backoffice.service.organisation.OrganisationService
import org.make.backoffice.service.proposal.{Accepted, Refused}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object EditQuestionConfiguration {

  case class EditQuestionConfigurationProps() extends RouterProps
  case class EditQuestionConfigurationState(reload: Boolean,
                                            activeFeaturesList: Seq[ActiveFeature],
                                            organisationSearchList: Seq[Organisation])

  def apply(): ReactClass = reactClass

  private lazy val reactClass: ReactClass =
    React
      .createClass[EditQuestionConfigurationProps, EditQuestionConfigurationState](
        displayName = "EditQuestionConfiguration",
        getInitialState = _ =>
          EditQuestionConfigurationState(
            reload = false,
            activeFeaturesList = Seq.empty,
            organisationSearchList = Seq.empty
        ),
        componentDidMount = { self =>
          ActiveFeatureService.listActiveFeatures.onComplete {
            case Success(activeFeatures) => self.setState(_.copy(activeFeaturesList = activeFeatures))
            case Failure(_)              =>
          }

          val nullOrganisation = Organisation(id = None, maybeOrganisationName = None, profile = None)

          OrganisationService.organisations(None, Some(500)).onComplete {
            case Success(organisations) =>
              self.setState(
                _.copy(
                  organisationSearchList = organisations.filterNot(_.organisationName.isEmpty).+:(nullOrganisation)
                )
              )
            case Failure(_) =>
          }
        },
        componentDidUpdate = { (self, _, state) =>
          if (state.reload) {
            ActiveFeatureService.listActiveFeatures.onComplete {
              case Success(activeFeatures) => self.setState(_.copy(activeFeaturesList = activeFeatures))
              case Failure(_)              =>
            }
          }
        },
        render = self => {

          def reloadComponent: () => Unit = { () =>
            self.setState(_.copy(reload = true))
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
                    <.CreatePartnerComponent(
                      ^.wrapped := CreatePartnerComponentProps(reloadComponent, self.state.organisationSearchList)
                    )(),
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
                            ^.wrapped := EditPartnerComponentProps(reloadComponent, self.state.organisationSearchList)
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
              }),
              <.FormTab(^.label := "Personalities")(if (!self.state.reload) {
                js.Array(
                    <.CreatePersonalityComponent(^.wrapped := CreatePersonalityComponentProps(reloadComponent))(),
                    <.ReferenceManyField(
                      ^.reference := Resource.questionPersonalities,
                      ^.target := "questionId",
                      ^.addLabel := false,
                      ^.perPage := 50
                    )(
                      <.Datagrid()(
                        <.FlatButton(
                          ^.label := "edit personality",
                          ^.containerElement := <.EditPersonalityComponent(
                            ^.wrapped := EditPersonalityComponentProps(reloadComponent)
                          )()
                        )(),
                        <.ReferenceField(
                          ^.source := "userId",
                          ^.label := "User firstname",
                          ^.reference := Resource.users,
                          ^.linkType := false,
                          ^.sortable := false
                        )(<.TextField(^.source := "firstName")()),
                        <.ReferenceField(
                          ^.source := "userId",
                          ^.label := "User lastname",
                          ^.reference := Resource.users,
                          ^.linkType := false,
                          ^.sortable := false
                        )(<.TextField(^.source := "lastName")()),
                        <.TextField(^.source := "personalityRole", ^.label := "role")(),
                        <.FlatButton(
                          ^.label := "delete personality",
                          ^.containerElement := <.DeletePersonalityComponent(
                            ^.wrapped := DeletePersonalityComponentProps(reloadComponent)
                          )()
                        )()
                      )
                    )
                  )
                  .toSeq
              } else {
                self.setState(_.copy(reload = false))
              }),
              <.FormTab(^.label := "Active Features")(
                <.ReferenceManyField(
                  ^.reference := Resource.features,
                  ^.target := "",
                  ^.addLabel := false,
                  ^.perPage := 50
                )(
                  <.Datagrid()(
                    <.ActiveFeature(
                      ^.label := "State",
                      ^.wrapped := ActiveFeatureProps(
                        activeFeaturesList = self.state.activeFeaturesList,
                        questionId = self.props.`match`.params.getOrElse("id", "").toString,
                        reloadComponent = reloadComponent
                      )
                    )(),
                    <.TextField(^.source := "name", ^.sortable := true)(),
                    <.TextField(^.source := "slug", ^.sortable := true)()
                  )
                )
              )
            )
          )
        }
      )

}

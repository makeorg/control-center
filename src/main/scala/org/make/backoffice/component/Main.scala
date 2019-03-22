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

package org.make.backoffice.component

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.Router._
import org.make.backoffice.client.{AuthClient, Resource, RestClient}
import org.make.backoffice.component.idea.{CreateIdea, EditIdea, IdeaList}
import org.make.backoffice.component.ideaMappings.{EditIdeaMapping, IdeaMappingsList}
import org.make.backoffice.component.moderator.{CreateModerator, EditModerator, ModeratorList}
import org.make.backoffice.component.operation.{CreateOperation, EditOperation, OperationsList}
import org.make.backoffice.component.organisation.{CreateOrganisation, EditOrganisation, OrganisationList}
import org.make.backoffice.component.proposal.common.ShowProposal
import org.make.backoffice.component.proposal.moderation.{NextProposal, ProposalList}
import org.make.backoffice.component.proposal.toEnrich.ToEnrichProposalList
import org.make.backoffice.component.proposal.validated.ValidatedProposalList
import org.make.backoffice.component.question.{CreateQuestion, EditQuestion, QuestionsList}
import org.make.backoffice.component.tag.{CreateTag, EditTag, TagList}
import org.make.backoffice.facade.AdminOnRest.Admin._
import org.make.backoffice.facade.AdminOnRest.Resource._
import org.scalajs.dom

import scala.scalajs.js

object Main {
  val defaultErrorMessage = "Oooops, something went wrong"

  def main(args: Array[String]): Unit = {

    ReactDOM.render(
      <.Admin(
        ^.title := "Backoffice",
        ^.menu := CustomMenu.reactClass,
        ^.loginPage := LoginPage(org.scalajs.dom.window.location.hash),
        ^.customRoutes := js.Array(
          <.Route(^.path := "/nextProposal/:id", ^.exact := true, ^.component := NextProposal.reactClass)()
        ),
        ^.dashboard := Dashboard(),
        ^.restClient := RestClient.makeClient,
        ^.authClient := AuthClient.auth
      )(
        <.Resource(^.name := Resource.proposals, ^.listing := ProposalList(), ^.show := ShowProposal())(),
        <.Resource(
          ^.name := Resource.toEnrichProposals,
          ^.listing := ToEnrichProposalList(),
          ^.show := ShowProposal()
        )(),
        <.Resource(
          ^.name := Resource.validatedProposals,
          ^.listing := ValidatedProposalList.ProposalListContainer,
          ^.show := ShowProposal()
        )(),
        <.Resource(
          ^.name := Resource.operations,
          ^.listing := OperationsList(),
          ^.create := CreateOperation(),
          ^.edit := EditOperation()
        )(),
        <.Resource(
          ^.name := Resource.operationsOfQuestions,
          ^.listing := QuestionsList(),
          ^.create := CreateQuestion(),
          ^.edit := EditQuestion()
        )(),
        <.Resource(^.name := Resource.ideas, ^.listing := IdeaList(), ^.create := CreateIdea(), ^.edit := EditIdea())(),
        <.Resource(^.name := Resource.ideaMappings, ^.listing := IdeaMappingsList(), ^.edit := EditIdeaMapping())(),
        <.Resource(^.name := Resource.tags, ^.listing := TagList(), ^.create := CreateTag(), ^.edit := EditTag())(),
        <.Resource(^.name := Resource.tagType)(),
        <.Resource(
          ^.name := Resource.organisations,
          ^.listing := OrganisationList(),
          ^.edit := EditOrganisation(),
          ^.create := CreateOrganisation()
        )(),
        <.Resource(
          ^.name := Resource.moderators,
          ^.listing := ModeratorList(),
          ^.edit := EditModerator(),
          ^.create := CreateModerator()
        )(),
        <.Resource(^.name := Resource.questions)()
      ),
      dom.document.getElementById("make-backoffice")
    )
  }
}

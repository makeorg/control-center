package org.make.backoffice.component

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.router.Router._
import org.make.backoffice.client.{AuthClient, Resource, RestClient}
import org.make.backoffice.component.idea.{CreateIdea, EditIdea, IdeaList}
import org.make.backoffice.component.proposal.common.ShowProposal
import org.make.backoffice.component.proposal.moderation.{NextProposal, ProposalList}
import org.make.backoffice.component.proposal.validated.ValidatedProposalList
import org.make.backoffice.component.tag.{CreateTag, ShowTag, TagList}
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
        ^.loginPage := LoginPage(),
        ^.customRoutes := js.Array(
          <.Route(^.path := "/nextProposal/:id", ^.exact := true, ^.component := NextProposal.reactClass)()
        ),
        ^.dashboard := Dashboard(),
        ^.restClient := RestClient.makeClient,
        ^.authClient := AuthClient.auth
      )(
        <.Resource(^.name := Resource.proposals, ^.listing := ProposalList(), ^.show := ShowProposal())(),
        <.Resource(
          ^.name := Resource.validatedProposals,
          ^.listing := ValidatedProposalList(),
          ^.show := ShowProposal()
        )(),
        <.Resource(^.name := Resource.ideas, ^.listing := IdeaList(), ^.create := CreateIdea(), ^.edit := EditIdea())(),
        <.Resource(^.name := Resource.operations)(),
        <.Resource(^.name := Resource.tags, ^.listing := TagList(), ^.show := ShowTag(), ^.create := CreateTag())()
      ),
      dom.document.getElementById("make-backoffice")
    )
  }
}

package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.make.backoffice.components.aor.idea.IdeaList
import org.make.backoffice.components.aor.idea.CreateIdea
import org.make.backoffice.components.aor.idea.EditIdea
import org.make.backoffice.components.aor.proposal.{CreateProposal, DeleteProposal, ProposalList, ShowProposal}
import org.make.backoffice.components.aor.validatedProposals.ValidatedProposalList
import org.make.backoffice.components.{Dashboard, LoginPage}
import org.make.backoffice.facades.AdminOnRest.Admin._
import org.make.backoffice.facades.AdminOnRest.Resource._
import org.make.client.{AuthClient, Resource, RestClient}
import org.scalajs.dom

object Main {
  def main(args: Array[String]): Unit = {

    ReactDOM.render(
      <.Admin(
        ^.title := "Backoffice",
        ^.loginPage := LoginPage(),
        ^.dashboard := Dashboard(),
        ^.restClient := RestClient.makeClient,
        ^.authClient := AuthClient.auth
      )(
        <.Resource(
          ^.name := Resource.proposals,
          ^.listing := ProposalList(),
          ^.create := CreateProposal(),
          ^.show := ShowProposal(),
          ^.remove := DeleteProposal()
        )(),
        <.Resource(
          ^.name := Resource.validatedProposals,
          ^.listing := ValidatedProposalList(),
          ^.show := ShowProposal()
        )(),
        <.Resource(^.name := Resource.ideas, ^.listing := IdeaList(), ^.create := CreateIdea(), ^.edit := EditIdea())(),
        <.Resource(^.name := Resource.operations)(),
        <.Resource(^.name := Resource.tags)()
      ),
      dom.document.getElementById("make-backoffice")
    )
  }
}

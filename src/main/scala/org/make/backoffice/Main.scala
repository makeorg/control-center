package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.make.backoffice.components.proposal._
import org.make.backoffice.components.validated_proposals.ValidatedProposalList
import org.make.backoffice.components.{Dashboard, LoginPage}
import org.make.backoffice.facades.AdminOnRest.Admin._
import org.make.backoffice.facades.AdminOnRest.Resource._
import org.make.client.{Resource, RestClient}
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  override def main(): Unit = {
    val wrapperNode = dom.document.getElementById("make-backoffice")

    ReactDOM.render(
      <.Admin(
        ^.title := "Backoffice",
        ^.loginPage := LoginPage(),
        ^.dashboard := Dashboard(),
        ^.restClient := RestClient.makeClient
      )(
        <.Resource(
          ^.name := Resource.proposals,
          ^.showList := ProposalList(),
          ^.create := CreateProposal(),
          ^.edit := EditProposal(),
          ^.show := ShowProposal(),
          ^.remove := DeleteProposal()
        )(),
        <.Resource(^.name := Resource.validatedProposals, ^.showList := ValidatedProposalList())()
      ),
      wrapperNode
    )
  }
}

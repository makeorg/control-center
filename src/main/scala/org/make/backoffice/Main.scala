package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.make.backoffice.components.validated_proposals.ValidatedProposalList
import org.make.backoffice.components.proposal._
import org.make.backoffice.components.{CustomRoutes, Dashboard}
import org.make.backoffice.facades.Admin._
import org.make.backoffice.facades.Resource._
import org.make.client.{Resource, RestClient}
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  override def main(): Unit = {
    val wrapperNode = dom.document.getElementById("make-backoffice")

    ReactDOM.render(
      <.Admin(
        ^.title := "Backoffice",
        ^.dashboard := Dashboard(),
        ^.customRoutes := CustomRoutes.customRoutes,
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

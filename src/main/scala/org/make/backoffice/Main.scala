package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.make.backoffice.components.proposal.{CreateProposal, ProposalList, EditProposal, ShowProposal, DeleteProposal}
import org.make.backoffice.components.{CustomRoutes, Menu}
import org.make.backoffice.libs.Admin._
import org.make.backoffice.libs.JsonServerRestClient._
import org.make.backoffice.libs.Resource._
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {
  override def main(): Unit = {
    val wrapperNode = dom.document.getElementById("wrapper")

    ReactDOM.render(
      <.Admin(
        ^.title := "Backoffice",
        ^.menu := Menu(),
        ^.customRoutes := CustomRoutes.customRoutes,
        ^.restClient := jsonServerRestClient("http://localhost:3000")
      )(
        <.Resource(
          ^.name := "propositions",
          ^.showList := ProposalList(),
          ^.create := CreateProposal(),
          ^.edit := EditProposal(),
          ^.show := ShowProposal(),
          ^.remove := DeleteProposal()
        )()
      ),
      wrapperNode
    )
  }
}

package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.make.backoffice.components.proposal._
import org.make.backoffice.components.{CustomRoutes, Dashboard}
import org.make.backoffice.libs.Admin._
import org.make.backoffice.libs.JsonServerRestClient._
import org.make.backoffice.libs.Resource._
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

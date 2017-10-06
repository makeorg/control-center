package org.make.backoffice

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.redux.{Action, Redux}
import org.make.backoffice.components.aor.proposal.{CreateProposal, DeleteProposal, ProposalList, ShowProposal}
import org.make.backoffice.components.aor.validatedProposals.ValidatedProposalList
import org.make.backoffice.components.{Dashboard, LoginPage}
import org.make.backoffice.facades.AdminOnRest.Admin._
import org.make.backoffice.facades.AdminOnRest.Resource._
import org.make.client.{AuthClient, Resource, RestClient}
import org.scalajs.dom

import scala.scalajs.js.JSApp

case class State(text: String)
case class SetText(text: String) extends Action
object Reducer {
  def apply(maybeState: Option[State], action: Any): State =
    action match {
      case action: SetText => State(text = action.text)
      case _               => State(text = "")
    }
}

object Main extends JSApp {
  override def main(): Unit = {

    val store = Redux.createStore[State](Reducer(_, _))

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
        )()
      ),
      dom.document.getElementById("make-backoffice")
    )
  }
}

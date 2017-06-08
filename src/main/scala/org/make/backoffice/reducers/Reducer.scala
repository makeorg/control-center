package org.make.backoffice.reducers

import java.util.UUID

import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterRedux
import org.make.backoffice.actions._
import org.make.backoffice.models.{GlobalState, Proposition, User}
import org.make.backoffice.services.PropositionsServiceComponent

object Reducer extends PropositionsServiceComponent {
  def reduce(maybeState: Option[GlobalState], action: Any): GlobalState =
    GlobalState(
      user = reduceUser(maybeState.map(_.user), action),
      listPropositions = reduceListPropositions(maybeState.map(_.listPropositions), action),
      displayedProposition = reduceDisplayedProposition(maybeState.map(_.displayedProposition), action),
      router = ReactRouterRedux.routerReducer(maybeState.map(_.router), action)
    )

  def reduceUser(maybeState: Option[User], action: Any): User = {
    val user = maybeState.getOrElse(User(UUID.randomUUID, false, "token"))
    action match {
      case Connect => user.copy(isAuthenticated = true)
      case Disconnect => user.copy(isAuthenticated = false)
      case _ => user
    }
  }

  def reduceListPropositions(maybeState: Option[Seq[Proposition]], action: Any): Seq[Proposition] = {
    val list = Seq[Proposition](Proposition(UUID.randomUUID, "content"))
    action match {
      case GetListProposition => propositionsList
      case _ => list
    }
  }

  def reduceDisplayedProposition(maybeState: Option[Proposition], action: Any): Proposition = {
    val proposition = Proposition(UUID.randomUUID, "content")
    action match {
      case action: SearchProposition => propositionsList.find(p => p.propositionId == action.propositionId).getOrElse(proposition)
      case _ => proposition
    }
  }
}

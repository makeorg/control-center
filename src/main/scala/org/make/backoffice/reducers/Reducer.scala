package org.make.backoffice.reducers

import java.util.UUID

import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterRedux
import org.make.backoffice.actions._
import org.make.backoffice.models.{GlobalState, Proposition, User}

object Reducer {
  def reduce(maybeState: Option[GlobalState], action: Any): GlobalState =
    GlobalState(
      user = reduceUser(maybeState.map(_.user), action),
      listPropositions = reduceListPropositions(maybeState.map(_.listPropositions), action),
      displayedProposition = reduceDisplayedProposition(maybeState.map(_.displayedProposition), action),
      router = ReactRouterRedux.routerReducer(maybeState.map(_.router), action)
    )

  def reduceUser(maybeState: Option[User], action: Any): User = {
    val user = User(UUID.randomUUID, false, "token")
    action match {
      case action: Connect => ???
      case _ => user
    }
  }

  def reduceListPropositions(maybeState: Option[List[Proposition]], action: Any): List[Proposition] = {
    val list = List[Proposition](Proposition(UUID.randomUUID, "content"))
    action match {
      case action: GetListProposition => ???
      case _ => list
    }
  }

  def reduceDisplayedProposition(maybeState: Option[Proposition], action: Any): Proposition = {
    val proposition = Proposition(UUID.randomUUID, "content")
    action match {
      case action: SearchProposition => ???
      case _ => proposition
    }
  }
}

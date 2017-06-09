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
    val user = maybeState.getOrElse(User(
      id = UUID.randomUUID,
      isAuthenticated = false,
      token = "token"
    ))
    action match {
      case Connect => user.copy(isAuthenticated = true)
      case Disconnect => user.copy(isAuthenticated = false)
      case _ => user
    }
  }

  def reduceListPropositions(maybeState: Option[Seq[Proposition]], action: Any): Seq[Proposition] = {
    val list = maybeState.getOrElse(Seq[Proposition]())
    action match {
      case DisplayListPropositions(propositions) => propositions
      case DisplayProposition(Some(proposition)) => Seq[Proposition](proposition)
      case _ => list
    }
  }

  def reduceDisplayedProposition(maybeState: Option[Proposition], action: Any): Proposition = {
    val defaultProposition = Proposition(UUID.randomUUID, "default content")
    action match {
      case _ => defaultProposition
    }
  }
}

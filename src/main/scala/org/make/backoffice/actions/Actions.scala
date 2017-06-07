package org.make.backoffice.actions

import java.util.UUID

import io.github.shogowada.scalajs.reactjs.redux.Action

case class Connect(id: UUID, credentials: String, token: String) extends Action

case class GetListProposition() extends Action

case class SearchProposition(propositionId: UUID) extends Action

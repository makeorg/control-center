package org.make.backoffice.actions

import java.util.UUID

import io.github.shogowada.scalajs.reactjs.redux.Action

case object Connect extends Action
case object Disconnect extends Action

case object GetListProposition extends Action

case class SearchProposition(propositionId: UUID) extends Action

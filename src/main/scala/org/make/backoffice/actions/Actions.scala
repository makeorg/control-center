package org.make.backoffice.actions

import java.util.UUID

import io.github.shogowada.scalajs.reactjs.redux.Action
import org.make.backoffice.models.Proposition

case object Connect extends Action
case object Disconnect extends Action

case class DisplayListPropositions(propositions: Seq[Proposition]) extends Action
case class DisplayProposition(maybeProposition: Option[Proposition]) extends Action

case class SearchProposition(propositionId: UUID) extends Action

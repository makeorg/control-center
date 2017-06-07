package org.make.backoffice.models

import java.util.UUID

import scala.annotation.meta.field
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

case class GlobalState(
                        user: User,
                        listPropositions: List[Proposition],
                        displayedProposition: Proposition,
                        @(JSExport@field) router: js.Object
                      )

case class User(
                 id: UUID,
                 isAuthenticated: Boolean,
                 token: String
               )

case class Proposition(
                        propositionId: UUID,
                        content: String
                      )
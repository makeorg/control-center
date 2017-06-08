package org.make.backoffice.services

import java.util.UUID

import org.make.backoffice.models.Proposition

import scala.concurrent.Future

trait PropositionsServiceComponent {

  //def propositionService: PropositionService

  val propositionsList: Seq[Proposition] = Seq[Proposition](
    Proposition(UUID.fromString("123e4567-e89b-12d3-a456-111111111111"), "Il faut faire une proposition"),
    Proposition(UUID.fromString("123e4567-e89b-12d3-a456-222222222222"), "Il faut faire deux propositions"),
    Proposition(UUID.fromString("123e4567-e89b-12d3-a456-333333333333"), "Il faut faire trois propositions"),
    Proposition(UUID.fromString("123e4567-e89b-12d3-a456-444444444444"), "Il faut faire quatre propositions")
  )

  class PropositionService {
    def getProposition(propositionId: UUID): Future[Option[Proposition]] =
      Future.successful(
        propositionsList.find(_.propositionId == propositionId)
      )

    def listPropositions: Future[Seq[Proposition]] =
      Future.successful(propositionsList)
  }
}

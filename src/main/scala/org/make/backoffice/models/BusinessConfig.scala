package org.make.backoffice.models

import scala.scalajs.js
import js.JSConverters._

@js.native
trait BusinessConfig extends js.Object {
  val proposalMinLength: Int
  val proposalMaxLength: Int
  val themes: js.Array[Theme]
  val nVotesTriggerConnexion: Int
  val nPendingProposalsTriggerEmailModerator: Int
  val minProposalsPerSequence: Int
  val maxProposalsPerSequence: Int
  val reasonsForRefusal: js.Array[String]
}

object BusinessConfig {
  def apply(proposalMinLength: Int,
            proposalMaxLength: Int,
            themes: Seq[Theme],
            nVotesTriggerConnexion: Int,
            nPendingProposalsTriggerEmailModerator: Int,
            minProposalsPerSequence: Int,
            maxProposalsPerSequence: Int,
            reasonsForRefusal: Seq[String]): BusinessConfig =
    js.Dynamic
      .literal(
        proposalMinLength = proposalMinLength,
        proposalMaxLength = proposalMaxLength,
        themes = themes.toJSArray,
        nVotesTriggerConnexion = nVotesTriggerConnexion,
        nPendingProposalsTriggerEmailModerator = nPendingProposalsTriggerEmailModerator,
        minProposalsPerSequence = minProposalsPerSequence,
        maxProposalsPerSequence = maxProposalsPerSequence,
        reasonsForRefusal = reasonsForRefusal.toJSArray
      )
      .asInstanceOf[BusinessConfig]
}

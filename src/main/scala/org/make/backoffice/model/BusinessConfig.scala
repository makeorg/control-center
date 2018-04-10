package org.make.backoffice.model

import scala.scalajs.js
import js.JSConverters._
import scala.scalajs.js.UndefOr

@js.native
trait CountryConfiguration extends js.Object {
  val countryCode: String
  val defaultLanguage: String
  val supportedLanguages: js.Array[String]
  val startDate: UndefOr[js.Date]
  val endDate: UndefOr[js.Date]
}

object CountryConfiguration {
  def apply(countryCode: String,
            defaultLanguage: String,
            supportedLanguages: Seq[String],
            startDate: Option[String],
            endDate: Option[String]): CountryConfiguration =
    js.Dynamic
      .literal(
        countryCode = countryCode,
        defaultLanguage = defaultLanguage,
        supportedLanguages = supportedLanguages.toJSArray,
        startDate = startDate.map(d => new js.Date(d)).orUndefined,
        endDate = endDate.map(d     => new js.Date(d)).orUndefined
      )
      .asInstanceOf[CountryConfiguration]
}

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
  val supportedCountries: js.Array[CountryConfiguration]
}

object BusinessConfig {
  def apply(proposalMinLength: Int,
            proposalMaxLength: Int,
            themes: Seq[Theme],
            nVotesTriggerConnexion: Int,
            nPendingProposalsTriggerEmailModerator: Int,
            minProposalsPerSequence: Int,
            maxProposalsPerSequence: Int,
            reasonsForRefusal: Seq[String],
            supportedCountries: Seq[CountryConfiguration]): BusinessConfig =
    js.Dynamic
      .literal(
        proposalMinLength = proposalMinLength,
        proposalMaxLength = proposalMaxLength,
        themes = themes.toJSArray,
        nVotesTriggerConnexion = nVotesTriggerConnexion,
        nPendingProposalsTriggerEmailModerator = nPendingProposalsTriggerEmailModerator,
        minProposalsPerSequence = minProposalsPerSequence,
        maxProposalsPerSequence = maxProposalsPerSequence,
        reasonsForRefusal = reasonsForRefusal.toJSArray,
        supportedCountries = supportedCountries.toJSArray
      )
      .asInstanceOf[BusinessConfig]
}

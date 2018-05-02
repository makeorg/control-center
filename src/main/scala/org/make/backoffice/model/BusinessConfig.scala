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
  val proposalMaxLength: Int
  val themes: js.Array[Theme]
  val reasonsForRefusal: js.Array[String]
  val supportedCountries: js.Array[CountryConfiguration]
}

object BusinessConfig {
  def apply(proposalMaxLength: Int,
            themes: Seq[Theme],
            reasonsForRefusal: Seq[String],
            supportedCountries: Seq[CountryConfiguration]): BusinessConfig =
    js.Dynamic
      .literal(
        proposalMaxLength = proposalMaxLength,
        themes = themes.toJSArray,
        reasonsForRefusal = reasonsForRefusal.toJSArray,
        supportedCountries = supportedCountries.toJSArray
      )
      .asInstanceOf[BusinessConfig]
}

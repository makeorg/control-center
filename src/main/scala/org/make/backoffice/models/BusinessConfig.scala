package org.make.backoffice.models

import scala.scalajs.js
import js.JSConverters._

@js.native
trait CountryConfiguration extends js.Object {
  val countryCode: String
  val defaultLanguage: String
  val supportedLanguages: js.Array[String]
}

object CountryConfiguration {
  def apply(countryCode: String, defaultLanguage: String, supportedLanguages: Seq[String]): CountryConfiguration =
    js.Dynamic
      .literal(
        countryCode = countryCode,
        defaultLanguage = defaultLanguage,
        supportedLanguages = supportedLanguages.toJSArray
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

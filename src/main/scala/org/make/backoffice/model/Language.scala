package org.make.backoffice.model

object Language {
  private val mapping = Map("fr" -> "French", "it" -> "Italian", "en" -> "English")

  def getLanguageNameFromLanguageCode(languageCode: String): Option[String] = {
    mapping.get(languageCode)
  }
}

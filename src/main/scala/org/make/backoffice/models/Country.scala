package org.make.backoffice.models

object Country {
  private val mapping = Map("FR" -> "France", "IT" -> "Italy", "GB" -> "United Kingdom")
  def getCountryNameByCountryCode(countryCode: String): Option[String] = {
    mapping.get(countryCode)
  }
}

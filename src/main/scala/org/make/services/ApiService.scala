package org.make.services

import io.circe.Printer
import org.make.client.DefaultMakeApiHttpClientComponent
import org.make.client.DefaultMakeApiHttpClientComponent.DefaultMakeApiHttpClient
import org.make.client.request.Filter

trait ApiService {
  val resourceName: String
  lazy val client: DefaultMakeApiHttpClient = DefaultMakeApiHttpClientComponent.client
}

object ApiService {
  val printer: Printer = Printer.noSpaces

  def getFieldValueFromFilters(field: String, maybeFilters: Option[Seq[Filter]]): Option[String] = {
    maybeFilters.flatMap(_.find(_.field == field).map(_.value.toString))
  }

}

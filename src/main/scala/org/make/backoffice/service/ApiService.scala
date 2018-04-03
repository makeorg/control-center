package org.make.backoffice.service

import io.circe.Printer
import org.make.backoffice.client.DefaultMakeApiHttpClientComponent
import org.make.backoffice.client.DefaultMakeApiHttpClientComponent.DefaultMakeApiHttpClient
import org.make.backoffice.client.request.Filter

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

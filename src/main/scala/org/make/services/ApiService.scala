package org.make.services

import io.circe.Printer
import org.make.client.DefaultMakeApiHttpClientComponent
import org.make.client.DefaultMakeApiHttpClientComponent.DefaultMakeApiHttpClient

trait ApiService {
  val resourceName: String
  lazy val client: DefaultMakeApiHttpClient = DefaultMakeApiHttpClientComponent.client
}

object ApiService {
  val printer: Printer = Printer.noSpaces
}

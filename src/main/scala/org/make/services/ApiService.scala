package org.make.services

import io.circe.Printer

trait ApiService {
  val resourceName: String
}

object ApiService {
  val printer: Printer = Printer.noSpaces
}

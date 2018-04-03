package org.make.backoffice.service.technical

import org.make.backoffice.util.CirceClassFormatters
import org.make.backoffice.model.BusinessConfig
import org.make.backoffice.util.uri._
import org.make.backoffice.service.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.Dynamic.{global => g}

trait ConfigurationsServiceComponent extends CirceClassFormatters {
  def apiBaseUrl: String
  def configurationService: ConfigurationService = new ConfigurationService

  class ConfigurationService extends ApiService with CirceClassFormatters {
    override val resourceName: String = "configurations"

    def getConfigurations: Future[BusinessConfig] =
      client.get[BusinessConfig](resourceName / "backoffice").recover {
        case e =>
          g.console.log(s"exception $e")
          throw e
      }
  }
}

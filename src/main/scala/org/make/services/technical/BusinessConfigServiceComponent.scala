package org.make.services.technical

import org.make.backoffice.models.BusinessConfig
import org.make.core.CirceClassFormatters
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import scala.scalajs.js.Dynamic.{global => g}

trait BusinessConfigServiceComponent extends CirceClassFormatters {
  def apiBaseUrl: String
  def businessConfigService: BusinessConfigService = new BusinessConfigService

  class BusinessConfigService extends ApiService with CirceClassFormatters {
    override val resourceName: String = "Business Config"

    def getBusinessConfig: Future[BusinessConfig] =
      client.get[BusinessConfig]("business_config_back").map(_.get).recover {
        case e =>
          g.console.log(s"exception $e")
          throw e
      }
  }
}

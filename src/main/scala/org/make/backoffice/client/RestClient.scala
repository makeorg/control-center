package org.make.backoffice.client

import org.make.backoffice.facade.{Configuration, FetchJson, JsonServerRestClient}
import org.make.backoffice.client.request.Request
import org.make.backoffice.util.CirceClassFormatters

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise

object RestClient extends CirceClassFormatters {
  def makeClient(restVerb: String, resource: String, parameters: js.Object): Promise[Response] = {

    def fetchJsonCookie(url: String, options: js.UndefOr[js.Dictionary[String]]): Promise[Response] = {
      val defined: js.Dictionary[String] = options.getOrElse(js.Dictionary())
      defined.update("credentials", "include")
      FetchJson.fetchJson(url, defined)
    }

    val jsonClient = JsonServerRestClient.jsonServerRestClient(Configuration.apiUrl + "/moderation", fetchJsonCookie)

    resource match {
      case Resource.tags                => jsonClient(restVerb, resource, parameters)
      case res if Resource.amongst(res) => Request.fetch(restVerb, resource, parameters).toJSPromise
      case unknownResource =>
        Future.failed(new ClassNotFoundException(s"Unknown resource: $unknownResource")).toJSPromise
    }
  }
}

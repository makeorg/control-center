package org.make.client

import org.make.backoffice.facades.{Configuration, FetchJson, JsonServerRestClient}
import org.make.client.request.Request
import org.make.core.CirceClassFormatters

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise

object RestClient extends CirceClassFormatters {
  def makeClient(restVerb: String, resource: String, parameters: js.Object): Promise[Response] = {

    def fetchJsonCookie(url: String, options: js.Dictionary[String]): Promise[Response] = {
      options("credentials") = "include"
      FetchJson.fetchJson(url, options)
    }

    val jsonClient = JsonServerRestClient.jsonServerRestClient(Configuration.apiUrl + "/moderation", fetchJsonCookie)

    resource match {
      case "tags" => jsonClient(restVerb, resource, parameters)
      case res if Resource.amongst(res) => Request.fetch(restVerb, resource, parameters).toJSPromise
      case unknownResource =>
        Future.failed(new ClassNotFoundException(s"Unknown resource: $unknownResource")).toJSPromise
    }
  }
}

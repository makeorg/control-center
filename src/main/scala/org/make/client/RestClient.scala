package org.make.client

import org.make.core.CirceClassFormatters

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise

object RestClient extends CirceClassFormatters {
  def makeClient(restVerb: String, resource: String, parameters: js.Object): Promise[Response] = {
    resource match {
      case "proposals" | "users" => Request.fetch(restVerb, resource, parameters).toJSPromise
      case unknownResource =>
        Future.failed(new ClassNotFoundException(s"Unknown resource: $unknownResource")).toJSPromise
    }
  }
}

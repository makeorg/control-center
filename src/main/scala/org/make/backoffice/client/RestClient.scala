/*
 * Make.org Control Center
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package org.make.backoffice.client

import org.make.backoffice.facade.{Configuration, FetchJson, JsonServerRestClient}
import org.make.backoffice.client.request.Request
import org.make.backoffice.util.CirceClassFormatters

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Promise
import org.scalajs.dom
import org.scalajs.dom.experimental.Headers

object RestClient extends CirceClassFormatters {
  def makeClient(restVerb: String, resource: String, parameters: js.Object): Promise[Response] = {

    def fetchJsonCookie(url: String, options: js.UndefOr[js.Dictionary[Any]]): Promise[Response] = {
      val defined: js.Dictionary[Any] = options.getOrElse(js.Dictionary())
      defined.update(
        "headers",
        new Headers(js.Dictionary("x-make-app-name" -> "backoffice", "x-make-source" -> "core"))
      )
      defined.update("credentials", "include")
      defined.update(
        "user",
        js.Dictionary(
          "authenticated" -> true,
          "token" -> dom.window.localStorage.getItem(AuthClient.AUTHENTICATION_KEY)
        )
      )
      FetchJson.fetchJson(url, defined)
    }

    val jsonClient = JsonServerRestClient.jsonServerRestClient(Configuration.apiUrl + "/moderation", fetchJsonCookie)
    val adminJsonClient = JsonServerRestClient.jsonServerRestClient(Configuration.apiUrl + "/admin", fetchJsonCookie)

    resource match {
      case Resource.tags                  => jsonClient(restVerb, resource, parameters)
      case Resource.tagType               => jsonClient(restVerb, resource, parameters)
      case Resource.organisations         => jsonClient(restVerb, resource, parameters)
      case Resource.questions             => jsonClient(restVerb, "questions", parameters)
      case Resource.operations            => jsonClient(restVerb, resource, parameters)
      case Resource.operationsOfQuestions => jsonClient(restVerb, "operations-of-questions", parameters)
      case Resource.moderators            => adminJsonClient(restVerb, resource, parameters)
      case Resource.ideas                 => jsonClient(restVerb, resource, parameters)
      case Resource.ideaMappings          => adminJsonClient(restVerb, resource, parameters)
      case res if Resource.amongst(res)   => Request.fetch(restVerb, resource, parameters).toJSPromise
      case unknownResource =>
        Future.failed(new ClassNotFoundException(s"Unknown resource: $unknownResource")).toJSPromise
    }
  }
}

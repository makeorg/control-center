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
import org.scalajs.dom.experimental.Headers

object RestClient extends CirceClassFormatters {

  private def retryOnFailure(promise: => Promise[Response], retries: Int): Future[Response] = {
    promise.toFuture.recoverWith {
      case _ if retries > 0 => retryOnFailure(promise, retries - 1)
      case other            => Future.failed(other)
    }
  }

  def makeClient(restVerb: String, resource: String, parameters: js.Object): Promise[Response] = {

    def fetchJsonCookie(url: String, options: js.UndefOr[js.Dictionary[Any]]): Promise[Response] = {
      val defined: js.Dictionary[Any] = options.getOrElse(js.Dictionary())
      defined.update(
        "headers",
        new Headers(js.Dictionary("x-make-app-name" -> "backoffice", "x-make-source" -> "core"))
      )
      defined.update("credentials", "include")
      FetchJson.fetchJson(url, defined)
    }

    val jsonClient = JsonServerRestClient.jsonServerRestClient(Configuration.apiUrl + "/moderation", fetchJsonCookie)
    val adminJsonClient = JsonServerRestClient.jsonServerRestClient(Configuration.apiUrl + "/admin", fetchJsonCookie)

    resource match {
      case Resource.tags          => retryOnFailure(jsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.tagType       => retryOnFailure(jsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.organisations => retryOnFailure(jsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.questions     => retryOnFailure(jsonClient(restVerb, "questions", parameters), 5).toJSPromise
      case Resource.operations    => retryOnFailure(jsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.operationsOfQuestions =>
        retryOnFailure(jsonClient(restVerb, "operations-of-questions", parameters), 5).toJSPromise
      case Resource.moderators   => retryOnFailure(adminJsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.users        => retryOnFailure(adminJsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.ideas        => retryOnFailure(jsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.ideaMappings => retryOnFailure(adminJsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.partners     => retryOnFailure(adminJsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.questionsConfiguration =>
        retryOnFailure(jsonClient(restVerb, "operations-of-questions", parameters), 5).toJSPromise
      case Resource.features => retryOnFailure(adminJsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.crmTemplates =>
        retryOnFailure(adminJsonClient(restVerb, "crm/templates", parameters), 5).toJSPromise
      case Resource.questionPersonalities =>
        retryOnFailure(adminJsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.personalities =>
        retryOnFailure(adminJsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.topIdeas =>
        retryOnFailure(adminJsonClient(restVerb, resource, parameters), 5).toJSPromise
      case Resource.personalityRoles =>
        retryOnFailure(adminJsonClient(restVerb, resource, parameters), 5).toJSPromise
      case res if Resource.amongst(res) =>
        retryOnFailure(Request.fetch(restVerb, resource, parameters).toJSPromise, 5).toJSPromise
      case unknownResource =>
        Future.failed(new ClassNotFoundException(s"Unknown resource: $unknownResource")).toJSPromise
    }
  }
}

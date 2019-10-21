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

package org.make.backoffice.model

import java.time.ZonedDateTime

import org.make.backoffice.util.JSConverters._

import scala.scalajs.js
import scala.scalajs.js.{Date, UndefOr}
import scala.scalajs.js.JSConverters._

@js.native
trait RequestContext extends js.Object {
  val requestId: String
  val sessionId: String
  val externalId: String
  val country: js.UndefOr[String]
  val language: js.UndefOr[String]
  val operation: js.UndefOr[String]
  val source: js.UndefOr[String]
  val location: js.UndefOr[String]
  val question: js.UndefOr[String]
}

object RequestContext {
  def apply(requestId: String,
            sessionId: String,
            externalId: String,
            country: Option[String],
            language: Option[String],
            operation: Option[String],
            source: Option[String],
            location: Option[String],
            question: Option[String]): RequestContext =
    js.Dynamic
      .literal(
        requestId = requestId,
        sessionId = sessionId,
        externalId = externalId,
        country = country.orUndefined,
        language = language.orUndefined,
        operation = operation.orUndefined,
        source = source.orUndefined,
        location = location.orUndefined,
        question = question.orUndefined
      )
      .asInstanceOf[RequestContext]
}

@js.native
trait ProposalAction extends js.Object {
  val date: Date
  val user: js.UndefOr[User]
  val actionType: String
  val arguments: js.Dictionary[String]
}

object ProposalAction {
  def apply(date: ZonedDateTime,
            user: Option[User],
            actionType: String,
            arguments: Map[String, String]): ProposalAction =
    js.Dynamic
      .literal(
        date = date.toJSDate,
        user = user.orUndefined,
        actionType = actionType,
        arguments = arguments.toJSDictionary
      )
      .asInstanceOf[ProposalAction]
}

@js.native
trait SingleProposal extends js.Object {
  val id: String
  val slug: String
  val content: String
  val author: SingleProposalAuthor
  val status: String
  val refusalReason: js.UndefOr[String]
  val tagIds: js.Array[String]
  val votes: js.Array[Vote]
  val context: RequestContext
  val country: String
  val language: String
  val createdAt: js.UndefOr[Date]
  val updatedAt: js.UndefOr[Date]
  val events: js.UndefOr[js.Array[ProposalAction]]
  val similarProposals: js.UndefOr[js.Array[ProposalId]]
  val ideaId: js.UndefOr[String]
  val ideaProposals: js.Array[Proposal]
  val operationId: js.UndefOr[String]
  val questionId: js.UndefOr[String]
}

object SingleProposal {
  def apply(proposalId: ProposalId,
            slug: String,
            content: String,
            author: SingleProposalAuthor,
            status: String,
            refusalReason: Option[String],
            tags: Seq[String],
            votes: Seq[Vote],
            context: RequestContext,
            country: String,
            language: String,
            createdAt: Option[ZonedDateTime],
            updatedAt: Option[ZonedDateTime],
            events: Option[Seq[ProposalAction]],
            similarProposals: Option[Seq[ProposalId]],
            ideaId: Option[IdeaId],
            ideaProposals: Seq[Proposal],
            operationId: Option[OperationId],
            questionId: Option[QuestionId]): SingleProposal =
    js.Dynamic
      .literal(
        id = proposalId.value,
        slug = slug,
        content = content,
        author = author,
        status = status,
        refusalReason = refusalReason.orUndefined,
        tagIds = tags.toJSArray,
        votes = votes.toJSArray,
        context = context,
        country = country,
        language = language,
        createdAt = createdAt.map(_.toJSDate).orUndefined,
        updatedAt = updatedAt.map(_.toJSDate).orUndefined,
        events = events.map(_.toJSArray).orUndefined,
        similarProposals = similarProposals.map(_.toJSArray).orUndefined,
        ideaId = ideaId.map(_.value).orUndefined,
        ideaProposals = ideaProposals.toJSArray,
        operationId = operationId.map(_.value).orUndefined,
        questionId = questionId.map(_.value).orUndefined
      )
      .asInstanceOf[SingleProposal]
}

@js.native
trait SingleProposalAuthor extends js.Object {
  def userId: String
  def firstName: js.UndefOr[String]
  def lastName: js.UndefOr[String]
  def postalCode: js.UndefOr[String]
  def age: js.UndefOr[Int]
  def avatarUrl: js.UndefOr[String]
  def organisationName: js.UndefOr[String]
  def organisationSlug: js.UndefOr[String]
}

object SingleProposalAuthor {
  def apply(_userId: UserId,
            _firstName: Option[String],
            _lastName: Option[String],
            _postalCode: Option[String],
            _age: Option[Int],
            _avatarUrl: Option[String],
            _organisationName: Option[String],
            _organisationSlug: Option[String]): SingleProposalAuthor = {

    new js.Object {
      val userId: String = _userId.value
      val firstName: UndefOr[String] = _firstName.orUndefined
      val lastName: UndefOr[String] = _lastName.orUndefined
      val postalCode: UndefOr[String] = _postalCode.orUndefined
      val age: UndefOr[Int] = _age.orUndefined
      val avatarUrl: UndefOr[String] = _avatarUrl.orUndefined
      val organisationName: UndefOr[String] = _organisationName.orUndefined
      val organisationSlug: UndefOr[String] = _organisationSlug.orUndefined
    }.asInstanceOf[SingleProposalAuthor]
  }
}

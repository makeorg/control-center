package org.make.backoffice.models

import java.time.ZonedDateTime

import org.make.core.JSConverters._

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.scalajs.js.JSConverters._

@js.native
trait RequestContext extends js.Object {
  val currentTheme: js.UndefOr[ThemeId]
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
  def apply(currentTheme: Option[ThemeId],
            requestId: String,
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
        currentTheme = currentTheme.orUndefined,
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
  val author: User
  val labels: js.Array[String]
  val theme: js.UndefOr[ThemeId]
  val status: String
  val refusalReason: js.UndefOr[String]
  val tags: js.Array[TagId]
  val votes: js.Array[Vote]
  val context: RequestContext
  val createdAt: js.UndefOr[Date]
  val updatedAt: js.UndefOr[Date]
  val events: js.UndefOr[js.Array[ProposalAction]]
  val similarProposals: js.UndefOr[js.Array[ProposalId]]
  val idea: js.UndefOr[IdeaId]
  val ideaProposals: js.Array[Proposal]
}

object SingleProposal {
  def apply(proposalId: ProposalId,
            slug: String,
            content: String,
            author: User,
            labels: Seq[String],
            theme: Option[ThemeId],
            status: String,
            refusalReason: Option[String],
            tags: Seq[TagId],
            votes: Seq[Vote],
            context: RequestContext,
            createdAt: Option[ZonedDateTime],
            updatedAt: Option[ZonedDateTime],
            events: Option[Seq[ProposalAction]],
            similarProposals: Option[Seq[ProposalId]],
            idea: Option[IdeaId],
            ideaProposals: Seq[Proposal]): SingleProposal =
    js.Dynamic
      .literal(
        id = proposalId.value,
        slug = slug,
        content = content,
        author = author,
        labels = labels.toJSArray,
        theme = theme.orUndefined,
        status = status,
        refusalReason = refusalReason.orUndefined,
        tags = tags.toJSArray,
        votes = votes.toJSArray,
        context = context,
        createdAt = createdAt.map(_.toJSDate).orUndefined,
        updatedAt = updatedAt.map(_.toJSDate).orUndefined,
        events = events.map(_.toJSArray).orUndefined,
        similarProposals = similarProposals.map(_.toJSArray).orUndefined,
        idea = idea.orUndefined,
        ideaProposals = ideaProposals.toJSArray
      )
      .asInstanceOf[SingleProposal]
}

package org.make.backoffice.models

import java.time.ZonedDateTime

import io.circe.{Decoder, Encoder, Json}
import org.make.core.StringValue

import scala.scalajs.js
import js.JSConverters._
import org.make.core.JSConverters._

import scala.scalajs.js.Date

@js.native
trait ProposalId extends js.Object with StringValue {
  val value: String
}

object ProposalId {
  def apply(value: String): ProposalId = js.Dynamic.literal(value = value).asInstanceOf[ProposalId]

  implicit lazy val proposalIdEncoder: Encoder[ProposalId] = (a: ProposalId) => Json.fromString(a.value)
  implicit lazy val proposalIdDecoder: Decoder[ProposalId] = Decoder.decodeString.map(ProposalId(_))
}

@js.native
trait Qualification extends js.Object {
  val key: String
  val count: Int = 0
  val selected: Boolean = false
}

object Qualification {
  def apply(key: String, count: Int = 0, selected: Boolean = false): Qualification =
    js.Dynamic.literal(key = key, count = count, selected = selected).asInstanceOf[Qualification]
}

@js.native
trait Vote extends js.Object {
  val key: String
  val selected: Boolean = false
  val count: Int = 0
  val qualifications: Seq[Qualification]
}

object Vote {
  def apply(key: String, selected: Boolean = false, count: Int = 0, qualifications: Seq[Qualification]): Vote =
    js.Dynamic
      .literal(key = key, selected = selected, count = count, qualifications = qualifications)
      .asInstanceOf[Vote]
}

@js.native
trait ProposalContext extends js.Object {
  val operation: js.UndefOr[String]
  val source: js.UndefOr[String]
  val location: js.UndefOr[String]
  val question: js.UndefOr[String]
}

object ProposalContext {
  def apply(operation: Option[String],
            source: Option[String],
            location: Option[String],
            question: Option[String]): ProposalContext =
    js.Dynamic
      .literal(
        operation = operation.orUndefined,
        source = source.orUndefined,
        location = location.orUndefined,
        question = question.orUndefined
      )
      .asInstanceOf[ProposalContext]
}

@js.native
trait Author extends js.Object {
  val firstName: js.UndefOr[String]
  val postalCode: js.UndefOr[String]
  val age: js.UndefOr[Int]
}

object Author {
  def apply(firstName: Option[String], postalCode: Option[String], age: Option[Int]): Author =
    js.Dynamic
      .literal(firstName = firstName.orUndefined, postalCode = postalCode.orUndefined, age = age.orUndefined)
      .asInstanceOf[Author]
}

@js.native
trait Proposal extends js.Object {
  val id: String
  val userId: String
  val content: String
  val slug: String
  val status: String
  val createdAt: Date
  val updatedAt: js.UndefOr[Date]
  val votesAgree: Vote
  val votesDisagree: Vote
  val votesNeutral: Vote
  val proposalContext: ProposalContext
  val trending: js.UndefOr[String]
  val labels: Seq[String]
  val author: Author
  val country: String
  val language: String
  val themeId: js.UndefOr[String]
  val tags: Seq[Tag]
}

object Proposal {
  def apply(id: ProposalId,
            userId: UserId,
            content: String,
            slug: String,
            status: String,
            createdAt: ZonedDateTime,
            updatedAt: Option[ZonedDateTime],
            votesAgree: Vote,
            votesDisagree: Vote,
            votesNeutral: Vote,
            proposalContext: ProposalContext,
            trending: Option[String],
            labels: Seq[String],
            author: Author,
            country: String,
            language: String,
            themeId: Option[ThemeId],
            tags: Seq[Tag]): Proposal =
    js.Dynamic
      .literal(
        id = id.value,
        userId = userId.value,
        content = content,
        slug = slug,
        status = status,
        createdAt = createdAt.toJSDate,
        updatedAt = updatedAt.map(_.toJSDate).orUndefined,
        votesAgree = votesAgree,
        votesDisagree = votesDisagree,
        votesNeutral = votesNeutral,
        proposalContext = proposalContext,
        trending = trending.orUndefined,
        labels = labels,
        author = author,
        country = country,
        language = language,
        themeId = themeId.map(_.value).orUndefined,
        tags = tags
      )
      .asInstanceOf[Proposal]
}

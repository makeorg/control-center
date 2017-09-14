package org.make.backoffice.models

import java.time.ZonedDateTime

import io.circe.{Decoder, Encoder, Json}
import org.make.core.StringValue

import scala.scalajs.js
import js.JSConverters._
import org.make.core.JSConverters._
import org.make.services.proposal.ProposalStatus

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
}

object Qualification {
  def apply(key: String, count: Int = 0): Qualification =
    js.Dynamic.literal(key = key, count = count).asInstanceOf[Qualification]
}

@js.native
trait Vote extends js.Object {
  val key: String
  val count: Int = 0
  val qualifications: js.Array[Qualification]
}

object Vote {
  def apply(key: String, count: Int = 0, qualifications: js.Array[Qualification]): Vote =
    js.Dynamic
      .literal(key = key, count = count, qualifications = qualifications)
      .asInstanceOf[Vote]
}

@js.native
trait Context extends js.Object {
  val operation: js.UndefOr[String]
  val source: js.UndefOr[String]
  val location: js.UndefOr[String]
  val question: js.UndefOr[String]
}

object Context {
  def apply(operation: Option[String],
            source: Option[String],
            location: Option[String],
            question: Option[String]): Context =
    js.Dynamic
      .literal(
        operation = operation.orUndefined,
        source = source.orUndefined,
        location = location.orUndefined,
        question = question.orUndefined
      )
      .asInstanceOf[Context]
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
  val votes: js.Array[Vote]
  val context: Option[Context]
  val trending: js.UndefOr[String]
  val labels: js.Array[String]
  val author: Author
  val country: String
  val language: String
  val themeId: js.UndefOr[String]
  val tags: js.Array[Tag]
}

object Proposal {
  def apply(id: ProposalId,
            userId: UserId,
            content: String,
            slug: String,
            status: ProposalStatus,
            createdAt: ZonedDateTime,
            updatedAt: Option[ZonedDateTime],
            votes: js.Array[Vote],
            context: Option[Context],
            trending: Option[String],
            labels: js.Array[String],
            author: Author,
            country: String,
            language: String,
            themeId: Option[ThemeId],
            tags: js.Array[Tag]): Proposal = {
    js.Dynamic
      .literal(
        id = id.value,
        userId = userId.value,
        content = content,
        slug = slug,
        status = status.shortName,
        createdAt = createdAt.toJSDate,
        updatedAt = updatedAt.map(_.toJSDate).orUndefined,
        votes = votes,
        context = context.orUndefined,
        trending = trending.orUndefined,
        labels = labels.toJSArray,
        author = author,
        country = country,
        language = language,
        themeId = themeId.map(_.value).orUndefined,
        tags = tags
      )
      .asInstanceOf[Proposal]
  }

  def totalVotes(proposal: Proposal): Int = {
    proposal.votes.map(_.count).sum
  }
}

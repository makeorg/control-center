package org.make.backoffice.model

import java.time.ZonedDateTime

import io.circe.{Decoder, Encoder, Json}
import org.make.backoffice.util.FormatToPercent._

import scala.scalajs.js
import js.JSConverters._
import org.make.backoffice.util.JSConverters._
import org.make.backoffice.service.proposal.ProposalStatus

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
  def apply(key: String, count: Int = 0, qualifications: Seq[Qualification]): Vote =
    js.Dynamic
      .literal(key = key, count = count, qualifications = qualifications.toJSArray)
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
  val context: Context
  val trending: js.UndefOr[String]
  val labels: js.Array[String]
  val author: Author
  val country: String
  val language: String
  val themeId: js.UndefOr[String]
  val tagIds: js.Array[String]
  val ideaId: js.UndefOr[String]
  val operationId: js.UndefOr[String]
}

object Proposal {
  def apply(id: ProposalId,
            userId: UserId,
            content: String,
            slug: String,
            status: ProposalStatus,
            createdAt: ZonedDateTime,
            updatedAt: Option[ZonedDateTime],
            votes: Seq[Vote],
            context: Context,
            trending: Option[String],
            labels: Seq[String],
            author: Author,
            country: String,
            language: String,
            themeId: Option[ThemeId],
            tags: Seq[Tag],
            ideaId: Option[IdeaId],
            operationId: Option[OperationId]): Proposal = {
    js.Dynamic
      .literal(
        id = id.value,
        userId = userId.value,
        content = content,
        slug = slug,
        status = status.shortName,
        createdAt = createdAt.toJSDate,
        updatedAt = updatedAt.map(_.toJSDate).orUndefined,
        votes = votes.toJSArray,
        context = context,
        trending = trending.orUndefined,
        labels = labels.toJSArray,
        author = author,
        country = country,
        language = language,
        themeId = themeId.map(_.value).orUndefined,
        tagIds = tags.map(_.id).toJSArray,
        ideaId = ideaId.map(_.value).orUndefined,
        operationId = operationId.map(_.value).orUndefined
      )
      .asInstanceOf[Proposal]
  }

  def totalVotes(votes: Seq[Vote]): Int = {
    votes.map(_.count).sum
  }

  def totalQualifications(qualifications: Seq[Qualification]): Int = {
    qualifications.map(_.count).sum
  }

  def voteFromKey(votes: Seq[Vote], key: String): Int = {
    votes
      .find(_.key == key)
      .map { vote =>
        vote.count
      }
      .getOrElse(0)
  }

  def qualificationFromKey(qualifications: Seq[Qualification], key: String): Int = {
    qualifications
      .find(_.key == key)
      .map { qualification =>
        qualification.count
      }
      .getOrElse(0)
  }

  def voteRate(votes: Seq[Vote], key: String): Int = {
    votes
      .find(_.key == key)
      .map { vote =>
        formatToPercent(vote.count, totalVotes(votes))
      }
      .getOrElse(0)
  }

  def qualificationRate(qualifications: Seq[Qualification], key: String): Int = {
    qualifications
      .find(_.key == key)
      .map { qualification =>
        formatToPercent(qualification.count, totalQualifications(qualifications))
      }
      .getOrElse(0)
  }
}

@js.native
trait SimilarResult extends js.Object {
  val ideaId: String
  val ideaName: String
}

object SimilarResult {
  def apply(ideaId: IdeaId, ideaName: String): SimilarResult =
    js.Dynamic.literal(ideaId = ideaId.value, ideaName = ideaName).asInstanceOf[SimilarResult]
}

@js.native
trait ProposalsResult extends js.Object {
  val total: Int
  val results: js.Array[Proposal]
}

object ProposalsResult {
  def apply(total: Int, results: Seq[Proposal]): ProposalsResult =
    js.Dynamic.literal(total = total, results = results.toJSArray).asInstanceOf[ProposalsResult]

  def empty: ProposalsResult =
    ProposalsResult(total = 0, results = Seq.empty)
}

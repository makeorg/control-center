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

import io.circe.{Decoder, Encoder, Json}
import org.make.backoffice.service.proposal.ProposalStatus
import org.make.backoffice.util.FormatToPercent._
import org.make.backoffice.util.JSConverters._

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.scalajs.js.JSConverters._

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
trait ProposalIdResult extends js.Object with StringValue {
  val proposalId: ProposalId
}

object ProposalIdResult {
  def apply(proposalId: String): ProposalIdResult =
    js.Dynamic.literal(proposalId = ProposalId(proposalId)).asInstanceOf[ProposalIdResult]
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
trait Author extends js.Object {
  val firstName: js.UndefOr[String]
  val postalCode: js.UndefOr[String]
  val age: js.UndefOr[Int]
  val userType: String
}

object Author {
  def apply(firstName: Option[String], postalCode: Option[String], age: Option[Int], userType: String): Author =
    js.Dynamic
      .literal(
        firstName = firstName.orUndefined,
        postalCode = postalCode.orUndefined,
        age = age.orUndefined,
        userType = userType
      )
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
  val author: Author
  val tagIds: js.Array[String]
  val ideaId: js.UndefOr[String]
  val questionId: js.UndefOr[String]
  val toEnrich: js.UndefOr[Boolean]
  val initialProposal: Boolean
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
            author: Author,
            tags: Seq[IndexedTag],
            ideaId: Option[IdeaId],
            questionId: Option[QuestionId],
            toEnrich: Option[Boolean],
            initialProposal: Boolean): Proposal = {
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
        author = author,
        tagIds = tags.map(_.id).toJSArray,
        ideaId = ideaId.map(_.value).orUndefined,
        questionId = questionId.map(_.value).orUndefined,
        toEnrich = toEnrich.orUndefined,
        initialProposal = initialProposal
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

  def qualificationRate(vote: Vote, key: String): Int = {
    val qualificationCount = qualificationFromKey(vote.qualifications, key)
    formatToPercent(qualificationCount, vote.count)
  }
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

@js.native
trait PredictedTagsWithModelResponse extends js.Object {
  val tags: js.Array[PredictedTag]
  val modelName: js.UndefOr[String]
}

object PredictedTagsWithModelResponse {
  def apply(tags: Seq[PredictedTag], modelName: Option[String]): PredictedTagsWithModelResponse =
    js.Dynamic
      .literal(tags = tags.toJSArray, modelName = modelName.orUndefined)
      .asInstanceOf[PredictedTagsWithModelResponse]

  implicit lazy val decoder: Decoder[PredictedTagsWithModelResponse] =
    Decoder.forProduct2("tags", "modelName")(PredictedTagsWithModelResponse.apply)
}

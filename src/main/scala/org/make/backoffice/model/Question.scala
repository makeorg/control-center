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
import java.time.LocalDate

import io.circe.{Decoder, Encoder, Json}

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

@js.native
trait QuestionId extends js.Object with StringValue {
  override val value: String
}

object QuestionId {
  def apply(value: String): QuestionId = js.Dynamic.literal(value = value).asInstanceOf[QuestionId]

  implicit lazy val questionIdEncoder: Encoder[QuestionId] = (a: QuestionId) => Json.fromString(a.value)
  implicit lazy val questionIdDecoder: Decoder[QuestionId] = Decoder.decodeString.map(QuestionId(_))
}

@js.native
trait Question extends js.Object {
  val id: String
  val question: String
  val slug: String
  val operationId: js.UndefOr[String]
  val startDate: js.UndefOr[String]
  val endDate: js.UndefOr[String]
  val landingSequenceId: js.UndefOr[String]
  val operationTitle: js.UndefOr[String]
  val country: js.UndefOr[String]
  val language: js.UndefOr[String]
}

object Question {
  def apply(questionId: QuestionId,
            question: String,
            slug: String,
            operationId: Option[OperationId],
            startDate: Option[LocalDate],
            endDate: Option[LocalDate],
            landingSequenceId: Option[String],
            operationTitle: Option[String],
            country: Option[String],
            language: Option[String]): Question =
    js.Dynamic
      .literal(
        id = questionId.value,
        question = question,
        slug = slug,
        operationId = operationId.map(_.value).orUndefined,
        startDate = startDate.map(_.toString).orUndefined,
        endDate = endDate.map(_.toString).orUndefined,
        landingSequenceId = landingSequenceId.orUndefined,
        operationTitle = operationTitle.orUndefined,
        country = country.orUndefined,
        language = language.orUndefined
      )
      .asInstanceOf[Question]

  @js.native
  trait DataConfiguration extends js.Object {
    val newProposalsRatio: Double
    val newProposalsVoteThreshold: Int
    val testedProposalsEngagementThreshold: Double
    val testedProposalsScoreThreshold: Double
    val testedProposalsControversyThreshold: Double
    val testedProposalsMaxVotesThreshold: Int
    val intraIdeaEnabled: Boolean
    val intraIdeaMinCount: Int
    val intraIdeaProposalsRatio: Double
    val interIdeaCompetitionEnabled: Boolean
    val interIdeaCompetitionTargetCount: Int
    val interIdeaCompetitionControversialRatio: Double
    val interIdeaCompetitionControversialCount: Int
    val maxTestedProposalCount: Int
    val sequenceSize: Int
    val selectionAlgorithmName: String
  }

  object DataConfiguration {
    def apply(newProposalsRatio: Double,
              newProposalsVoteThreshold: Int,
              maybeTestedProposalsEngagementThreshold: Option[Double],
              maybeTestedProposalsScoreThreshold: Option[Double],
              maybeTestedProposalsControversyThreshold: Option[Double],
              maybeTestedProposalsMaxVotesThreshold: Option[Int],
              intraIdeaEnabled: Boolean,
              intraIdeaMinCount: Int,
              intraIdeaProposalsRatio: Double,
              interIdeaCompetitionEnabled: Boolean,
              interIdeaCompetitionTargetCount: Int,
              interIdeaCompetitionControversialRatio: Double,
              interIdeaCompetitionControversialCount: Int,
              maxTestedProposalCount: Int,
              sequenceSize: Int,
              selectionAlgorithmName: String): DataConfiguration = {
      val testedProposalsEngagementThreshold: Double = maybeTestedProposalsEngagementThreshold.getOrElse(0)
      val testedProposalsScoreThreshold: Double = maybeTestedProposalsScoreThreshold.getOrElse(0)
      val testedProposalsControversyThreshold: Double = maybeTestedProposalsControversyThreshold.getOrElse(0)
      val testedProposalsMaxVotesThreshold: Int = maybeTestedProposalsMaxVotesThreshold.getOrElse(0)
      js.Dynamic
        .literal(
          newProposalsRatio = newProposalsRatio,
          newProposalsVoteThreshold = newProposalsVoteThreshold,
          testedProposalsEngagementThreshold = testedProposalsEngagementThreshold,
          testedProposalsScoreThreshold = testedProposalsScoreThreshold,
          testedProposalsControversyThreshold = testedProposalsControversyThreshold,
          testedProposalsMaxVotesThreshold = testedProposalsMaxVotesThreshold,
          intraIdeaEnabled = intraIdeaEnabled,
          intraIdeaMinCount = intraIdeaMinCount,
          intraIdeaProposalsRatio = intraIdeaProposalsRatio,
          interIdeaCompetitionEnabled = interIdeaCompetitionEnabled,
          interIdeaCompetitionTargetCount = interIdeaCompetitionTargetCount,
          interIdeaCompetitionControversialRatio = interIdeaCompetitionControversialRatio,
          interIdeaCompetitionControversialCount = interIdeaCompetitionControversialCount,
          maxTestedProposalCount = maxTestedProposalCount,
          sequenceSize = sequenceSize,
          selectionAlgorithmName = selectionAlgorithmName
        )
        .asInstanceOf[DataConfiguration]
    }
  }
}

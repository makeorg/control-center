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
trait ResultsLink extends js.Object {
  val kind: String
  val value: String
}

object ResultsLink {
  def apply(kind: String, value: String): ResultsLink =
    js.Dynamic
      .literal(kind = kind, value = value)
      .asInstanceOf[ResultsLink]
}

@js.native
trait Question extends js.Object {
  val id: String
  val question: String
  val slug: String
  val operationId: js.UndefOr[String]
  val startDate: String
  val endDate: String
  val operationTitle: js.UndefOr[String]
  val countries: js.Array[String]
  val language: js.UndefOr[String]
  val consultationImage: js.UndefOr[String]
  val descriptionImage: js.UndefOr[String]
  val resultsLink: js.UndefOr[ResultsLink]
}

object Question {
  def apply(questionId: QuestionId,
            question: String,
            slug: String,
            operationId: Option[OperationId],
            startDate: ZonedDateTime,
            endDate: ZonedDateTime,
            operationTitle: Option[String],
            countries: Seq[String],
            language: Option[String],
            consultationImage: Option[String],
            descriptionImage: Option[String],
            resultsLink: Option[ResultsLink]): Question =
    js.Dynamic
      .literal(
        id = questionId.value,
        question = question,
        slug = slug,
        operationId = operationId.map(_.value).orUndefined,
        startDate = startDate.toString,
        endDate = endDate.toString,
        operationTitle = operationTitle.orUndefined,
        countries = countries.toJSArray,
        language = language.orUndefined,
        consultationImage = consultationImage.orUndefined,
        descriptionImage = descriptionImage.orUndefined,
        resultsLink = resultsLink.orUndefined
      )
      .asInstanceOf[Question]

  @js.native
  trait DataConfiguration extends js.Object {
    val newProposalsRatio: Double
    val newProposalsVoteThreshold: Int
    val testedProposalsEngagementThreshold: js.UndefOr[Double]
    val testedProposalsScoreThreshold: js.UndefOr[Double]
    val testedProposalsControversyThreshold: js.UndefOr[Double]
    val testedProposalsMaxVotesThreshold: js.UndefOr[Int]
    val nonSequenceVotesWeight: Double
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
              testedProposalsEngagementThreshold: Option[Double],
              testedProposalsScoreThreshold: Option[Double],
              testedProposalsControversyThreshold: Option[Double],
              testedProposalsMaxVotesThreshold: Option[Int],
              nonSequenceVotesWeight: Double,
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
      js.Dynamic
        .literal(
          newProposalsRatio = newProposalsRatio,
          newProposalsVoteThreshold = newProposalsVoteThreshold,
          testedProposalsEngagementThreshold = testedProposalsEngagementThreshold.orUndefined,
          testedProposalsScoreThreshold = testedProposalsScoreThreshold.orUndefined,
          testedProposalsControversyThreshold = testedProposalsControversyThreshold.orUndefined,
          testedProposalsMaxVotesThreshold = testedProposalsMaxVotesThreshold.orUndefined,
          nonSequenceVotesWeight = nonSequenceVotesWeight,
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

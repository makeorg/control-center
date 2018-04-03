package org.make.backoffice.model

import java.time.ZonedDateTime

import io.circe.{Decoder, Encoder, Json}
import org.make.backoffice.util.JSConverters._

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.scalajs.js.JSConverters._

@js.native
trait IdeaId extends js.Object with StringValue {
  val value: String
}

object IdeaId {
  def apply(value: String): IdeaId = js.Dynamic.literal(value = value).asInstanceOf[IdeaId]

  implicit lazy val ideaIdEncoder: Encoder[IdeaId] = (a: IdeaId) => Json.fromString(a.value)
  implicit lazy val ideaIdDecoder: Decoder[IdeaId] = Decoder.decodeString.map(IdeaId(_))
}

@js.native
trait Idea extends js.Object {
  val id: String
  val name: String
  val operationId: js.UndefOr[String]
  val themeId: js.UndefOr[String]
  val question: js.UndefOr[String]
  val country: js.UndefOr[String]
  val language: js.UndefOr[String]
  val createdAt: Date
  val updatedAt: js.UndefOr[Date]

}

object Idea {
  def apply(ideaId: IdeaId,
            name: String,
            language: Option[String],
            country: Option[String],
            operationId: Option[OperationId],
            themeId: Option[ThemeId],
            question: Option[String],
            createdAt: ZonedDateTime,
            updatedAt: Option[ZonedDateTime]): Idea = {
    js.Dynamic
      .literal(
        id = ideaId.value,
        name = name,
        operationId = operationId.map(_.value).orUndefined,
        themeId = themeId.map(_.value).orUndefined,
        question = question.orUndefined,
        country = country.orUndefined,
        language = language.orUndefined,
        createdAt = createdAt.toJSDate,
        updatedAt = updatedAt.map(_.toJSDate).orUndefined
      )
      .asInstanceOf[Idea]
  }
}

@js.native
trait IdeasResult extends js.Object {
  val total: Int
  val results: js.Array[Idea]
}

object IdeasResult {
  def apply(total: Int, results: Seq[Idea]): IdeasResult =
    js.Dynamic.literal(total = total, results = results.toJSArray).asInstanceOf[IdeasResult]

  def empty: IdeasResult = IdeasResult(total = 0, results = Seq.empty)
}

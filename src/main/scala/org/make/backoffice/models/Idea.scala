package org.make.backoffice.models

import io.circe.{Decoder, Encoder, Json}
import org.make.core.StringValue

import scalajs.js
import js.JSConverters._

@js.native
trait IdeaId extends js.Object with StringValue {
  val value: String
}

object IdeaId {
  def apply(value: String): IdeaId = js.Dynamic.literal(value = value).asInstanceOf[IdeaId]

  implicit lazy val proposalIdEncoder: Encoder[IdeaId] = (a: IdeaId) => Json.fromString(a.value)
  implicit lazy val proposalIdDecoder: Decoder[IdeaId] = Decoder.decodeString.map(IdeaId(_))
}

@js.native
trait Idea extends js.Object {
  val ideaId: IdeaId
  val name: String
  val language: js.UndefOr[String]
  val country: js.UndefOr[String]
  val operation: js.UndefOr[String]
  val question: js.UndefOr[String]
}

object Idea {
  def apply(ideaId: IdeaId,
            name: String,
            language: Option[String],
            country: Option[String],
            operation: Option[String],
            question: Option[String]): Idea = {
    js.Dynamic
      .literal(
        ideaId = ideaId,
        name = name,
        language = language.orUndefined,
        country = country.orUndefined,
        operation = operation.orUndefined,
        question = question.orUndefined
      )
      .asInstanceOf[Idea]
  }
}

package org.make.backoffice.model

import io.circe.{Decoder, Encoder, Json}

import scala.scalajs.js
import js.JSConverters._

@js.native
trait ThemeTranslation extends js.Object {
  val slug: String
  val title: String
  val language: String
}

object ThemeTranslation {
  def apply(slug: String, title: String, language: String): ThemeTranslation =
    js.Dynamic.literal(slug = slug, title = title, language = language).asInstanceOf[ThemeTranslation]
}

@js.native
trait GradientColor extends js.Object {
  val from: String
  val to: String
}

object GradientColor {
  def apply(from: String, to: String): GradientColor =
    js.Dynamic.literal(from = from, to = to).asInstanceOf[GradientColor]
}

@js.native
trait Theme extends js.Object {
  val themeId: ThemeId
  val translations: js.Array[ThemeTranslation]
  val actionsCount: Int
  val proposalsCount: Int
  val country: String
  val color: String
  val gradient: js.UndefOr[GradientColor]
  val tags: js.Array[Tag]
}

object Theme {
  def apply(themeId: ThemeId,
            translations: Seq[ThemeTranslation],
            actionsCount: Int,
            proposalsCount: Int,
            country: String,
            color: String,
            gradient: Option[GradientColor],
            tags: Seq[Tag]): Theme =
    js.Dynamic
      .literal(
        themeId = themeId,
        translations = translations.toJSArray,
        actionsCount = actionsCount,
        proposalsCount = proposalsCount,
        country = country,
        color = color,
        gradient = gradient.orUndefined,
        tags = tags.toJSArray
      )
      .asInstanceOf[Theme]
}

@js.native
trait ThemeId extends js.Object with StringValue {
  value: String
}

object ThemeId {
  def apply(value: String): ThemeId = js.Dynamic.literal(value = value).asInstanceOf[ThemeId]

  implicit lazy val themeIdEncoder: Encoder[ThemeId] = (a: ThemeId) => Json.fromString(a.value)
  implicit lazy val themeIdDecoder: Decoder[ThemeId] = Decoder.decodeString.map(ThemeId(_))
}
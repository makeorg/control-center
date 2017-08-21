package org.make.backoffice.models

import io.circe.{Decoder, Encoder, Json}
import org.make.core.StringValue

import scala.scalajs.js

@js.native
trait Theme extends js.Object {
  val themeId: ThemeId
  val label: String
}

object Theme {
  def apply(themeId: ThemeId, label: String): Theme =
    js.Dynamic.literal(themeId = themeId, label = label).asInstanceOf[Theme]
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

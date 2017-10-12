package org.make.backoffice.helpers

import io.circe.parser.parse
import org.make.backoffice.facades.Choice
import org.make.backoffice.models.{BusinessConfig, Tag, ThemeId}
import org.make.core.CirceClassFormatters
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}
import scala.scalajs.js.JSConverters._

object Configuration extends CirceClassFormatters {

  val defaultProposalMaxLength: Int = 256

  val defaultLanguage: String = "fr"

  val businessConfig: Option[BusinessConfig] =
    parse(dom.window.localStorage.getItem("Configuration")).flatMap(_.as[BusinessConfig]) match {
      case Right(bc) =>
        Some(bc)
      case Left(error) =>
        g.console.log(error.getMessage)
        None
    }

  def getReasonsForRefusal: Seq[String] = {
    businessConfig.map { bc =>
      bc.reasonsForRefusal.toSeq
    }.getOrElse(Seq.empty)
  }

  def getThemeFromThemeId(themeId: ThemeId): String = {
    businessConfig.flatMap { bc =>
      bc.themes.toArray.find(_.themeId.value == themeId.value).flatMap { theme =>
        theme.translations.toArray.find(_.language == defaultLanguage).map(_.title)
      }
    }.getOrElse("")
  }

  def getTagsFromThemeId(themeId: ThemeId): Seq[Tag] = {
    businessConfig.flatMap { bc =>
      bc.themes.toArray.find(_.themeId.value == themeId.value).map { theme =>
        theme.tags.toSeq
      }
    }.getOrElse(Seq.empty)
  }

  def choicesThemeFilter: js.Array[Choice] = {
    businessConfig.map { bc =>
      bc.themes.map(
        theme =>
          Choice(
            id = theme.themeId.value,
            name = theme.translations.toArray.find(_.language == defaultLanguage).map(_.title).getOrElse("")
        )
      )
    }.getOrElse(Seq.empty.toJSArray)
  }

  def choicesTagsFilter: js.Array[Choice] = {

    businessConfig.map { bc =>
      bc.themes
        .flatMap(theme => theme.tags)
        .groupBy(_.tagId.value)
        .map {
          case (_, tags) => Choice(id = tags.head.tagId.value, name = tags.head.label)
        }
        .toJSArray
    }.getOrElse(Seq.empty.toJSArray)
  }
}
